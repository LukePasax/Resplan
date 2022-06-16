package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.RMS;

/**
 * This class represents a gate, which is a tool that reduces or eliminates noise coming from an audio source.
 * Specifically, a gate parses the audio signal and reduces the volume of all the samples that do not reach
 * a certain volume threshold.
 */
public class GateEffect extends UGen {

    private final int channels;
    private final int memSize;
    private int index = 0;
    private final float[][] delayMem;
    private UGen powerUGen;
    private final BiquadFilter pf;
    private float downstep = .9998f, upstep = 1.0002f, ratio = .5f,
            threshold = .5f, knee = 1;
    private float tok, kt, ikp1, ktrm1, tt1mr;

    private float attack, decay;
    private float currval = 1;
    private float delay;
    private final int delaySamps;
    private int rmsMemorySize = 500;
    private UGen myInputs;
    private final float[][] myBufIn;

    /**
     * Constructs a gate and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public GateEffect(@JsonProperty("ins") int channels) {
        this(AudioContextManager.getAudioContext(), channels);
    }

    GateEffect(AudioContext context, int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.channels = channels;
        this.delay = 0;
        this.delaySamps = 0;
        this.memSize = (int) context.msToSamples(this.delay) + 1;
        this.delayMem = new float[channels][this.memSize];
        this.myBufIn = this.bufIn;
        class MyInputs extends UGen {
            MyInputs(AudioContext context, int channels) {
                super(context, 0, channels);
                this.bufOut = GateEffect.this.myBufIn;
                this.outputInitializationRegime = OutputInitializationRegime.RETAIN;
            }
            @Override
            public void calculateBuffer() {
            }
        }
        this.myInputs = new MyInputs(context, channels);
        this.sendData(new DataBead("attack", 1.0f, "decay", 0.5f, "ratio", 2.0f,
                "threshold", 0.5f, "knee", 0.5f));
        this.pf = (new BiquadFilter(context, 1, BiquadFilter.BUTTERWORTH_LP))
                .setFrequency(31);
        this.powerUGen = new RMS(context, channels, this.rmsMemorySize);
        // myInputs -> powerUGen -> pf
        this.powerUGen.addInput(this.myInputs);
        this.pf.addInput(this.powerUGen);
        this.calcVals();
    }

    public void sendData(DataBead db) {
        if (db != null) {
            this.setThreshold(db.getFloat("threshold", threshold));
            this.setRatio(db.getFloat("ratio", ratio));
            this.setAttack(db.getFloat("attack", attack));
            this.setDecay(db.getFloat("decay", decay));
            this.setKnee(db.getFloat("knee", knee));
        }
    }

    private void calcVals() {
        this.tok = this.threshold / this.knee;
        this.kt = this.knee * this.threshold;
        this.ikp1 = 1 / (this.knee + 1);
        this.ktrm1 = this.knee * this.ratio - 1;
        this.tt1mr = this.threshold * (1 - this.ratio);
    }

    public void setAttack(float attack) {
        if (attack < .0001f) {
            attack = .0001f;
        }
        this.attack = attack;
        this.downstep = (float) Math.pow(Math.pow(10,attack/20f), -1000f/this.context.getSampleRate());
    }

    private void setDecay(float decay) {
        if (decay < .0001f) {
            decay = .0001f;
        }
        this.decay = decay;
        this.upstep = (float) Math.pow(Math.pow(10,decay/20f), 1000f/this.context.getSampleRate());
    }

    private void setRatio(float ratio) {
        if (ratio <= 0) {
            ratio = .01f;
        }
        this.ratio = 1 / ratio;
        this.calcVals();
    }

    private void setThreshold(float threshold) {
        this.threshold = threshold;
        this.calcVals();
    }

    private void setKnee(float knee) {
        this.knee = knee + 1;
        this.calcVals();
    }

    @Override
    public void calculateBuffer() {
        this.pf.update();
        float target;
        if (this.channels == 1) {
            float[] bi = this.bufIn[0];
            float[] bo = this.bufOut[0];
            float[] dm = this.delayMem[0];
            for (int i = 0; i < this.bufferSize; i++) {
                float p = this.pf.getValue(0, i);
                if (p <= this.tok) {
                    target = 1;
                } else if (p >= this.kt) {
                    target = ((p - this.threshold) * this.ratio + this.threshold) / p;
                } else {
                    float x1 = (p - this.tok) * this.ikp1 + this.tok;
                    target = ((this.ktrm1*x1+this.tt1mr) * (p-x1) / (x1*(this.knee-1)) + x1)  /  p;
                }
                this.setCurrentValue(target);
                dm[this.index] = bi[i];
                bo[i] = dm[(this.index + this.delaySamps) % this.memSize] * this.currval;
                this.index = (this.index + 1) % this.memSize;
            }
        } else {
            for (int i = 0; i < this.bufferSize; i++) {
                float p = this.pf.getValue(0, i);
                if (p <= this.tok) {
                    target = 1;
                } else if (p >= this.kt) {
                    target = ((p - this.threshold) * this.ratio + this.threshold) / p;
                } else {
                    float x1 = (p - this.tok) * this.ikp1 + this.tok;
                    target = (this.ktrm1*x1+this.tt1mr) * (p-x1) / (x1*(this.knee-1)) + x1;
                }
                this.setCurrentValue(target);
                int delIndex = (this.index + this.delaySamps) % this.memSize;
                for (int j = 0; j < this.channels; j++) {
                    this.delayMem[j][this.index] = this.bufIn[j][i];
                    this.bufOut[j][i] = this.delayMem[j][delIndex] * this.currval;
                }
                this.index = (this.index + 1) % this.memSize;
            }
        }
    }

    private void setCurrentValue(float target) {
        if (this.currval < target) {
            this.currval *= this.downstep;
            if (this.currval < target)
                this.currval = target;
        } else if (this.currval > target) {
            this.currval *= this.upstep;
            if (this.currval > target)
                this.currval = target;
        }
    }

    float getThreshold() {
        return this.threshold;
    }

    float getRatio() {
        return 1/this.ratio;
    }

    float getAttack() {
        return this.attack;
    }

    float getDecay() {
        return this.decay;
    }

    float getCurrentCompression() {
        return this.currval;
    }

}

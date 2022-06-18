package daw.core.audioprocessing;

import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.data.DataBead;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.RMS;

// package protection since it is wrapped by Gate
class GateEffect extends UGen {

    private final int channels;
    private final int memSize;
    private int index = 0;
    private final float[][] delayMem;
    private final BiquadFilter pf;
    private float downStep = .9998f;
    private float upStep = 1.0002f;
    private float ratio = .5f;
    private float threshold = .5f;
    private float knee = 1;
    private float tok;
    private float kt;
    private float ikp1;
    private float kTrm1;
    private float tt1mr;
    private float attack;
    private float decay;
    private float currentValue = 1;
    private final int delaySamples;
    private final float[][] myBufIn;

    GateEffect(AudioContext context, int channels) {
        super(AudioContextManager.getAudioContext(), channels, channels);
        this.channels = channels;
        this.delaySamples = 0;
        this.memSize = (int) context.msToSamples(0) + 1;
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
        final UGen myInputs = new MyInputs(context, channels);
        this.pf = new BiquadFilter(context, 1, BiquadFilter.BUTTERWORTH_LP).setFrequency(31);
        final UGen powerUGen = new RMS(context, channels, 500);
        // myInputs -> powerUGen -> pf
        powerUGen.addInput(myInputs);
        this.pf.addInput(powerUGen);
        this.sendData(new DataBead("attack", 1.0f, "decay", 0.5f, "ratio", 2.0f,
                "threshold", 0.5f, "knee", 0.5f));
        this.calculateCurrentValues();
    }

    private void calculateCurrentValues() {
        this.tok = this.threshold / this.knee;
        this.kt = this.knee * this.threshold;
        this.ikp1 = 1 / (this.knee + 1);
        this.kTrm1 = this.knee * this.ratio - 1;
        this.tt1mr = this.threshold * (1 - this.ratio);
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

    public void setAttack(float attack) {
        if (attack < .0001f) {
            attack = .0001f;
        }
        this.attack = attack;
        this.downStep = (float) Math.pow(Math.pow(10,attack/20f), -1000f/this.context.getSampleRate());
    }

    private void setDecay(float decay) {
        if (decay < .0001f) {
            decay = .0001f;
        }
        this.decay = decay;
        this.upStep = (float) Math.pow(Math.pow(10,decay/20f), 1000f/this.context.getSampleRate());
    }

    private void setRatio(float ratio) {
        if (ratio <= 0) {
            ratio = .01f;
        }
        this.ratio = 1 / ratio;
        this.calculateCurrentValues();
    }

    private void setThreshold(float threshold) {
        this.threshold = threshold;
        this.calculateCurrentValues();
    }

    private void setKnee(float knee) {
        this.knee = knee + 1;
        this.calculateCurrentValues();
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
                    target = ((this.kTrm1 *x1+this.tt1mr) * (p-x1) / (x1*(this.knee-1)) + x1)  /  p;
                }
                this.setCurrentValue(target);
                dm[this.index] = bi[i];
                bo[i] = dm[(this.index + this.delaySamples) % this.memSize] * this.currentValue;
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
                    target = (this.kTrm1 *x1+this.tt1mr) * (p-x1) / (x1*(this.knee-1)) + x1;
                }
                this.setCurrentValue(target);
                int delIndex = (this.index + this.delaySamples) % this.memSize;
                for (int j = 0; j < this.channels; j++) {
                    this.delayMem[j][this.index] = this.bufIn[j][i];
                    this.bufOut[j][i] = this.delayMem[j][delIndex] * this.currentValue;
                }
                this.index = (this.index + 1) % this.memSize;
            }
        }
    }

    private void setCurrentValue(float target) {
        if (this.currentValue < target) {
            this.currentValue *= this.downStep;
            if (this.currentValue < target)
                this.currentValue = target;
        } else if (this.currentValue > target) {
            this.currentValue *= this.upStep;
            if (this.currentValue > target)
                this.currentValue = target;
        }
    }

    // package protection since these methods are only used by Gate

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
        return this.currentValue;
    }

}

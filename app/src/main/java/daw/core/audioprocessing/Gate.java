package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.BiquadFilter;
import net.beadsproject.beads.ugens.RMS;

/**
 * This class represents a gate, which is a tool that reduces or eliminates noise coming from an audio source.
 * Specifically, a gate parses the audio signal and reduces the volume of all the samples that do not reach
 * a certain volume threshold.
 */
public class Gate extends AbstractCompression {

    private final int channels;
    private final int memSize;
    private int index = 0;
    private final float[][] delayMem;
    private UGen powerUGen;
    private BiquadFilter pf;
    private float downstep = .9998f, upstep = 1.0002f, ratio = .5f, threshold = .5f, knee = 1;
    private float tok, kt, ikp1, ktrm1, tt1mr;
    private float attack, decay;
    private float currval = 1, target = 1, delay;
    private int delaySamps;
    private int rmsMemorySize = 500;
    private UGen myInputs;
    private float[][] myBufIn;

    /**
     * Constructs a gate and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public Gate(@JsonProperty("ins") int channels) {
        this(AudioContextManager.getAudioContext(), channels, 0, null);
    }

    /**
     * Constructor for a multi-channel compressor with the specified look-ahead
     * time and side-chain, and other parameters set to their default values.
     *
     * @param context
     *            The audio context.
     * @param channels
     *            The number of channels.
     * @param lookAheadDelay
     *            The look-ahead time in milliseconds.
     * @param sideChain
     *            The UGen to use as the side-chain.
     */
    public Gate(AudioContext context, int channels, float lookAheadDelay,
                      UGen sideChain) {
        super(channels);
        this.channels = channels;
        this.delay = lookAheadDelay;
        this.delaySamps = (int) this.delay;
        this.memSize = (int) context.msToSamples(this.delay) + 1;
        this.delayMem = new float[channels][this.memSize];
        this.myBufIn = this.bufIn;

        class MyInputs extends UGen {
            MyInputs(AudioContext context, int channels) {
                super(context, 0, channels);
                this.bufOut = Gate.this.myBufIn;
                this.outputInitializationRegime = OutputInitializationRegime.RETAIN;
            }

            @Override
            public void calculateBuffer() {
            }
        }

        this.myInputs = new MyInputs(context, channels);
        this.setSideChain(sideChain).setAttack(1).setDecay(.5f).setRatio(2)
                .setThreshold(.5f).setKnee(.5f);
    }

    @Override
    public void calculateBuffer() {
        pf.update();
        float[] bi = bufIn[0];
        float[] bo = bufOut[0];
        float[] dm = delayMem[0];
        for (int i = 0; i < bufferSize; i++) {
            float p = pf.getValue(0, i);
            if (p <= tok) {
                target = 1;
            } else if (p >= kt) {
                target = ((p - threshold) * ratio + threshold) / p;
            } else {
                float x1 = (p - tok) * ikp1 + tok;
                target = ((ktrm1 * x1 + tt1mr) * (p - x1) / (x1 * (knee - 1)) + x1) / p;
            }
            if (currval < target) {
                currval *= downstep;
                if (currval < target) {
                    currval = target;
                }
            } else if (currval > target) {
                currval *= upstep;
                if (currval > target) {
                    currval = target;
                }
            }
            dm[index] = bi[i];
            bo[i] = dm[(index + delaySamps) % memSize] * currval;
            index = (index + 1) % memSize;
        }
    }

    private void calcVals() {
        tok = threshold / knee;
        kt = knee * threshold;
        ikp1 = 1 / (knee + 1);
        ktrm1 = knee * ratio - 1;
        tt1mr = threshold * (1 - ratio);
    }

    private Gate setSideChain(UGen sideChain) {
        pf = (new BiquadFilter(context, 1, BiquadFilter.BUTTERWORTH_LP))
                .setFrequency(31);
        if (sideChain == null) {
            powerUGen = new RMS(context, channels, rmsMemorySize);
            powerUGen.addInput(myInputs);
            pf.addInput(powerUGen);
        } else {
            powerUGen = new RMS(context, sideChain.getOuts(), rmsMemorySize);
            powerUGen.addInput(sideChain);
            pf.addInput(powerUGen);
        }
        return this;
    }

    private Gate setAttack(float attack) {
        if (attack < .0001f) {
            attack = .0001f;
        }
        this.attack = attack;
        this.downstep = (float) Math.pow(Math.pow(10,attack/20f), -1000f/this.context.getSampleRate());
        return this;
    }

    private Gate setDecay(float decay) {
        if (decay < .0001f) {
            decay = .0001f;
        }
        this.decay = decay;
        this.upstep = (float) Math.pow(Math.pow(10, decay/20f), 1000f/this.context.getSampleRate());
        return this;
    }

    private Gate setRatio(float ratio) {
        if (ratio <= 0)
            ratio = .01f;
        this.ratio = 1 / ratio;
        calcVals();
        return this;
    }

    private Gate setThreshold(float threshold) {
        this.threshold = threshold;
        calcVals();
        return this;
    }

    private Gate setKnee(float knee) {
        this.knee = knee + 1;
        calcVals();
        return this;
    }

}

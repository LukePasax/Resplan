package daw.core.audioprocessing;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import daw.utilities.AudioContextManager;
import net.beadsproject.beads.core.AudioContext;
import net.beadsproject.beads.core.UGen;
import net.beadsproject.beads.ugens.BiquadFilter;
import java.util.Map;

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
    private float currval = 1, target = 1, delay;
    private int delaySamps;
    private UGen myInputs;
    private float[][] myBufIn;

    /**
     * Constructs a gate and sets its parameters to the current default.
     * @param channels the number of inputs and outputs of this effect.
     */
    @JsonCreator
    public Gate(@JsonProperty("ins") int channels) {
        this(AudioContextManager.getAudioContext(), channels);
    }

    private Gate(AudioContext context, int channels) {
        super(channels);
        this.channels = channels;
        this.delay = 0;
        this.delaySamps = 0;
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
        this.setParameters(Map.of("attack", 1.0f, "decay", 0.5f, "ratio", 2.0f,
                "threshold", 0.5f, "knee", 0.5f));
        this.calcVals();
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

}

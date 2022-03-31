package daw.general;

/**
 * TO DO: Since there are 4 possible constructors (one without parameters, two with only one parameter
 * and one with two parameters, all of which are Integers), a VolumeFactory may be useful.
 */
public class BasicVolume implements Volume {

    private static final int DEFAULT_MAX_VOL = 100;
    private static final int DEFAULT_INITIAL_VOL = 20;
    private int volume;
    private int max;

    public BasicVolume() {
        this.volume = DEFAULT_INITIAL_VOL;
        this.max = DEFAULT_MAX_VOL;
    }

    public BasicVolume(final int vol, final int max) {
        this.volume = vol;
        this.max = max;
    }

    @Override
    public void setVolume(final int vol) {
        if (vol>=0 && vol<=this.max) {
            this.volume = vol;
        }
    }

    @Override
    public void increment() {
        this.setVolume(this.volume+1);
    }

    @Override
    public void decrement() {
        this.setVolume(this.volume-1);
    }

    @Override
    public int getVolume() {
        return this.volume;
    }

}

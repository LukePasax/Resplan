package daw.general;

public class BasicVolume implements Volume {

    private static final int MAX_VOL = 100;
    private static final int DEFAULT_VOL = 20;
    private int volume;

    public BasicVolume() {
        this.volume = DEFAULT_VOL;
    }

    @Override
    public void setVolume(int vol) {
        if (vol<0 || vol>MAX_VOL) {
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

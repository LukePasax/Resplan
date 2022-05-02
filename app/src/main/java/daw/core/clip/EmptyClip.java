package daw.core.clip;

import java.io.File;

public class EmptyClip implements RPClip {

	private double duration;
	
	public EmptyClip() {
		this.duration = RPClip.DEFAULT_DURATION;
	}
	
	public EmptyClip(double duration) {
		if(duration <= 0) {
			throw new IllegalArgumentException("The duration of a clip must be a positive value");
		}
		this.duration = duration;
	}
	
	@Override
	public void setContentPosition(double milliseconds) {
		throw new UnsupportedOperationException("Can't set Content Position in an Empty Clip. Conver the clip into one with content then retry.");
	}

	@Override
	public double getContentPosition() {
		throw new UnsupportedOperationException("Can't get the Content Position. This is an Empty Clip. Conver the clip into one with content then retry.");
	}

	@Override
	public File getContent() {
		throw new UnsupportedOperationException("Can't get the Content of an Empty Clip. Conver the clip into one with content then retry.");
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public void setDuration(double milliseconds) {
		if(duration <= 0) {
			throw new IllegalArgumentException("The duration of a clip must be a positive value");
		}
		this.duration = milliseconds;
	}

	@Override
	public double getDuration() {
		return this.duration;
	}

	@Override
	public RPClip duplicate() {
		return new EmptyClip(this.duration);
	}

}

package daw.core.clip;

import java.io.File;

public class EmptyClip implements RPClip {

	private double duration = 0;
	
	@Override
	public void setContentPosition(double milliseconds) {
		throw new IllegalStateException("Can't set Content Position in an Empty Clip. Fill the clip with a Content then retry.");
	}

	@Override
	public double getContentPosition() {
		throw new IllegalStateException("Can't get the Content Position. This is an Empty Clip. Fill the clip with a Content then retry.");
	}

	@Override
	public File getContent() {
		throw new IllegalStateException("Can't get the Content of an Empty Clip. Fill the clip with a Content then retry.");
	}

	@Override
	public boolean isEmpty() {
		return true;
	}

	@Override
	public void setDuration(double milliseconds) {
		this.duration = milliseconds;
	}

	@Override
	public double getDuration() {
		return this.duration;
	}

}

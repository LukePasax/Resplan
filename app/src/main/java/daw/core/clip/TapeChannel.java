package daw.core.clip;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Optional;
import javafx.util.Pair;

public class TapeChannel implements RPTapeChannel {
	
	private final Map<Double, RPClip> timeline;
	
	public TapeChannel() {
		this.timeline = new HashMap<>();
	}

	@Override
	public void insertRPClip(RPClip clip, double time) {
		if(this.timeline.containsKey(time)) {
			throw new IllegalArgumentException("Another clip is already present in the timeline at the given time. Choose another time or remove the other clip.");
		}
		this.clearBetween(time, this.getClipTimeOut(new Pair<>(time, clip))); 
		this.timeline.put(time, clip);
	}	
	
	@Override
	public void removeRPClip(double clipTimeIn) {
		var removed = this.timeline.remove(clipTimeIn);
		if(removed == null) {
			throw new IllegalArgumentException("No clip found at the specified clipTimeIn");
		}
	}

	@Override
	public Optional<Pair<Double, RPClip>> getClipAt(double time) {
		return this.timeline.entrySet().stream().filter(x->{
			return (x.getKey()<=time) && (this.getClipTimeOut(new Pair<>(x.getKey(), x.getValue()))>time);
		}).map(x->(new Pair<>(x.getKey(), x.getValue()))).findFirst();
	}

	@Override
	public Iterator<Pair<Double, RPClip>> getClipWithTimeIterator() {
		return this.timeline.entrySet().stream()
				.sorted((x1, x2)->Double.compare(x1.getKey(), x2.getKey()))
				.map(x->(new Pair<>(x.getKey(), x.getValue())))
				.iterator();
	}

	@Override
	public void move(double initialClipTimeIn, double finalClipTimeIn) {
		this.removeRPClip(initialClipTimeIn);
		this.insertRPClip(this.timeline.get(initialClipTimeIn), finalClipTimeIn);
	}

	@Override
	public void setTimeOut(double initialClipTimeIn, double finalClipTimeOut) {
		double newDuration = finalClipTimeOut - initialClipTimeIn;
		this.timeline.get(initialClipTimeIn).setDuration(newDuration);
	}

	@Override
	public void setTimeIn(double initialClipTimeIn, double finalClipTimeIn) {
		RPClip clip = this.timeline.get(initialClipTimeIn);
		double newDuration = this.getClipTimeOut(new Pair<>(initialClipTimeIn, clip)) - finalClipTimeIn;
		this.removeRPClip(initialClipTimeIn);
		clip.setContentPosition(clip.getContentPosition()+(clip.getDuration()-newDuration));
		clip.setDuration(newDuration);
		this.insertRPClip(clip, finalClipTimeIn);
	}

	@Override
	public void split(double initialClipTimeIn, double splittingTime) {
		RPClip clip = this.timeline.get(initialClipTimeIn);
		if(splittingTime >= this.getClipTimeOut(new Pair<>(initialClipTimeIn, clip)) || splittingTime<= initialClipTimeIn) {
			throw new IllegalArgumentException("splittingTime must be within the clip in/out");
		}
		RPClip duplicate = clip.duplicate();
		this.setTimeIn(initialClipTimeIn, splittingTime);
		duplicate.setDuration(splittingTime-initialClipTimeIn);
		this.insertRPClip(duplicate, initialClipTimeIn);
	}	
	
	private double getClipTimeOut(Pair<Double, RPClip> clip) {
		return clip.getKey()+clip.getValue().getDuration();
	}
	
	private void clearBetween(double initialTime, double finalTime) {
		this.getIteratorOfClipBetween(initialTime, finalTime).forEachRemaining(x->{
			if(x.getKey()<initialTime) {
				if(this.getClipTimeOut(new Pair<>(x.getKey(),x.getValue()))<=finalTime) {
					//case (in<initialTime and out<=finalTime) A
					this.setTimeOut(x.getKey(), initialTime);
				} else {
					//case (in<initialTime and out>finalTime) D
					this.split(x.getKey(), finalTime);
					this.setTimeOut(this.getClipAt(initialTime).get().getKey(), initialTime);
				}
			} else {
				if(this.getClipTimeOut(new Pair<>(x.getKey(),x.getValue()))<=finalTime) {
					//case (in>=initialTime and out<=finalTime) B
					this.removeRPClip(x.getKey());
				} else {
					//case (in>=initialTime and out>finalTime) C
					this.setTimeIn(x.getKey(), finalTime);
				}
			}
		});
		
	}

	private Iterator<Pair<Double, RPClip>> getIteratorOfClipBetween(double initialTime, double finalTime) {
		return this.timeline.entrySet().stream().filter(x->{
			return x.getKey()<finalTime && this.getClipTimeOut(new Pair<>(x.getKey(), x.getValue()))>initialTime;
		}).sorted((x1, x2)->Double.compare(x1.getKey(), x2.getKey()))
				.map(x->(new Pair<>(x.getKey(), x.getValue())))
				.iterator();
	}


}

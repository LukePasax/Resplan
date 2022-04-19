package daw.core.clip;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;
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
		if(!this.getClipAt(time).equals(Optional.empty())) {
			this.setTimeOut(getClipAt(time).get().getKey(), time);
			//TODO controlla che non ci siano altre clip dopo l'inizio... Simile al move...
		} 
		this.timeline.put(time, clip);
	}

	@Override
	public Optional<Pair<Double, RPClip>> getClipAt(double time) {
		return this.timeline.entrySet().stream().filter(x->{
			return (x.getKey()<=time) && (this.getTimeOut(x)>time);
		}).map(x->(new Pair<>(x.getKey(), x.getValue()))).findFirst();
	}

	@Override
	public Iterator<Pair<Double, RPClip>> getClipWithTimeIterator() {
		return this.timeline.entrySet().stream().sorted((x1, x2)->Double.compare(x1.getKey(), x2.getKey())).map(x->(new Pair<>(x.getKey(), x.getValue()))).iterator();
	}

	@Override
	public void move(double initialClipTimeIn, double finalClipTimeIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void setTimeOut(double initialClipTimeIn, double duration) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void SetTimeIn(double initialClipTimeIn, double finalClipTimeIn) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void split(double initialClipTimeIn, double SplittingTime) {
		// TODO Auto-generated method stub
		
	}	
	
	private double getTimeOut(Entry<Double, RPClip> clip) {
		return clip.getKey()+clip.getValue().getDuration();
	}
	
	private void clearBetween(double initialTime, double finalTime) {
		//TODO
	}

	private Iterator<Pair<Double, RPClip>> getIteratorOfClipBetween(double initialTime, double finalTime) {
		//TODO
		return null;
	}
}

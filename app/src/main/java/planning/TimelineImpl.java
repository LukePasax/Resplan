package planning;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * It's the implementation of a {@link planning.RPTimeline}
 */
public class TimelineImpl implements RPTimeline{
	
	private Map<Double, RPSection> sections = new HashMap<>();

	private boolean isAddValid(double initialTime, RPSection section) {
		for(Double i : this.sections.keySet()) {
			if(i.equals(initialTime)) {
				return false;
			}
		}
		for(RPSection s : this.sections.values()) {
			if(s.equals(section)) {
				return false;
			}
		}
		for(Double i : this.sections.keySet()) {
			if(initialTime >= i && initialTime <= i + this.sections.get(i).getDuration()) {
				return false;
			}
		}
		return true;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean addSection(double initialTime, RPSection section) {
		if(this.isAddValid(initialTime, section)) {
			this.sections.put(initialTime, section);
			return true;
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public void removeSection(RPSection section) {
		this.sections.forEach((i, s) -> {
			if(s.equals(section)) {
				this.sections.remove(i);
			}
		});
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<RPSection> getSection(double initialTime) {
		for(Double i : this.sections.keySet()) {
			if(i.equals(initialTime)) {
				return Optional.of(this.sections.get(i));
			}
		}
		return Optional.empty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public double getOverallDuration() {
		double totalDuration = 0.0;
		if(this.sections.size() == 0) {
			return totalDuration;
		}
		double max = this.sections.keySet().stream()
						.max((i1, i2) -> i1.compareTo(i2)).get();
		for(Double i : this.sections.keySet()) {
			if(i.equals(max)) {
				totalDuration = i + this.sections.get(i).getDuration();
			}
		}
		return totalDuration;
	}

	/**
	 * {@inheritDoc}
	 */
	@Override
	public Set<Map.Entry<Double,RPSection>> getAllSections() {
		return this.sections.entrySet();
	}
}

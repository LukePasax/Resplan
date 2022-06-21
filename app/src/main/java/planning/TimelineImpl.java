package planning;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

/**
 * It's the implementation of a {@link planning.RPTimeline}
 */
public final class TimelineImpl implements RPTimeline{

	@JsonProperty
	private final Map<Double, RPSection> sections = new HashMap<>();

	private final boolean isAddValid(double initialTime, RPSection section) {
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
	public final boolean addSection(double initialTime, RPSection section) {
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
	public final void removeSection(RPSection section) {
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
	public final Optional<RPSection> getSection(double initialTime) {
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
	public final double getOverallDuration() {
		double totalDuration = 0.0;
		if(this.sections.size() == 0) {
			return totalDuration;
		}
		double max = this.sections.keySet().stream()
						.max(Double::compareTo).get();
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
	@JsonIgnore
	public final Set<Map.Entry<Double,RPSection>> getAllSections() {
		return this.sections.entrySet();
	}
}

package planning;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

public class TimelineImpl implements RPTimeline{
	
	private Map<Integer, RPSection> sections = new HashMap<>();

	private boolean isAddValid(int initialTime, RPSection section) {
		for(Integer i : this.sections.keySet()) {
			if(i.equals(initialTime)) {
				return false;
			}
		}
		for(RPSection s : this.sections.values()) {
			if(s.equals(section)) {
				return false;
			}
		}
		for(Integer i : this.sections.keySet()) {
			if(initialTime >= i && initialTime <= i + this.sections.get(i).getDuration()) {
				return false;
			}
		}
		return true;
	}
	
	@Override
	public boolean addSection(int initialTime, RPSection section) {
		if(this.isAddValid(initialTime, section)) {
			this.sections.put(initialTime, section);
			return true;
		}
		return false;
	}

	@Override
	public void removeSection(RPSection section) {
		this.sections.forEach((i, s) -> {
			if(s.equals(section)) {
				this.sections.remove(i);
			}
		});
	}

	@Override
	public Optional<RPSection> getSection(int initialTime) {
		for(Integer i : this.sections.keySet()) {
			if(i.equals(initialTime)) {
				return Optional.of(this.sections.get(i));
			}
		}
		return Optional.empty();
	}

	@Override
	public int getOverallDuration() {
		int totalDuration = 0;
		if(this.sections.size() == 0) {
			return totalDuration;
		}
		int max = this.sections.keySet().stream()
						.max((i1, i2) -> i1 - i2).get();
		for(Integer i : this.sections.keySet()) {
			if(i.equals(max)) {
				totalDuration = i + this.sections.get(i).getDuration();
			}
		}
		return totalDuration;
	}
}

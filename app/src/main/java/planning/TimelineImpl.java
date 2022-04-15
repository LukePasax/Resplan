package planning;

import java.util.HashMap;
import java.util.Map;

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
		return true;
	}
	
	@Override
	public void addSection(int initialTime, RPSection section) {
		if(this.isAddValid(initialTime, section)) {
			this.sections.put(initialTime, section);
		}
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
	public RPSection getSection(int initialTime) {
		return this.sections.get(initialTime);
	}

	@Override
	public int getOverallDuration() {
		int totalDuration = 0;
		int max = this.sections.keySet().stream()
						.max((i1, i2) -> i2 - i1).get();
		for(Integer i : this.sections.keySet()) {
			if(i.equals(max)) {
				totalDuration = i + this.sections.get(i).getDuration();
			}
		}
		return totalDuration;
	}
}

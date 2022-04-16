package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

public class SimpleSpeakerRubric implements SpeakerRubric {
	
	private List<Speaker> rubric = new ArrayList<>();

	public SimpleSpeakerRubric() {
		// TODO Auto-generated constructor stub
	}

	@Override
	public List<Speaker> getSpeakers() {
		return this.rubric;
	}

	@Override
	public boolean addSpeaker(Speaker speaker) {
		for(Speaker s : this.rubric) {
			if(speaker.equals(s)) {
				return false;
			}
		}
		this.rubric.add(speaker);
		return true;
	}

	@Override
	public boolean removeSpeaker(Speaker speaker) {
		for(Speaker s : this.rubric) {
			if(speaker.equals(s)) {
				return this.rubric.remove(speaker);
			}
		}
		return false;
	}

	@Override
	public Optional<Speaker> searchSpeaker(int speakerCode) {
		for(Speaker s : this.rubric) {
			if(speakerCode == s.getSpeakerCode()) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}

	@Override
	public List<Speaker> getFilteredSpeakers(Predicate<? super Speaker> filter) {
		List<Speaker> filteredRubric = new ArrayList<>();
		this.rubric.stream()
			.filter(filter)
			.forEach(filteredRubric::add);
		return filteredRubric;
	}

}

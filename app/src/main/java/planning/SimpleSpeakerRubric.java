package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.Predicate;

/**
 * This is the implementation of a {@link planning.SpeakerRubric}
 */
public class SimpleSpeakerRubric implements SpeakerRubric {
	
	private List<Speaker> rubric = new ArrayList<>();
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Speaker> getSpeakers() {
		return this.rubric;
	}
	
	/**
	 * {@inheritDoc}
	 */
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
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public boolean removeSpeaker(Speaker speaker) {
		for(Speaker s : this.rubric) {
			if(speaker.equals(s) && speaker.getFirstName().equals(s.getFirstName()) && speaker.getLastName().equals(s.getLastName())) {
				this.rubric.remove(speaker);
				return true;
			}
		}
		return false;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public Optional<Speaker> searchSpeaker(int speakerCode) {
		for(Speaker s : this.rubric) {
			if(speakerCode == s.getSpeakerCode()) {
				return Optional.of(s);
			}
		}
		return Optional.empty();
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public List<Speaker> getFilteredSpeakers(Predicate<? super Speaker> filter) {
		List<Speaker> filteredRubric = new ArrayList<>();
		this.rubric.stream()
			.filter(filter)
			.forEach(filteredRubric::add);
		return filteredRubric;
	}

}

package planning;

/**
 * It's an interface modelling a section, that is a named part of the timeline
 */
public interface RPSection extends Element {
	
	/**
	 * Returns the duration of the section
	 * 
	 * @return a double representing the duration of the section (expressed in ms)
	 */
	double getDuration();
}

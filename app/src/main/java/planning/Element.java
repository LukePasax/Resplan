package planning;

/**
 * It's an interface that represents a generic element of high level DAW
 */
public interface Element {
	public String getTitle();
	public String getDescription();
	public void setTitle(final String title);
	public void setDescription(final String description);
}

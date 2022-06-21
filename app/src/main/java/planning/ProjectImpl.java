package planning;

import java.util.Optional;

/**
 * It's the implementation of a {@link planning.RPProject}
 */
public final class ProjectImpl implements RPProject {
	
	private String title;
	private Optional<String> description = Optional.empty();
	private ProjectType type;
	
	/**
	 * It is used to build a new Object of type RPProject, with a description
	 * 
	 * @param title
	 * the title of the project
	 * 
	 * @param description
	 * the description of the project
	 * 
	 * @param type
	 * the type of the project
	 */
	public ProjectImpl(final String title, final String description, final ProjectType type) {
		this.title = title;
		this.description = Optional.of(description);
		this.type = type;
	}
	
	/**
	 * It is used to build a new Object of type RPProject, without a description
	 * 
	 * @param title
	 * the title of the project
	 * 
	 * @param type
	 * the type of the project
	 */
	public ProjectImpl(final String title, final ProjectType type) {
		this.title = title;
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getTitle() {
		return this.title;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final void addDescription(final String description) {
		this.description = Optional.of(description);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final Optional<String> getDescription() {
		return this.description;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final ProjectType getType() {
		return this.type;
	}
}

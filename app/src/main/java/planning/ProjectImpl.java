package planning;

/**
 * It's the implementation of a {@link planning.RPProject}
 */
public class ProjectImpl implements RPProject {
	
	private String title;
	private String descritpion;
	private ProjectType type;
	
	/**
	 * It is used to build a new Object of type RPProject
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
		this.descritpion = description;
		this.type = type;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getTitle() {
		return this.title;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getDescription() {
		return this.descritpion;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public ProjectType getType() {
		return this.type;
	}
}

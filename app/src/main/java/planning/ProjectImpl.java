package planning;

public class ProjectImpl implements RPProject {
	
	private String title;
	private String descritpion;
	private ProjectType type;

	public ProjectImpl(final String title, final String description, final ProjectType type) {
		this.title = title;
		this.descritpion = description;
		this.type = type;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getDescription() {
		return this.descritpion;
	}

	@Override
	public ProjectType getType() {
		return this.type;
	}

}

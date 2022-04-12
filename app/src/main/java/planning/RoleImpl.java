package planning;

public abstract class RoleImpl implements RPRole {
	
	private String title;
	private String description;
	private RoleType type;
	
	public RoleImpl(final String title, final String description, final RoleType type) {
		this.title = title;
		this.description = description;
		this.type = type;
	}

	@Override
	public String getTitle() {
		return this.title;
	}

	@Override
	public String getDescription() {
		return this.description;
	}

	@Override
	public RoleType getType() {
		return this.type;
	}

}

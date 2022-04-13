package planning;

import java.util.ArrayList;
import java.util.List;

public abstract class RoleImpl implements RPRole {
	
	private String title;
	private String description;
	private RoleType type;
	private final List<String> notesList = new ArrayList<>();
	
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
	
	@Override
	public void addNote(String note) {
		this.notesList.add(note);
	}

	@Override
	public List<String> getNotes() {
		return this.notesList;
	}

}

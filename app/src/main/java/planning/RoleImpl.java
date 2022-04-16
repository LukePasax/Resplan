package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

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
	
	@Override
	public abstract void addSpeaker(Speaker speaker);
	
	@Override
	public abstract Optional<Speaker> getSpeaker();

	@Override
	public int hashCode() {
		return Objects.hash(title, type);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		RoleImpl other = (RoleImpl) obj;
		return Objects.equals(title, other.title) && type == other.type;
	}

}

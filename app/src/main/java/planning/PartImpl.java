package planning;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public abstract class PartImpl implements RPPart {
	
	private String title;
	private String description;
	private PartType type;
	private List<String> notes = new ArrayList<>();
	
	public PartImpl(final String title, final String description, final PartType type) {
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
	public void addNote(String note) {
		this.notes.add(note);

	}

	@Override
	public List<String> getNotes() {
		return this.notes;
	}
	
	@Override
	public PartType getType() {
		return this.type;
	}

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
		PartImpl other = (PartImpl) obj;
		return Objects.equals(title, other.title) && type == other.type;
	}
}

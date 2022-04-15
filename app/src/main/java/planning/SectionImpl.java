package planning;

import java.util.Objects;

public class SectionImpl implements RPSection {
	
	private String title;
	private String description;

	public SectionImpl(final String title, final String description) {
		this.title = title;
		this.description = description;
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
	public int hashCode() {
		return Objects.hash(title);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SectionImpl other = (SectionImpl) obj;
		return Objects.equals(title, other.title);
	}
}

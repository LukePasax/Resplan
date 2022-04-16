package planning;

import java.util.Objects;

public class SimpleSpeaker implements Speaker {
	
	private int speakerCode;
	private String firstName;
	private String lastName;

	public SimpleSpeaker(final int speakerCode, final String firstName, final String lastName) {
		this.speakerCode = speakerCode;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	public static class Builder {
		private Integer speakerCode;
		private String firstName;
		private String lastName;
		
		public Builder(final int speakerCode) {
			this.speakerCode = speakerCode;
		}
		
		public Builder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		public Builder lastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		public Speaker build() throws IllegalStateException {
			if(this.speakerCode == null || this.firstName == null || this.lastName == null) {
				throw new IllegalStateException();
			}
			return new SimpleSpeaker(this.speakerCode, this.firstName, this.lastName);
		}
	}

	@Override
	public int getSpeakerCode() {
		return this.speakerCode;
	}

	@Override
	public String getFirstName() {
		return this.firstName;
	}

	@Override
	public String getLastName() {
		return this.lastName;
	}

	@Override
	public int hashCode() {
		return Objects.hash(speakerCode);
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		SimpleSpeaker other = (SimpleSpeaker) obj;
		return speakerCode == other.speakerCode;
	}
}

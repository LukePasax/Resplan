package planning;

import java.util.Objects;

/**
 * This is a simple implementation of a {@link planning.Speaker}
 */
public class SimpleSpeaker implements Speaker {
	
	private int speakerCode;
	private String firstName;
	private String lastName;
	
	/**
	 * Builds a speaker 
	 * 
	 * @param speakerCode
	 * the code associated to the speaker
	 * 
	 * @param firstName
	 * the first name of the speaker
	 * 
	 * @param lastName
	 * the last name of the speaker
	 */
	public SimpleSpeaker(final int speakerCode, final String firstName, final String lastName) {
		this.speakerCode = speakerCode;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * This is the realization of the pattern builder
	 * for a {@link planning.SimpleSpeaker}
	 */
	public static class Builder {
		private Integer speakerCode;
		private String firstName;
		private String lastName;
		
		/**
		 * This is the first part of the builder
		 * 
		 * @param speakerCode
		 * the code associated to the speaker
		 */
		public Builder(final int speakerCode) {
			this.speakerCode = speakerCode;
		}
		
		/**
		 * Returns the builder until the first name
		 * 
		 * @param firstName
		 * the first name of the speaker
		 * 
		 * @return the current builder
		 */
		public Builder firstName(final String firstName) {
			this.firstName = firstName;
			return this;
		}
		
		/**
		 * Returns the builder until the last name
		 * 
		 * @param lastName
		 * the last name of the speaker
		 * 
		 * @return the current builder
		 */
		public Builder lastName(final String lastName) {
			this.lastName = lastName;
			return this;
		}
		
		/**
		 * Builds the speaker
		 * 
		 * @return the speaker generated
		 * 
		 * @throws IllegalStateException if the code, the first
		 * name or the last name have not been entered
		 */
		public Speaker build() throws IllegalStateException {
			if(this.speakerCode == null || this.firstName == null || this.lastName == null) {
				throw new IllegalStateException();
			}
			return new SimpleSpeaker(this.speakerCode, this.firstName, this.lastName);
		}
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int getSpeakerCode() {
		return this.speakerCode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public String getLastName() {
		return this.lastName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public int hashCode() {
		return Objects.hash(speakerCode);
	}
	
	/**
	 * {@inheritDoc}
	 */
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

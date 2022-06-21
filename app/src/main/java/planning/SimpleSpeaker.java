package planning;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

import java.util.Objects;

/**
 * This is a simple implementation of a {@link planning.Speaker}
 */
public final class SimpleSpeaker implements Speaker {
	
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
	@JsonCreator
	public SimpleSpeaker(@JsonProperty("speakerCode") final int speakerCode,
						 @JsonProperty("firstName") final String firstName,
						 @JsonProperty("lastName") final String lastName) {
		this.speakerCode = speakerCode;
		this.firstName = firstName;
		this.lastName = lastName;
	}
	
	/**
	 * This is the realization of the pattern builder
	 * for a {@link planning.SimpleSpeaker}
	 */
	public final static class Builder {
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
		public final Builder firstName(final String firstName) {
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
		public final Builder lastName(final String lastName) {
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
		public final Speaker build() throws IllegalStateException {
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
	public final int getSpeakerCode() {
		return this.speakerCode;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getFirstName() {
		return this.firstName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final String getLastName() {
		return this.lastName;
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final int hashCode() {
		return Objects.hash(speakerCode);
	}
	
	/**
	 * {@inheritDoc}
	 */
	@Override
	public final boolean equals(Object obj) {
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

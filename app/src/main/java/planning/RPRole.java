package planning;

/**
 * It's an interface that represents a guest or a soundtrack in high level
 */
public interface RPRole extends Element {
	
	public enum RoleType{
		SPEECH, SOUNDTRACK, EFFECTS
	}
	
	RoleType getType();
}

package daw.engine;

import daw.core.clip.RPClipPlayer;

/**
 * Read the current step from {@link RPClock} and play all the {@link RPClipPlayer}s
 * subscribed at that step.
 * When the {@link RPEngine} is stopped, stop all the subscribed players.
 */
public interface RPClipPlayerNotifier {
	
	/**
	 * Notify all the players associated with the given step to play or stop.
	 * Then remove them from the observers.
	 * 
	 * @param  step  The actual clock step.
	 */
	void update(Long step);
	
	/**
	 * Stop all the subscribed players.
	 */
	void notifyStopped();
	
	/**
	 * Subscribe a player to this clip player notifier.
	 *
	 * @param  step  The step to subscribe the player at.
	 * 
	 * @param  clipPlayer  The clip player to subscribe.
	 * 
	 * @return  {@code true} If and only if the player was successfully
	 * 							added to the observer list
	 * 			{@code false} otherwise.
	 */
	boolean addObserver(Long step, RPClipPlayer clipPlayer);
	
	/**
	 * Unsubscribe a player to this clip player notifier.
	 *
	 * @param  step  The step where to find the player to unsubscribe.
	 * 
	 * @param  clipPlayer  The clip player to unsubscribe.
	 * 
	 * @return  {@code true} If and only if the player was successfully
	 * 							removed to the observer list
	 * 			{@code false} otherwise.
	 */
	boolean removeObserver(Long step, RPClipPlayer clipPlayer);

}

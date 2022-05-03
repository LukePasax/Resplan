package daw.core.clip;

import net.beadsproject.beads.core.UGen;

/**
 * A player for an RPClip which read and plays back it's content.
 * <p>A player could handle only some type of RPClips and some 
 * RPClips could not have any player which support them (for example an {@link EmptyClip}, 
 * which has no content, could not be played).
 * <p>A cut point could be setted on any player. When a cut point is active the player will start the playback to it's cut position.
 * <p>Any player wraps an {@link UGen} so it must supply the wrapped {@link UGen} for connecting it's output to some audio channel.
 */
public interface RPClipPlayer {
	
	/**
	 * Start the playback.
	 */
	void play();
	
	/**
	 * Stop the playback.
	 */
	void pause();
	
	/**
	 * Stop the playback and reset the playback position to 0 or to {@code this.getCutTime()} if {@code this.isCutActive}.
	 */
	void stop();
	
	/**
	 * Set the playback position.
	 * <p>The time position is referred to the clip lenght so if {@code clip.getContentPosition()} is not zero, 
	 * after a {@code this.setPlaybackPosition(0.0)} call the playback position of the clip content will be setted 
	 * to {@code clip.getContentPosition()}, which is equals to the zero position of the clip.
	 * 
	 * @param  milliseconds  The playback position in milliseconds.
	 * 
	 * @throws  IllegalArgumentException  If the given time is a negative value or if is equals or bigger than the clip duration.
	 */
	void setPlaybackPosition(double milliseconds);
	
	/**
	 * Set a cut time for this player.
	 * <p>The zero time position is referred to the {@code clip.getContentPosition()} point, which is the starting point of the clip and not the content zero position.
	 * 
	 * @param  time  The cut position in milliseconds.
	 * 
	 * @throws  IllegalArgumentException  If the given time is equal to zero, a negative value or if is bigger than the clip duration.
	 */
	void setCut(double time);
	
	/**
	 * Disable the cut for this player.
	 */
	void disableCut();
	
	/**
	 * Get this playback position.
	 * <p>The time position is referred to the clip lenght so if {@code this.getPlaybackPosition()} is zero, 
	 * the playback position of the clip content could be a different value.
	 * 
	 * @return  The playback position in milliseconds.
	 */
	double getPlaybackPosition();
	
	/**
	 * Check if this player is paused.
	 * 
	 * @return  {@code true} if and only if this player is not playing back audio
	 * 			{@code false} otherwise.
	 */
	boolean isPaused();
	
	/**
	 * Check if this player has an active cut.
	 * 
	 * @return  {@code true} if and only if this player has an active cut
	 * 			{@code false} otherwise.
	 */
	boolean isCutActive();
	
	/**
	 * Get this cut time referred to the clip lenght if {@code this.isCutActive()}.
	 * 
	 * @return  The current cut time in milliseconds.
	 * 
	 * @throws  IllegalStateException  If this cut is not active.
	 */
	double getCutTime();
	
	/**
	 * Get the UGen for connecting this player to any channel.
	 * 
	 * @return  The wrapped UGen.
	 */
	UGen getUGen();
	
	
}

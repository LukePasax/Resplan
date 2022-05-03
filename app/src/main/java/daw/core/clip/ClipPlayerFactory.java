package daw.core.clip;

import daw.core.channel.RPChannel;

/**
 *	A Factory for {@link RPClipPlayer}
 *	<p>Creates a player and connect it with the given {@link RPChannel}.
 *	Could also create a player with an active cut already setted.
 *
 * @param <X>  The type of {@link RPClip} to be played from the created player.
 */
public interface ClipPlayerFactory {
	
	/**
	 * Creates a {@link RPClipPlayer} and connects it to the given {@link RPChannel}.
	 * 
	 * @param  clip  The clip to be played from the player.
	 * 
	 * @param  channel  The channel to connect the player to.
	 * 
	 * @return  The created player.
	 */
	RPClipPlayer createClipPlayer(RPClip<?> clip, RPChannel channel);
	
	/**
	 * Creates a {@link RPClipPlayer} with an active cut and connects it to the given {@link RPChannel}.
	 * 
	 * @param  clip  The clip to be played from the player.
	 * 
	 * @param  channel  The channel to connect the player to.
	 * 
	 * @param  cut  The time where to set the cut.
	 * 
	 * @return The created player.
	 */
	RPClipPlayer createClipPlayerWithActiveCut(RPClip<?> clip, RPChannel channel, double cut);

}

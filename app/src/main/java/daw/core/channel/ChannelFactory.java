package daw.core.channel;

import net.beadsproject.beads.core.UGen;
import daw.core.audioprocessing.ProcessingUnit;
import daw.core.audioprocessing.RPEffect;
import daw.core.audioprocessing.Gate;
import daw.core.audioprocessing.Sidechaining;

/**
 * This factory allows the client to acquire a particular form of {@link RPChannel}.
 * The channels are distinguished here only by their {@link ProcessingUnit}.
 * Still, every form of built-in channel can be later modulated by adding {@link RPEffect}s
 * that serve as effects for sound processing.
 */
public interface ChannelFactory {

    /**
     * Creates an {@link RPChannel} that does not possess a {@link ProcessingUnit}.
     * This is channel represents the most modular choice when creating a channel,
     * as no built-in structure is provided.
     * @return the {@link RPChannel}.
     */
    RPChannel basic();

    /**
     * Creates a gated {@link RPChannel}. To know what a gate is and when to use it,
     * please read the documentation at {@link Gate}.
     * @return the {@link RPChannel}.
     */
    RPChannel gated();

    /**
     * Creates an {@link RPChannel} that possesses a sidechain. To know what a sidechain is and how to use it,
     * please read the documentation at {@link Sidechaining}.
     * @param u the audio source that this channel must be sidechained to.
     * @return the {@link RPChannel}.
     */
    RPChannel sidechained(UGen u);

    /**
     * Creates a return channel.
     * Return channels let the client run multiple sounds through the same effect in the mixer.
     * Using return channels can make the sound more cohesive.
     * @return the {@link RPChannel}.
     */
    RPChannel returnChannel();

    /**
     * Creates a master channel. A master channel is the one to which all other {@link RPChannel}s'
     * outputs are sent to.
     * @return the {@link RPChannel}.
     */
    RPChannel masterChannel();

}

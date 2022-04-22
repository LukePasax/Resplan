package daw.core.channel;

import daw.core.audioprocessing.ProcessingUnit;
import net.beadsproject.beads.core.UGen;
import daw.core.audioprocessing.Sidechaining;
import daw.core.audioprocessing.Gate;

/**
 * This factory allows the client to acquire a particular form of {@link RPChannel}.
 * The channels are only distinguished by their {@link ProcessingUnit}.
 * Still, every form of built-in channel can be later modulated by adding {@link UGen}s
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

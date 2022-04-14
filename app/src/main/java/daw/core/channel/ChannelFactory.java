package daw.core.channel;

import net.beadsproject.beads.core.UGen;

/**
 * This factory allows the client to acquire a particular form of {@link RPChannel}.
 * The channels are only distinguished by their {@link ProcessingUnit} (TO DO: their default Volume?).
 * Still, every form of built-in channel can be modulated by adding {@link net.beadsproject.beads.core.UGen}s
 * that serve as effects for sound processing.
 */
public interface ChannelFactory {

    /**
     * Creates a {@link RPChannel} that does not possess a {@link ProcessingUnit}.
     * This is channel represents the most modular choice when creating a channel,
     * as no built-in structure is provided.
     * @return the {@link RPChannel}.
     */
    RPChannel basic();

    /**
     * Creates a gated {@link RPChannel}.
     * @return the {@link RPChannel}.
     */
    RPChannel gated();

    /**
     * Creates a sidechained {@link RPChannel}.
     * @return the {@link RPChannel}.
     */
    RPChannel sidechained(UGen u);

    /**
     * Creates a return channel. A return channel is... TO DO
     * @return the {@link RPChannel}.
     */
    RPChannel returnChannel();

    /**
     * Creates a master channel. A master channel is... TO DO
     * @return the {@link RPChannel}.
     */
    RPChannel masterChannel();

}

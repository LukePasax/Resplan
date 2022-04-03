package daw.core.channel;

/**
 * This factory allows the client to acquire a particular form of {@link RPChannel}.
 * The channels are only distinguished by their {@link ProcessingUnit} (TO DO: their default Volume?).
 * Still, every form of built-in channel can be modulated by adding {@link net.beadsproject.beads.core.UGen}s
 * that serve as effects for sound processing.
 */
public interface ChannelFactory {

    /**
     * Creates a gated {@link RPChannel}.
     * @return the {@link RPChannel}.
     */
    RPChannel gated();

    /**
     * Creates a sidechained {@link RPChannel}.
     * @return the {@link RPChannel}.
     */
    RPChannel sidechained();

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

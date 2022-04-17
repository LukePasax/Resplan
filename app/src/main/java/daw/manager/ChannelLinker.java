package daw.manager;

import daw.core.channel.RPChannel;
import daw.core.clip.RPTapeChannel;
import javafx.util.Pair;
import planning.RPRole;

import java.util.HashMap;
import java.util.Map;

public class ChannelLinker implements RPChannelLinker {

    private static Map<RPRole, Pair<RPChannel, RPTapeChannel>> channelMap;

    ChannelLinker() {
        channelMap = new HashMap<>();
    }

    @Override
    public void addChannelReferences(RPChannel channel, RPTapeChannel tapeChannel, RPRole role) {
        channelMap.put(role, new Pair<>(channel,tapeChannel));
    }

    @Override
    public RPChannel getChannel(RPRole role) {
        return channelMap.get(role).getKey();
    }

    @Override
    public RPTapeChannel getTapeChannel(RPRole role) {
        return channelMap.get(role).getValue();
    }
}

package view.common;

import java.util.Set;

import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public interface ViewData {

	void addChannel(Channel channel);
	
	void removeChannel(Channel channel);
	
	void addClip(Channel channel,  Clip clip);
	
	void removeClip(Channel channel, Clip clip);

	Channel getChannel(String title);

	void addChannelsDataListener(MapChangeListener<Channel, ObservableList<Clip>> listener);

	void addClipsDataListener(Channel channel, ListChangeListener<Clip> listener);

	Set<Channel> getUnmodifiableChannels();

	ObservableList<Clip> getUnmodifiableClips(Channel channel);
	
}

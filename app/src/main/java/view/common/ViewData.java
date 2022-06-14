package view.common;

import java.util.Set;

import javafx.beans.InvalidationListener;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;
import view.common.ViewDataImpl.Section;

public interface ViewData {

	//channels and clips
	void addChannel(Channel channel);
	
	void removeChannel(Channel channel);
	
	void addClip(Channel channel,  Clip clip);
	
	void removeClip(Channel channel, Clip clip);

	Channel getChannel(String title);
	
	Clip getClip(String channelTitle, String clipTitle);

	void addChannelsDataListener(MapChangeListener<Channel, ObservableList<Clip>> listener);

	void addChannelsInvalidateListener(InvalidationListener invalidationListener);
	
	void addClipsDataListener(Channel channel, ListChangeListener<Clip> listener);

	Set<Channel> getUnmodifiableChannels();

	ObservableList<Clip> getUnmodifiableClips(Channel channel);
	
	//markers
	void addSection(Section section);
	
	void removeSection(Section section);
	
	void addSectionDataListener(SetChangeListener<Section> listener);
	
	ObservableSet<Section> getUnmodifiableSections();

}

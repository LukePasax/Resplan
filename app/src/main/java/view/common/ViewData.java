package view.common;

import view.common.ViewDataImpl.Channel;
import view.common.ViewDataImpl.Clip;

public interface ViewData {

	void addChannel(Channel channel);
	
	void removeChannel(Channel channel);
	
	void addClip(Channel channel,  Clip clip);
	
	void removeClip(Channel channel, Clip clip);
	
}

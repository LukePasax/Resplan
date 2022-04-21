package daw.engine;

import daw.manager.ChannelLinker;

public class Engine implements RPEngine {
	
	private ChannelLinker channelLinker;
	
	public Engine(ChannelLinker channelLinker) {
		this.channelLinker = channelLinker;
	}

	@Override
	public void start() {
		this.update();
		// TODO avvia il clock
	}

	@Override
	public void pause() {
		// TODO Auto-generated method stub

	}

	@Override
	public void stop() {
		// TODO Auto-generated method stub
	}
	
	private void update() {
		
		// aggiornati le clip
		// preparati i player e i listener del clock
	}

}

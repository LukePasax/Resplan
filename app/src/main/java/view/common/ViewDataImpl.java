package view.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Set;
import daw.core.clip.RPClip;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.scene.Node;
import planning.RPPart;

public class ViewDataImpl implements ViewData {
	
	private ObservableMap<Channel, ObservableList<Clip>> data = FXCollections.observableHashMap();

	@Override
	public void addChannel(Channel channel) {
		data.put(channel, FXCollections.observableArrayList());
	}

	@Override
	public void removeChannel(Channel channel) {
		data.remove(channel);
	}

	@Override
	public void addClip(Channel channel, Clip clip) {
		if(!data.containsKey(channel)) {
			throw new IllegalArgumentException("channel does not exist");
		}
		data.get(channel).add(clip);
	}

	@Override
	public void removeClip(Channel channel, Clip clip) {
		if(!data.containsKey(channel)) {
			throw new IllegalArgumentException("view channel does not exist");
		}
		if(!data.get(channel).remove(clip)) {
			throw new IllegalArgumentException("view clip does not exist");
		}
		
	}
	
	public Channel getChannel(String title) {
		var ch = data.keySet().stream().filter(x->x.getTitle().equals(title)).findFirst();
		return ch.isEmpty() ? null : ch.get();
	}
	
	public ObservableMap<Channel, ObservableList<Clip>> getChannels() {
		return data;
	}
	
	public ObservableList<Clip> getClips(Channel channel){
		return data.get(channel);
	}
	
	public static class Channel {
		private String title;
		private Set<Node> view = new HashSet<>();
		
		public Channel(String title) {
			this.title = title;
		}
		
		public String getTitle() {
			return title;
		}

		public void addToViewAll(Node... nodes) {
			for(Node n : nodes) {
				view.add(n);
			}
		}
		
		public Set<Node> getViewSet() {
			return Collections.unmodifiableSet(view);
		}
		
		public void clearViewSet() {
			view.clear();
		}
		
		public void removeFromViewAll(Node... nodes) {
			for(Node n : nodes) {
				view.remove(n);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(title);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Channel other = (Channel) obj;
			return Objects.equals(title, other.title);
		}
		
		
	}
	
	public static class Clip {
		
		private String title;
		private Double position;
		private Double duration;
		private Double contentPosition;
		private Set<Node> view = new HashSet<>();
		
		
		public Clip(String title, Double position, Double duration, Double contentPosition) {
			super();
			this.title = title;
			this.position = position;
			this.duration = duration;
			this.contentPosition = contentPosition;
		}

		public Clip(RPClip<?> clip, RPPart part) {
			this.title = part.getTitle();
			this.duration = clip.getDuration();
			this.contentPosition = clip.getContentPosition();
		}
		
		public String getTitle() {
			return title;
		}

		public Double getPosition() {
			return position;
		}
		
		public Double getDuration() {
			return duration;
		}

		public Double getContentPosition() {
			return contentPosition;
		}
		
		public void addToViewAll(Node... nodes) {
			for(Node n : nodes) {
				view.add(n);
			}
		}
		
		public Set<Node> getViewSet() {
			return Collections.unmodifiableSet(view);
		}
		
		public void clearViewSet() {
			view.clear();
		}
		
		public void removeFromViewAll(Node... nodes) {
			for(Node n : nodes) {
				view.remove(n);
			}
		}

		@Override
		public int hashCode() {
			return Objects.hash(title);
		}

		@Override
		public boolean equals(Object obj) {
			if (this == obj)
				return true;
			if (obj == null)
				return false;
			if (getClass() != obj.getClass())
				return false;
			Clip other = (Clip) obj;
			return Objects.equals(title, other.title);
		}
		
	}
}

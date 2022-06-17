package view.common;

import java.util.Collections;
import java.util.HashSet;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;

import Resplan.Starter;
import javafx.beans.property.BooleanProperty;
import javafx.beans.property.DoubleProperty;
import javafx.beans.property.SimpleBooleanProperty;
import javafx.beans.property.SimpleDoubleProperty;
import javafx.collections.FXCollections;
import javafx.collections.ListChangeListener;
import javafx.collections.MapChangeListener;
import javafx.collections.ObservableList;
import javafx.collections.ObservableMap;
import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;
import javafx.scene.Node;
import view.effects.EffectsPane;

public class ViewDataImpl implements ViewData {
	
	private ObservableMap<Channel, ObservableList<Clip>> data = FXCollections.observableHashMap();

	private ObservableSet<Section> sections = FXCollections.observableSet();
	
	private DoubleProperty prLenght = new SimpleDoubleProperty(Starter.getController().getProjectLength());
	
	//prLenght
	@Override
	public void setProjectLenght(Double prLenght) {
		this.prLenght.set(prLenght);
	}
	
	@Override
	public DoubleProperty getProjectLenghtProperty() {
		return this.prLenght;
	}
	
	//channels and clips
	@Override
	public void addChannel(Channel channel) {
		data.put(channel, FXCollections.observableArrayList());
		channel.setFxView(new EffectsPane(channel.getTitle()));
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
	
	@Override
	public void clearChannelClips(Channel channel) {
		data.get(channel).clear();
	}
	
	@Override
	public Channel getChannel(String title) {
		var ch = data.keySet().stream().filter(x->x.getTitle().equals(title)).findFirst();
		return ch.isEmpty() ? null : ch.get();
	}
	
	@Override
	public Clip getClip(String ch, String cl) {
		var channel = getChannel(ch);
		var clip = data.get(channel).stream().filter(x->x.getTitle().equals(cl)).findFirst();
		return  clip.isEmpty() ? null : clip.get();
	}
	
	@Override
	public void addChannelsDataListener(MapChangeListener<Channel, ObservableList<Clip>> mapChangeListener) {
		data.addListener(mapChangeListener);
	}
	
	@Override
	public void addClipsDataListener(Channel channel, ListChangeListener<Clip> listChangeListener) {
		data.get(channel).addListener(listChangeListener);
	}
	
	@Override
	public Set<Channel> getUnmodifiableChannels() {
		return Collections.unmodifiableSet(data.keySet());
	}
	
	@Override
	public ObservableList<Clip> getUnmodifiableClips(Channel channel) {
		return FXCollections.unmodifiableObservableList(data.get(channel));
	}

	//sections
	
		@Override
	public void addSection(Section section) {
		sections.add(section);
	}

	@Override
	public ObservableSet<Section> getUnmodifiableSections() {
		return FXCollections.unmodifiableObservableSet(sections);
	}
	
	@Override
	public void removeSection(Section section) {
		sections.remove(section);
	}

	@Override
	public void addSectionDataListener(SetChangeListener<Section> listener) {
		sections.addListener(listener);
	}

	@Override
	public void clearData() {
		this.data.clear();
		this.sections.clear();
	}
	
	public static class Channel {
		private String title;
		private String group;
		private BooleanProperty muted = new SimpleBooleanProperty(false);
		private BooleanProperty solo = new SimpleBooleanProperty(false);
		private Set<Node> view = new HashSet<>();
		private ObservableList<Effect> effects = FXCollections.observableArrayList();
		private Node fxView;
		
		public Channel(String title, String group) {
			this.title = title;
			this.group = group;
		}
		
		public String getTitle() {
			return title;
		}
		
		public String getGroup() {
			return group;
		}

		public BooleanProperty isMuted() {
			return muted;
		}
		
		public void setMute(boolean muted) {
			this.muted.set(muted);
		}
		
		public BooleanProperty isSolo() {
			return solo;
		}
		
		public void setSolo(boolean solo) {
			this.solo.set(solo);
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

		public Node getFxView() {
			return fxView;
		}
		
		public void setFxView(Node node) {
			this.fxView = node;
		}
		
		public ObservableList<Effect> getFxList() {
			return effects;
		}
		
		public void addFxListListener(ListChangeListener<Effect> listChangeListener) {
			effects.addListener(listChangeListener);
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
		private DoubleProperty position = new SimpleDoubleProperty();
		private DoubleProperty duration = new SimpleDoubleProperty();;
		private Optional<Double> contentPosition;
		private Optional<Double> contentDuration;
		private Optional<String> contentFileName;
		private Set<Node> view = new HashSet<>();
		
		
		public Clip(String title, Double position, Double duration, Optional<Double> contentPosition, Optional<Double> contentDuration, Optional<String> contentFileName) {
			super();
			this.title = title;
			this.position.set(position);
			this.duration.set(duration);
			this.contentFileName = contentFileName;
			this.contentPosition = contentPosition;
			this.contentDuration = contentDuration;
			if(!coerentOptionals()) {
				this.contentFileName = Optional.empty();
				this.contentPosition = Optional.empty();
				this.contentDuration = Optional.empty();
				throw new IllegalArgumentException("If the clip is empty no content position and duration must be specified. Else both content position and duration must be specified.");
			}
		}
		
		private boolean coerentOptionals() {
			boolean check = this.contentDuration.isEmpty();
			return check == this.contentPosition.isEmpty() && check == this.contentFileName.isEmpty(); 
		}
		
		public String getTitle() {
			return title;
		}

		public DoubleProperty getPosition() {
			return position;
		}
		
		public DoubleProperty getDuration() {
			return duration;
		}
		
		public void setPosition(Double position) {
			this.position.set(position);
		}
		
		public void setDuration(Double duration) {
			this.duration.set(duration);
		}

		public boolean isEmpty() {
			return contentPosition.isEmpty();
		}
		
		public Double getContentPosition() {
			return contentPosition.get();
		}
		
		public Double getContentDuration() {
			return contentDuration.get();
		}
		
		public String getContentName() {
			return contentFileName.get();
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

	public static class Section {
		
		private String title;
		private Double position;
		private Set<Node> view = new HashSet<>();

		public Section(String title, Double position) {
			this.title = title;
			this.position = position;
		}
		
		public String getTitle() {
			return title;
		}
		
		public Double getPosition() {
			return position;
		}
		
		public void addToViewAll(Node... nodes) {
			for(Node n : nodes) {
				view.add(n);
			}
		}
		
		public Set<Node> getViewSet() {
			return Collections.unmodifiableSet(view);
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
			Section other = (Section) obj;
			return Objects.equals(title, other.title);
		}
		
	}

	public static class Effect {
		
		private String type;
		
		public Effect(String type) {
			this.type = type;
		}
		
		public String getType() {
			return type;
		}
		
	}
}

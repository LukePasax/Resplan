package view.common;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import javafx.scene.control.Button;
import javafx.scene.layout.Background;
import javafx.scene.layout.BackgroundFill;
import javafx.scene.paint.Color;

public final class ToolBarSetter {
	
	private final Map<Tool, Button> tools = new HashMap<>();
	private Tool currentTool = Tool.CURSOR;
	private final Set<ToolChangeListener> listeners = new HashSet<>();
	
	public ToolBarSetter addTool(final Tool tool, final Button button) {
		this.tools.put(tool, button);
		return this;
	}
	
	public void selectTool(final Tool tool) {
		if (tools.containsKey(tool)) {
			this.currentTool = tool;
			this.tools.forEach((t, b) -> {
				if (t.equals(tool)) {
					b.setBackground(new Background(new BackgroundFill(Color.ORANGE, null, null)));
				} else {
					b.setBackground(new Background(new BackgroundFill(Color.LIGHTGREY, null, null)));
				}
			});
			listeners.forEach(ToolChangeListener::notifyToolChange);
		}
	}
	
	public Tool getCurrentTool() {
		return this.currentTool;
	}
	
	public void addToolChangeListener(final ToolChangeListener listener) {
		listeners.add(listener);
	}
}

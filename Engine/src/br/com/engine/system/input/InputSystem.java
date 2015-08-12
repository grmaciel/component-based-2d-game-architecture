package br.com.engine.system.input;

import java.util.List;

import android.view.MotionEvent;
import android.view.View;
import br.com.engine.component.IComponent;


public class InputSystem {

	public void onTouch(List<IComponent> components, View v, MotionEvent event) {
		for (IComponent comp : components) {
			if (comp.isActive()) {
				comp.onTouchListener(event);
			}
		}
	}

}

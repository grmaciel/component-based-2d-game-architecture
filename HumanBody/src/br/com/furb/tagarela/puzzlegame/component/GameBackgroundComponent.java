package br.com.furb.tagarela.puzzlegame.component;

import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.CanvasHolder;
import br.com.engine.system.render.RenderWrapper;

public class GameBackgroundComponent extends AbstractComponent {
	private int color;
	
	public GameBackgroundComponent(int color) {
		this.color = color;
	}
	
	@Override
	public void onRender(RenderWrapper render) {
		CanvasHolder wrapper = render.getHolder();
		wrapper.canvas.drawColor(color);
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onTouchListener(MotionEvent event) {
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
	}


}

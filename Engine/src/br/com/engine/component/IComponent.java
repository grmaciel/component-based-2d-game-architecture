package br.com.engine.component;

import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.RenderWrapper;
import android.view.MotionEvent;

public interface IComponent {
	public void onUpdate();
	
	public void onTouchListener(MotionEvent event);
	
	public void onRender(RenderWrapper render);
	
	public void receiveMessage(MessageWrapper wrapper);
	
	public boolean isActive();
}

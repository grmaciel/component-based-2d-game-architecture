package br.com.furb.tagarela.puzzlegame.component;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import android.view.View.OnClickListener;
import br.com.engine.component.AbstractComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.util.CollisionUtil;

public class ButtonComponent extends AbstractComponent {
	private GameObject gameObject;
	private OnClickListener clickListener;
	private Bitmap bitmap;
	
	public ButtonComponent(GameObject go, Bitmap bitmap, OnClickListener clickListener) {
		this.gameObject = go;
		this.bitmap = bitmap;
		this.clickListener = clickListener;
	}
	
	@Override
	public void onTouchListener(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			if (CollisionUtil.touchSelected(gameObject, bitmap, event.getX(),
					event.getY())) {
				clickListener.onClick(null);
			}
			break;
		default:
			break;
		}
	}

	@Override
	public void onUpdate() {
	}

	@Override
	public void onRender(RenderWrapper render) {
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
	}
}

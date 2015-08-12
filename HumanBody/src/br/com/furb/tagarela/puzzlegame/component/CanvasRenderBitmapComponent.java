package br.com.furb.tagarela.puzzlegame.component;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.CanvasHolder;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.enumeration.EnumMessageId;
import br.com.furb.tagarela.puzzlegame.messages.MessageBitmapAlpha;

public class CanvasRenderBitmapComponent extends AbstractComponent {
	private Bitmap bitmap;
	private int alpha = 255;

	public CanvasRenderBitmapComponent(GameObject gameObject, Bitmap bitmap) {
		this.gameObject = gameObject;
		this.bitmap = bitmap;
	}

	public Bitmap getBitmap() {
		return bitmap;
	}

	public void setBitmap(Bitmap bitmap) {
		this.bitmap = bitmap;
	}

	@Override
	public void onUpdate() {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTouchListener(MotionEvent event) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRender(RenderWrapper render) {
		CanvasHolder holder = render.getHolder();
		holder.paint.setAlpha(alpha);
		holder.canvas.drawBitmap(this.getBitmap(), gameObject.getX(), 
				gameObject.getY(), holder.paint);
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
		if (wrapper.getId() == EnumMessageId.CHANGE_ALPHA.ordinal()) {
			MessageBitmapAlpha message = wrapper.getMessage();
			if (!this.gameObject.equals(message.getGameObject())) {
				this.alpha = message.getAlpha();
			}
		}
	}
}

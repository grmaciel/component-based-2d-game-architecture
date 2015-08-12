package br.com.engine.component;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.CanvasHolder;
import br.com.engine.system.render.RenderWrapper;

public class BitmapRenderComponent extends AbstractComponent {
	private Bitmap bitmap;
	private int alpha = 255;

	public BitmapRenderComponent(GameObject gameObject, Bitmap bitmap) {
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
	}

	@Override
	public void onTouchListener(MotionEvent event) {
	}

	@Override
	public void onRender(RenderWrapper render) {
		CanvasHolder holder = render.getHolder();
//		holder.paint.setAlpha(alpha);
		holder.canvas.drawBitmap(this.getBitmap(), gameObject.getX(), 
				gameObject.getY(), holder.paint);
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
	}
}

package br.com.engine.component;

import android.graphics.Color;
import android.graphics.Rect;
import android.view.MotionEvent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.CanvasHolder;
import br.com.engine.system.render.RenderWrapper;

public class BoundBoxRenderComponent extends AbstractComponent {
	private Rect boundBox = new Rect();
	private GameObject gameObject;
	private int width;
	private int height;
	
	public BoundBoxRenderComponent(GameObject gameObject, int width, int height) {
		this.gameObject = gameObject;
		this.width = width;
		this.height = height;
	}
	
	private void updateRectPosition() {
		this.boundBox.left= (int) gameObject.getX();
		this.boundBox.right = (int) (gameObject.getX() + width);
		this.boundBox.top = (int) gameObject.getY();
		this.boundBox.bottom = (int) (gameObject.getY() + height);
	}
	/*
	@Override
	public void onDraw() {
	}

	@Override
	public void onDraw(Canvas c, Paint paint) {
		configureRectPosition();
		paint.setColor(Color.YELLOW);
		c.drawRect(boundBox, paint);	
	}*/

	@Override
	public void onUpdate() {
	}
	/*
	@Override
	public void onDraw(CanvasHolder holder) {
		configureRectPosition();
		holder.paint.setColor(Color.YELLOW);
		holder.canvas.drawRect(boundBox, holder.paint);
	}*/

	@Override
	public void onTouchListener(MotionEvent event) {
//		updateRectPosition();
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onRender(RenderWrapper render) {
		updateRectPosition();
		CanvasHolder wrapper = render.getHolder();
		wrapper.paint.setColor(Color.GREEN);
		wrapper.canvas.drawRect(boundBox, wrapper.paint);
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
	}
}

package br.com.furb.tagarela.puzzlegame.component;

import android.graphics.Bitmap;
import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.util.CollisionUtil;

public class DragComponent extends AbstractComponent {
	private Bitmap bmp;
	private boolean isSelected;
	
	public DragComponent(GameObject owner, Bitmap gameObjBmp) {
		this.gameObject = owner;
		this.bmp = gameObjBmp;
	}

	@Override
	public void onUpdate() {
		if (this.getGameObject().getX() > 700) {
			this.getGameObject().setX(0);
		}
		this.getGameObject().setX(this.getGameObject().getX()+1);
	}

	@Override
	public void onTouchListener(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.isSelected = CollisionUtil
				.touchSelected(getGameObject(), bmp, event.getX(), event.getY());
			break;
		case MotionEvent.ACTION_MOVE:
			if (isSelected) {
				actionMoveObject(event);
			}
			
			break;
		case MotionEvent.ACTION_UP: 
			if (isSelected) {
				isSelected = false;
			}
		default:
			break;
		}		
	}
	
	private void actionMoveObject(MotionEvent event) {
		int newX = (int) event.getX() - bmp.getWidth() /2;
		int newY = (int) event.getY() - bmp.getHeight();
		this.getGameObject().setX(newX);
		this.getGameObject().setY(newY);
	}

	@Override
	public void onRender(RenderWrapper render) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
		// TODO Auto-generated method stub
		
	}

}

package br.com.furb.tagarela.puzzlegame.component;

import android.graphics.Bitmap;
import android.graphics.Rect;
import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.message.MessageWrapper.MessageType;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.GameActivity;
import br.com.furb.tagarela.puzzlegame.enumeration.EnumMessageId;
import br.com.furb.tagarela.puzzlegame.messages.MessageAttract;
import br.com.furb.tagarela.puzzlegame.messages.GenericMessage;
import br.com.furb.tagarela.puzzlegame.messages.MessageBitmapAlpha;
import br.com.furb.tagarela.puzzlegame.messages.MessageDragScroll;
import br.com.furb.tagarela.puzzlegame.util.CollisionUtil;

public class DragDropComponent extends AbstractComponent {
	private Bitmap ownerBmp;
	private GameObject destiny;
	private Bitmap destinyBmp;
	private boolean isSelected;
	private boolean isAttractComponentRunning = false;

	public DragDropComponent(GameObject owner, Bitmap gameObjBmp, GameObject destinyObj, Bitmap destinyBmp) {
		this.gameObject = owner;
		this.destiny = destinyObj;
		this.ownerBmp = gameObjBmp;
		this.destinyBmp = destinyBmp;
	}
	
	@Override
	public void onUpdate() {
	}

	@Override
	public void onTouchListener(MotionEvent event) {
		switch (event.getAction()) {
		case MotionEvent.ACTION_DOWN:
			this.isSelected = CollisionUtil
				.touchSelected(getGameObject(), ownerBmp, event.getX(), event.getY());
			
			if (this.isSelected) {
				if (GameActivity.destacarDestino) {
					this.sendChangeAlphaMessage(100);
				}
				
				this.sendPlaySoundMessage();
				
			}
			break;
		case MotionEvent.ACTION_MOVE:
			if (isSelected) {
				actionMoveObject(event);
			}
			
			break;
		case MotionEvent.ACTION_UP: 
			if (isSelected) {
				actionOnFingerUp();
			}
		default:
			break;
		}
	}

	private void actionOnFingerUp() {
		isSelected = false;
		
		Rect orig = CollisionUtil.getBoundBox(getGameObject(),
				ownerBmp, GameActivity.bBoxReduceRatio);
		Rect dest = CollisionUtil.getBoundBox(destiny, destinyBmp,
				GameActivity.bBoxReduceRatio);
		
		if (orig.intersect(dest)) {
			this.setActive(false);
			this.getGameObject().setX(destiny.getX());
			this.getGameObject().setY(destiny.getY());
			
			this.sendScrollRemoveMessage();
		} else {
			this.sendScrollPutBackMessage();
		}
		
		this.sendChangeAlphaMessage(255);
		this.sendStopSoundMessage();
	}

	private void actionMoveObject(MotionEvent event) {
		if (isAttractComponentRunning) {
			return;
		}
		
		int newX = (int) event.getRawX() - ownerBmp.getWidth() /2;
		int newY = (int) event.getRawY() - ownerBmp.getHeight();
		this.getGameObject().setX(newX);
		this.getGameObject().setY(newY);
	}

	private void sendScrollPutBackMessage() {
		MessageDragScroll message = new MessageDragScroll(this.getGameObject());
		MessageWrapper wrapper = new MessageWrapper(EnumMessageId.DRAG_SCROLL_PUT_BACK.ordinal(), 
				message, MessageType.LISTENER);
		this.broadCastMessage(wrapper);
	}

	@Override
	public void onRender(RenderWrapper render) {
//		CanvasWrapper wrapper = render.getHolder();
//		wrapper.paint.setColor(Color.GREEN);
//		wrapper.paint.setStyle(Style.STROKE);
//		wrapper.canvas.drawRect(CollisionUtil.getBoundBox(getGameObject(), ownerBmp, GameActivity.bBoxReduceRatio), wrapper.paint);
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
		if (wrapper.getId() == EnumMessageId.STOP_DRAG.ordinal()) {
			if (this.isSelected) {
				this.isSelected = false;
				sendScrollPutBackMessage();	
				sendChangeAlphaMessage(255);
			}
		} else if (wrapper.getId() == EnumMessageId.ATTRACTING.ordinal()) {
			MessageAttract message = wrapper.getMessage();
			
			if (message.isAttracting()) {
				isAttractComponentRunning = true;
			} else {
				isAttractComponentRunning = false;
			}
		}
	}
	
	private void sendScrollRemoveMessage() {
		MessageDragScroll message = new MessageDragScroll(this.getGameObject());
		MessageWrapper wrapper = new MessageWrapper(EnumMessageId.DRAG_SCROLL_REMOVE_ELEMNT.ordinal(), 
				message, MessageType.LISTENER);
		this.broadCastMessage(wrapper);
	}
	
	private void sendChangeAlphaMessage(int alpha) {
		MessageBitmapAlpha message = new MessageBitmapAlpha(this.destiny, alpha);
		MessageWrapper wrapper = new MessageWrapper(EnumMessageId.CHANGE_ALPHA.ordinal(), 
				message, MessageType.LISTENER);
		this.broadCastMessage(wrapper);
	}
	

	private void sendPlaySoundMessage() {
		GenericMessage message = new GenericMessage();
		MessageWrapper wrapper = new MessageWrapper(EnumMessageId.DRAG_OBJECT_SELECTED.ordinal(), 
				message, MessageType.LISTENER);
		this.sendMessageToOwnerComponents(wrapper);	
	}
	
	private void sendStopSoundMessage() {
		GenericMessage message = new GenericMessage();
		MessageWrapper wrapper = new MessageWrapper(EnumMessageId.STOP_DRAG.ordinal(), 
				message, MessageType.LISTENER);
		this.sendMessageToOwnerComponents(wrapper);	
	}

}

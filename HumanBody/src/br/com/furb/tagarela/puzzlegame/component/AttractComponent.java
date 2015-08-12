package br.com.furb.tagarela.puzzlegame.component;

import android.util.Log;
import android.view.MotionEvent;
import br.com.engine.component.AbstractComponent;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;
import br.com.engine.system.message.MessageWrapper.MessageType;
import br.com.engine.system.render.RenderWrapper;
import br.com.furb.tagarela.puzzlegame.enumeration.EnumMessageId;
import br.com.furb.tagarela.puzzlegame.messages.MessageAttract;

public class AttractComponent extends AbstractComponent {
	private static final float attractSpeed = 0.5f;
	private GameObject destiny;
	private Boolean running = false;
	private Boolean attracting = false;
	private float startAttractX;
	private float startAttractY;
	private int distanceToStartAttract = 200;
	private int distanceToMoveToDestiny = 10;
	private int distanceToStopAttract = 50;

	public AttractComponent(GameObject owner, GameObject destinyObj) {
		this.gameObject = owner;
		this.destiny = destinyObj;
	}

	@Override
	public void onUpdate() {
		if (running) {
			double distance = this.getDistance();
			
			if (!attracting && distance <= distanceToStartAttract) {
				attracting = true;
				this.sendAttractionMessage(true);
			}
			if (attracting) {
				applyAttract(distance);
			}
		}
	}

	private double getDistance() {
		double x = Math.pow(destiny.getX() - this.gameObject.getX(), 2);
		double y = Math.pow(destiny.getY() - this.gameObject.getY(), 2);
		return Math.sqrt(x + y);
	}

	private void applyAttract(double distance) {
		this.startAttractX = this.gameObject.getX();
		this.startAttractY = this.gameObject.getY();
		if (distance < distanceToMoveToDestiny) {
			this.gameObject.setX(destiny.getX());
			this.gameObject.setY(destiny.getY());
		} else {
			int directionX = 1;
			int directionY = 1;
			if (this.gameObject.getX() > destiny.getX()) {
				directionX *= -1;
			}
			if (this.gameObject.getY() > destiny.getY()) {
				directionY *= -1;
			}
			
			this.gameObject.setX(this.gameObject.getX() + attractSpeed * directionX);
			this.gameObject.setY(this.gameObject.getY() + attractSpeed * directionY);
		}
	}

	private void sendAttractionMessage(boolean attraction) {
		MessageAttract message = new MessageAttract(attraction);
		MessageWrapper wrapper = new MessageWrapper(
				EnumMessageId.ATTRACTING.ordinal(), message,
				MessageType.LISTENER);
		this.sendMessageToOwnerComponents(wrapper);
	}


	@Override
	public void onTouchListener(MotionEvent event) {
		if (attracting) {
			double x = Math.pow(event.getX() - startAttractX, 2);
			double y = Math.pow(event.getY() - startAttractY, 2);
			double distance = Math.sqrt(x + y);
			
			if (distance > distanceToStopAttract) {
				attracting = false;
				this.sendAttractionMessage(false);	
			}
		}
	}

	@Override
	public void onRender(RenderWrapper render) {
	}

	@Override
	public void receiveMessage(MessageWrapper wrapper) {
		if (wrapper.getId() == EnumMessageId.DRAG_OBJECT_SELECTED.ordinal()) {
			running = true;
		} else if (wrapper.getId() == EnumMessageId.STOP_DRAG.ordinal()) {
			running = false;
		}
	}

}

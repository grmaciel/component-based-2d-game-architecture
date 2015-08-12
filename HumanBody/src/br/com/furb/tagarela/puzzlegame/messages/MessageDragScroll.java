package br.com.furb.tagarela.puzzlegame.messages;

import br.com.engine.model.GameObject;

public class MessageDragScroll {
	private GameObject gameObject;
	
	public MessageDragScroll(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}
}

package br.com.furb.tagarela.puzzlegame.messages;

import br.com.engine.model.GameObject;

public class MessageBitmapAlpha {
	private int alpha;
	private GameObject gameObject;
	
	public MessageBitmapAlpha(GameObject go, int alpha) {
		this.gameObject = go;
		this.alpha = alpha;
	}

	public int getAlpha() {
		return alpha;
	}

	public void setAlpha(int alpha) {
		this.alpha = alpha;
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public void setGameObject(GameObject go) {
		this.gameObject = go;
	}
	
	
}

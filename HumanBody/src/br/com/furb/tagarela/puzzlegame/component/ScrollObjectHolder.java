package br.com.furb.tagarela.puzzlegame.component;

import br.com.engine.model.GameObject;

public class ScrollObjectHolder {
	public GameObject obj;
	public int width;
	public int height;

	public ScrollObjectHolder(GameObject obj, int width, int height) {
		this.obj = obj;
		this.width = width;
		this.height = height;
	}
}

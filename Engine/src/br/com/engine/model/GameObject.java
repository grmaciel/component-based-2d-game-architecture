package br.com.engine.model;

import java.util.ArrayList;
import java.util.List;

import br.com.engine.component.IComponent;

public class GameObject {
	private float x;
	private float y;
	private int layer = Integer.MAX_VALUE;
	
	private List<IComponent> components = new ArrayList<IComponent>();
	
	public GameObject() {}
	
	public GameObject(int x, int y) {
		this.x = x;
		this.y = y;
	}
	
	public GameObject(int x, int y, int layer) {
		this(x, y);
		this.layer = layer;
	}
	
	public float getX() {
		return x;
	}
	public void setX(float x) {
		this.x = x;
	}
	public float getY() {
		return y;
	}
	public void setY(float y) {
		this.y = y;
	}
	
	public void addComponent(IComponent component) {
		this.components.add(component);
	}

	public List<IComponent> getComponents() {
		return components;
	}

	public void setComponents(List<IComponent> components) {
		this.components = components;
	}

	public int getLayer() {
		return layer;
	}

	public void setLayer(int layer) {
		this.layer = layer;
	}
}

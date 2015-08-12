package br.com.engine.game;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import br.com.engine.model.GameObject;


public class Scene {
	private List<GameObject> gameObjects = new ArrayList<GameObject>();
	private Scene nextScene;
	
	public void addGameObject(GameObject gameObject) {
		this.gameObjects.add(gameObject);
		
		Collections.sort(gameObjects, new Comparator<GameObject>() {
			@Override
			public int compare(GameObject left, GameObject right) {
				if (left.getLayer() < right.getLayer()) {
					return -1;
				} else if (left.getLayer() > right.getLayer()){
					return 1;
				}
				
				return 0;
			}
		});
	}

	public List<GameObject> getGameObjects() {
		return gameObjects;
	}

	public void setGameObjects(List<GameObject> gameObjects) {
		this.gameObjects = gameObjects;
	}
	
	public Scene getNextScene() {
		return nextScene;
	}

	public void setNextScene(Scene nextScene) {
		this.nextScene = nextScene;
	}
}
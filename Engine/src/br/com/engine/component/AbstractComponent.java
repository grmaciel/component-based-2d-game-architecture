package br.com.engine.component;

import br.com.engine.game.Game;
import br.com.engine.model.GameObject;
import br.com.engine.system.message.MessageWrapper;

public abstract class AbstractComponent implements IComponent {
	protected GameObject gameObject;
	private boolean active = true;
	
	public void sendMessageToOwnerComponents(MessageWrapper wrapper) {
		if (gameObject != null) {
			for (IComponent comp : gameObject.getComponents()) {
				comp.receiveMessage(wrapper);
			}	
		}
	}
	
	public void broadCastMessage(MessageWrapper wrapper) {
		Game.getInstance().sendMessage(wrapper);
	}

	public GameObject getGameObject() {
		return gameObject;
	}

	public void setGameObject(GameObject gameObject) {
		this.gameObject = gameObject;
	}

	@Override
	public boolean isActive() {
		return active;
	}

	public void setActive(boolean active) {
		this.active = active;
	}

}

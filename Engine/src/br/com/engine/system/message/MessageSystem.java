package br.com.engine.system.message;

import java.util.ArrayList;
import java.util.List;

import br.com.engine.component.IComponent;
import br.com.engine.game.Game;

public class MessageSystem {
	private List<IComponent> messageListener = new ArrayList<IComponent>();

	public void sendMessage(List<MessageWrapper> messages) {
		for (MessageWrapper messageWrapper : messages) {
			switch (messageWrapper.getType()) {
			case LISTENER:
				sendMessageToListener(messageWrapper);
				break;
			case TO_ALL:
				sendMessageToAllComponents(messageWrapper);
				break;
			default:
				break;
			}
		}
		messages.clear();
	}
	
	private void sendMessageToListener(MessageWrapper wrapper) {
		for (IComponent comp : messageListener) {
			comp.receiveMessage(wrapper);
		}
	}
	
	public void registerListener(IComponent component) {
		messageListener.add(component);
	}
	
	public void removeListener(IComponent component) {
		messageListener.add(component);
	}
	
	private void sendMessageToAllComponents(MessageWrapper messageWrapper) {
		for (IComponent comp : Game.getInstance().getAllComponents()) {
			comp.receiveMessage(messageWrapper);
		}
	}
}
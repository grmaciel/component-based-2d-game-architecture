package br.com.engine.system.logic;

import java.util.List;

import br.com.engine.component.IComponent;

public class LogicSystem {
	public void update(List<IComponent> components) {
		for (IComponent iComponent : components) {
			if (iComponent.isActive()) {
				iComponent.onUpdate();
			}
		}
	}
}

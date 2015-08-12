package br.com.engine.system.render;

import java.util.List;

import br.com.engine.component.IComponent;


public interface IRenderSystem {
	public void render(List<IComponent> components);
}

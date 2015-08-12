package br.com.engine.system.render;

public class RenderWrapper {
	private Object holder;
	
	public RenderWrapper() {}
	
	public RenderWrapper(Object holder) {
		this.holder = holder;
	}

	@SuppressWarnings("unchecked")
	public <T> T getHolder() {
		return (T) holder;
	}

	public void setHolder(Object holder) {
		this.holder = holder;
	}
}

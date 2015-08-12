package br.com.furb.tagarela.puzzlegame.messages;

public class MessageAttract {
	private boolean attracting;
	
	public MessageAttract(boolean attracting) {
		this.attracting = attracting;
	}
	
	public boolean isAttracting() {
		return attracting;
	}

	public void setAttracting(boolean attracting) {
		this.attracting = attracting;
	}

}

package br.com.engine.system.message;

public class MessageWrapper {
	private Object data;
	private int messageId;
	private MessageType type;
	
	public enum MessageType {
		TO_ALL,
		LISTENER;
	}
	
	public MessageWrapper(int id, Object data, MessageType type) {
		this.messageId = id;
		this.data = data;
		this.type = type;
	}
	
	public int getId() {
		return this.messageId;
	}
	
	@SuppressWarnings("unchecked")
	public <T> T getMessage() {
		return (T) this.data;
	}

	public MessageType getType() {
		return type;
	}

	public void setType(MessageType type) {
		this.type = type;
	}
}

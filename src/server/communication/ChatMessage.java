package server.communication;

import server.utils.BoardJavaGameServerConfig.MESSAGE_TYPES;

@SuppressWarnings("serial")
public class ChatMessage extends Message{
	public static final MESSAGE_TYPES TYPE = MESSAGE_TYPES.CHAT_MESSAGE;
	
	private String form;
	private String to;
	private String msg;
	public ChatMessage(String form, String to, String msg) {
		super();
		this.form = form;
		this.to = to;
		this.msg = msg;
	}
	public String getForm() {
		return form;
	}
	public void setForm(String form) {
		this.form = form;
	}
	public String getTo() {
		return to;
	}
	public void setTo(String to) {
		this.to = to;
	}
	public String getMsg() {
		return msg;
	}
	public void setMsg(String msg) {
		this.msg = msg;
	}
	
	
}

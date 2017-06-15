package server.communication;

import server.utils.BoardJavaGameServerConfig.MESSAGE_TYPES;

@SuppressWarnings("serial")
public class Login extends Message {
	public static final MESSAGE_TYPES TYPE = MESSAGE_TYPES.LOGIN;
	
	private String name;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}
	
}

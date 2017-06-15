package server.communication;


import java.io.Serializable;

import server.utils.BoardJavaGameServerConfig;
import server.utils.BoardJavaGameServerConfig.MESSAGE_TYPES;

@SuppressWarnings("serial")
public abstract class Message implements Serializable {
	
	public static final MESSAGE_TYPES TYPE = BoardJavaGameServerConfig.MESSAGE_TYPES.MESSAGE;
}

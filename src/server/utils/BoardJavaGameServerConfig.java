package server.utils;

import java.util.concurrent.TimeUnit;

public class BoardJavaGameServerConfig {
	
							/* AbstrctListener configuration */

	// ThreadPoolExecutor
	public static final int CORE_POOL_SIZE = 10;
	public static final int MAXIMUM_POOL_SIZE = 20;
	public static final int KEEP_ALIVE_TIME = 20;
	public static final TimeUnit BASE_TIME_UNIT = TimeUnit.MINUTES;
	
							/* Message Types */
	public enum MESSAGE_TYPES { 
		MESSAGE,RESPONSE, LOGIN,CHAT_MESSAGE
	}
							/* Response Fiend Types */
	public enum RESPONSE_TYPES {
	}
}

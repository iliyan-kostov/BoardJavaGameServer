package server.impl;

import java.io.ObjectOutputStream;
import java.util.HashMap;
import java.util.Map;


public class Connection {
	public static Map<String, ObjectOutputStream> connections = 
			new HashMap<String, ObjectOutputStream>();
}

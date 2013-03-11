package edu.buffalo.cse.cse486586.groupmessenger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class GlobalVar {
	
	static Map<Integer,MessageFormat> myReceivedMessages = new ConcurrentHashMap<Integer, MessageFormat>();
	static int mySequenceNumber=0;
	
	

}

package edu.buffalo.cse.cse486586.groupmessenger;

import java.io.Serializable;

public class MessageFormat implements Serializable
{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	String messageType; 					// messageType =  DATA / ORDER
	String devID; 							// devID = 5554 / 5556 / 5558 / 5560 / 5562
	int messageID; 							// messageID = 1...n or -1 for NOT_USED
	int seqNo; 								// sequence number for this message ; -1 for REQ
	String data; 	

}

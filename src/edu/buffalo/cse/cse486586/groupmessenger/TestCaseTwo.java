package edu.buffalo.cse.cse486586.groupmessenger;

public class TestCaseTwo extends Thread {

	String deviceID;
	TestCaseTwo (String deviceID)
	{
		this.deviceID=deviceID;
	}

	
	
	
	
	public  void run()
	{
		String tdeviceID=deviceID;
		if(deviceID.equals("5554"))
		{tdeviceID="avd1";
		}
		else if(deviceID.equals("5556")){tdeviceID="avd1";}
		else if(deviceID.equals("5558")){tdeviceID="avd0";}
		
		
		
		MessageFormat message0 = new MessageFormat();
		message0.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
		message0.messageID=GroupMessengerActivity.createMsgID();
		message0.devID=deviceID;
		message0.messageType="DATA_TEST2";
		message0.seqNo=GlobalVar.mySequenceNumber;
		GlobalVar.mySequenceNumber=(GlobalVar.mySequenceNumber+1);
		GroupMessengerActivity.bMulticast(message0);
		
		
	}
	
	
	
}

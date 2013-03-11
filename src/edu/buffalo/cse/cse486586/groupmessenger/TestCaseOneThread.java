package edu.buffalo.cse.cse486586.groupmessenger;

public class TestCaseOneThread extends Thread
{	
	String deviceID;
	TestCaseOneThread(String deviceID)
	{
		this.deviceID=deviceID;
	}
	
	public void run()
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
		message0.messageType="DATA";
		message0.seqNo=GlobalVar.mySequenceNumber;
		GlobalVar.mySequenceNumber=(GlobalVar.mySequenceNumber+1);
		GroupMessengerActivity.bMulticast(message0);
		try {
			Thread.sleep(3000);
			MessageFormat message1 = new MessageFormat();
			message1.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
			message1.messageID=GroupMessengerActivity.createMsgID();
			message1.devID=deviceID;
			message1.messageType="DATA";
			message1.seqNo=GlobalVar.mySequenceNumber;
			GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
			GroupMessengerActivity.bMulticast(message1);
			Thread.sleep(3000);
			
			MessageFormat message2 = new MessageFormat();
			message2.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
			message2.messageID=GroupMessengerActivity.createMsgID();
			message2.devID=deviceID;
			message2.messageType="DATA";
			message2.seqNo=GlobalVar.mySequenceNumber;
			GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
			GroupMessengerActivity.bMulticast(message2);
			Thread.sleep(3000);
			MessageFormat message3 = new MessageFormat();
			message3.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
			message3.messageID=GroupMessengerActivity.createMsgID();
			message3.devID=deviceID;
			message3.messageType="DATA";
			message3.seqNo=GlobalVar.mySequenceNumber;
			
			GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
			GroupMessengerActivity.bMulticast(message3);
			Thread.sleep(3000);

			MessageFormat message4 = new MessageFormat();
			message4.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
			message4.messageID=GroupMessengerActivity.createMsgID();
			message4.devID=deviceID;
			message4.messageType="DATA";
			message4.seqNo=GlobalVar.mySequenceNumber;
			
			GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
				GroupMessengerActivity.bMulticast(message4);
			
			
			
		} catch (InterruptedException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		
		
		
		
		
	}

}

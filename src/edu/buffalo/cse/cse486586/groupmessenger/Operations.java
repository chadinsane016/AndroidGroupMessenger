package edu.buffalo.cse.cse486586.groupmessenger;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import android.util.Log;

public class Operations {
	
	static int order=0;
	static int expectedSequenceNumberAvd1=0;
	static int expectedSequenceNumberAvd2=0;
	static int expectedSequenceNumberAvd0=0;
	static Map<Integer,MessageFormat> sequencerBufferedMessages = new ConcurrentHashMap<Integer, MessageFormat>();
	
	

	public static void sendToSequencer(MessageFormat receivedMessage) 
	{
		if(receivedMessage.messageType=="REQUEST")
		{
			if(receivedMessage.devID.equals("5554")) //:avd1
			{
				if(receivedMessage.seqNo==expectedSequenceNumberAvd1)    //received msg should be ordered first
				{
					receivedMessage.messageType="REPLY";
					receivedMessage.seqNo=order;
					Log.v("Sequencer Multicasting order of", ""+ receivedMessage.data);
					
					GroupMessengerActivity.bMulticast(receivedMessage);
					order=order+1;
					expectedSequenceNumberAvd1=expectedSequenceNumberAvd1+1;
					
					checkinSequencerBuffer();
							
				}
				else	//received msg is not the expected to be ordered next, then buffer received Messages
				{
					sequencerBufferedMessages.put(receivedMessage.messageID,receivedMessage);
					Log.v("Sequencer Puting in buffer,not expected avd1",receivedMessage.data);
					
				}
			}
			else if(receivedMessage.devID.equals("5556")) //:avd2
			{
				if(receivedMessage.seqNo==expectedSequenceNumberAvd2)    //received msg should be ordered first
				{
					receivedMessage.messageType="REPLY";
					receivedMessage.seqNo=order;
					
					GroupMessengerActivity.bMulticast(receivedMessage);
					order=order+1;
					expectedSequenceNumberAvd2=expectedSequenceNumberAvd2+1;
					checkinSequencerBuffer();
							
				}
				else	//received msg is not the expected to be ordered next, then buffer received Messages
				{
					sequencerBufferedMessages.put(receivedMessage.messageID,receivedMessage);
					Log.v("Sequencer Puting in buffer,not expected avd2",receivedMessage.data);
				}
			}
			else if(receivedMessage.devID.equals("5558")) //:avd0
			{
				if(receivedMessage.seqNo==expectedSequenceNumberAvd0)    //received msg should be ordered first
				{
					receivedMessage.messageType="REPLY";
					receivedMessage.seqNo=order;
					
					
					GroupMessengerActivity.bMulticast(receivedMessage);
					order=order+1;
					expectedSequenceNumberAvd0=expectedSequenceNumberAvd0+1;
					Log.v("Sequencer Puting in buffer,not expected avd0",receivedMessage.data);
					checkinSequencerBuffer();
							
				}
				else	//received msg is not the expected to be ordered next, then buffer received Messages
				{
					sequencerBufferedMessages.put(receivedMessage.messageID,receivedMessage);
				}
			}
			
		}
		
		
		
		
	}



	private static void checkinSequencerBuffer()
	{// TODO Auto-generated method stub
		Boolean checkAgain=true;
	while(checkAgain==true)
	{checkAgain=false;
	if(sequencerBufferedMessages.size()!=0){	
	for(int key:sequencerBufferedMessages.keySet())
	
		{
			if(sequencerBufferedMessages.get(key).devID.equals("5554"))
			{
				if(sequencerBufferedMessages.get(key).seqNo==expectedSequenceNumberAvd1)
				{
					sequencerBufferedMessages.get(key).messageType="REPLY";
					sequencerBufferedMessages.get(key).seqNo=order;
					
					GroupMessengerActivity.bMulticast(sequencerBufferedMessages.get(key));
					order=order+1;
					expectedSequenceNumberAvd1=expectedSequenceNumberAvd1+1;
					sequencerBufferedMessages.remove(key);
					checkAgain=true;
					
										
				}
				//else{checkAgain=false;}
			}
			else if(sequencerBufferedMessages.get(key).devID.equals("5556"))
			{
				if(sequencerBufferedMessages.get(key).seqNo==expectedSequenceNumberAvd2)
				{
					sequencerBufferedMessages.get(key).messageType="REPLY";
					sequencerBufferedMessages.get(key).seqNo=order;
					
					GroupMessengerActivity.bMulticast(sequencerBufferedMessages.get(key));
					order=order+1;
					expectedSequenceNumberAvd2=expectedSequenceNumberAvd2+1;
					sequencerBufferedMessages.remove(key);
					checkAgain=true;
					
								
					
				}//else{checkAgain=false;}
			}
			else if(sequencerBufferedMessages.get(key).devID.equals("5558"))
			{
				if(sequencerBufferedMessages.get(key).seqNo==expectedSequenceNumberAvd0)
				{
					sequencerBufferedMessages.get(key).messageType="REPLY";
					sequencerBufferedMessages.get(key).seqNo=order;
					
					GroupMessengerActivity.bMulticast(sequencerBufferedMessages.get(key));
					order=order+1;
					expectedSequenceNumberAvd0=expectedSequenceNumberAvd0+1;
					sequencerBufferedMessages.remove(key);
					checkAgain=true;
					
					
					
				}//else{checkAgain=false;}
			}
		}
		
	}
		}
	}
	
}

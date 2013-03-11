package edu.buffalo.cse.cse486586.groupmessenger;


import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Random;

import edu.buffalo.cse.cse486586.groupmessenger.MessageFormat;
import edu.buffalo.cse.cse486586.groupmessenger.provider.GroupMessengerProvider;


import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.app.Activity;
import android.content.ContentValues;
import android.content.Context;
import android.telephony.TelephonyManager;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

public class GroupMessengerActivity extends Activity {

	
	
	public static final int[] 	portArray 		= new int[] {11108, 11112, 11116};
	int connectPort;
	String deviceID;

	EditText inputText;
	TextView displayText;
	Button send;
	Button pTest;
	Button test1;
	Button test2;
	ServerThread serverThread;
	
	int nextToBeDelivered=0;
	
	
	

	Handler handler = new Handler();;  
	protected	String nl = System.getProperty("line.separator");
	
	
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_group_messenger);
        

        TextView displayText = (TextView) findViewById(R.id.textView1);
        displayText.setMovementMethod(new ScrollingMovementMethod());
        
        inputText = (EditText) findViewById(R.id.editText1);
        pTest=(Button) findViewById(R.id.button1);
        test1=(Button) findViewById(R.id.button2);
        test2=(Button) findViewById(R.id.button3);
        send=(Button) findViewById(R.id.button4);
        
        serverThread = new ServerThread();
		serverThread.start();
        
        pTest.setOnClickListener(new OnPTestClickListener(displayText, getContentResolver()));
        
        
    	TelephonyManager tel = (TelephonyManager)this.getSystemService(Context.TELEPHONY_SERVICE);
		deviceID= tel.getLine1Number().substring(tel.getLine1Number().length()-4);
		
		/*if(Integer.parseInt(deviceID) == 5558)   //If  I am   sequencer, then start sequencer thread.                                                                                             
		{
			Sequencer sequencerThread = new Sequencer();
			sequencerThread.start();
			
		}
		/*else if(Integer.parseInt(deviceID) == 5556)
		{
			connectPort = 11108;
		}*/
        
        
        
        
        
        
        OnClickListener sendClickListener = new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	try {
	        		String sendText=inputText.getText().toString();
	        		inputText.setText("");
	        		ClientThread clientThread = new ClientThread(sendText,"DATA");
	        		clientThread.start();
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	        		
	        }
	    };
	    
	    OnClickListener test1ClickListener = new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	try {
					TestCaseOneThread testCaseOne = new TestCaseOneThread(deviceID);
					testCaseOne.start();
	        		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	        		
	        }
	    };
	    
	    OnClickListener test2ClickListener = new OnClickListener() {
	        @Override
	        public void onClick(View v) {
	        	try {
	        		
	        		TestCaseTwo testCaseTwo = new TestCaseTwo(deviceID);
	        		testCaseTwo.start();
	        		
	        		
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
	        	        		
	        }
	    };
	    
	    
	    
	    send.setOnClickListener(sendClickListener);
        test1.setOnClickListener(test1ClickListener);
        test2.setOnClickListener(test2ClickListener);
        
        
        
        
        
    }

   
	public class ClientThread extends Thread
	{

		//int connectPort;
		String msgType;
		String sendText;
		

		ClientThread ( String sendText, String msgType)
		{
			this.msgType= msgType;
			this.sendText=sendText;
		}
		public void run()
		{

			if(sendText!=null){
			try{
				
				MessageFormat message = new MessageFormat();
				//Create message of type data
				message.data=sendText;
				message.messageID=createMsgID();
				message.devID=deviceID;
				message.messageType="DATA";
				message.seqNo=GlobalVar.mySequenceNumber;
				
				GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
				bMulticast(message);
				
				
				
			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}
			
			
			
			

			
		}
		
		
		

	}
	
	
	
	
	//-----------Server Thread

	public class ServerThread  extends Thread {

		static final String TAG="Received msg by Server";
		String receivedMsg;
		ServerSocket serverSocket=null;
		ObjectInputStream 	in;
		MessageFormat recvData;
		public void run()
		{
			try
			{
				serverSocket=new ServerSocket(10000);

			}
			catch(Exception e)
			{

				e.printStackTrace();
			}

			while(true)
			{
				try
				{	
					Socket incomingSocket=serverSocket.accept();
					Log.v("GroupMessengerActivity", "Connection received");
					in = new ObjectInputStream(incomingSocket.getInputStream());
					recvData=(MessageFormat) in.readObject();
					Log.v("GroupMessengerActivity", "data received"+recvData.data);
					incomingSocket.close();			

				}
				catch(Exception e)
				{
					e.printStackTrace();
				}
				
				
				if (recvData.messageType.contentEquals("DATA_TEST2"))
				{
					Log.v("Received msg from test 2", recvData.data);
					String tdeviceID=deviceID;
					if(deviceID.equals("5554"))
					{tdeviceID="avd1";
					}
					else if(deviceID.equals("5556")){tdeviceID="avd1";}
					else if(deviceID.equals("5558")){tdeviceID="avd0";}
					
					MessageFormat message1 = new MessageFormat();
					message1.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
					message1.messageID=GroupMessengerActivity.createMsgID();
					message1.devID=deviceID;
					message1.messageType="DATA";
					message1.seqNo=GlobalVar.mySequenceNumber;
					
					GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
					bMulticast(message1);
					Log.v("Sent msg 1 from test 2", message1.data);
					
					
					MessageFormat message2 = new MessageFormat();
					message2.data=tdeviceID+":"+GlobalVar.mySequenceNumber;
					message2.messageID=GroupMessengerActivity.createMsgID();
					message2.devID=deviceID;
					message2.messageType="DATA";
					message2.seqNo=GlobalVar.mySequenceNumber;
					
					GlobalVar.mySequenceNumber=GlobalVar.mySequenceNumber+1;
					bMulticast(message2);
					Log.v("Sent msg 2 from test 2", message2.data);
					
					
					recvData.messageType="DATA";
					
				}
				if (recvData.messageType.contentEquals("DATA")) {

					
					Log.v("Received msg of type data", recvData.data);
					GlobalVar.myReceivedMessages.put(recvData.messageID, recvData);
					Log.v("INSERT To my received messages buffer", "" + recvData.messageID+ " , " + recvData.data);
					
					
					if(Integer.parseInt(deviceID) == 5558)  //I am sequencer, then i send it to sequencer thread
					{
						
						recvData.messageType="REQUEST";
						Operations.sendToSequencer(recvData);
						
						/*Sequencer sequencerThread = new Sequencer(recvData);
						sequencerThread.start();*/
					}
					
					
					
					
					
				}
				else if(recvData.messageType.contentEquals("REPLY"))
				{
					Log.v("Received msg of type reply", recvData.data);
					if(recvData.seqNo==nextToBeDelivered)
					{
						//remove from myreceivedbuffer,deliver this message
						Log.v("Received order from sequencer", "for msg:"+recvData.data+"It is next to be delivered");
						GlobalVar.myReceivedMessages.remove(recvData.messageID);
						
						deliverMessage(recvData);
						Log.v("Received order from sequencer", "Delivered");
						nextToBeDelivered=nextToBeDelivered+1;
						checkMessagesToBeDeliveredinMyOwnBuffer();
						
						
					}
					else
					{
						GlobalVar.myReceivedMessages.put(recvData.messageID, recvData); //Replace with new sequence number
						Log.v("Received order from sequencer", "Order stored for a msg in buffer");
						
					}
					
					
					
					
					
					
				}
				
												
											

			}

		}
		
		


		
	}
	
	
	public static int createMsgID()
	{
		
		int min =-300;
		int max=300;
		Random gen = new Random();
				int random=gen.nextInt(max-min+1)+min;
				
		
		return random;
	}

	public void checkMessagesToBeDeliveredinMyOwnBuffer() 
	{Boolean checkAgain=true;
		while(checkAgain==true)
		{	checkAgain=false;
			for(int index:GlobalVar.myReceivedMessages.keySet())
			{
			if(GlobalVar.myReceivedMessages.get(index).messageType.equals("REPLY"))
			{
				if(GlobalVar.myReceivedMessages.get(index).seqNo==nextToBeDelivered)
				{
					deliverMessage(GlobalVar.myReceivedMessages.get(index));
					Log.v("DElivering from buffer", "Delivered");
					nextToBeDelivered=nextToBeDelivered+1;
					GlobalVar.myReceivedMessages.remove(GlobalVar.myReceivedMessages.get(index).messageID);
					checkAgain=true;
					
				}
			}
			}
		}
		
	}
	public void deliverMessage(MessageFormat displayMessage) {
		final String receivedMsg=displayMessage.data;
		
		Log.v("Got msg in deliverMessage function", ""+receivedMsg);
		//if((receivedMsg!= null))
		//{//Display on text field
		if((receivedMsg!= null))
		{//Display on text field
			handler.post(new Runnable() {						  
				 @Override 
				 public void run() { // append data to
				TextView tv=(TextView)	findViewById(R.id.textView1);
				
				 tv.append(receivedMsg);
				 tv.append(nl);
				 EditText et =(EditText) findViewById(R.id.editText1);
				 
				 

				  } });
		}		
			
		//}
		
		
		
		insertCV(displayMessage.seqNo, displayMessage.data);     //Store in database
		
		
		
	}
	public static void bMulticast(MessageFormat message) {
		Socket multicastSocket;
		ObjectOutputStream out;
		 
		try{
			for(int port : portArray){
				multicastSocket = new Socket("10.0.2.2",port);
				if(multicastSocket.isConnected())
	    			Log.v("GroupMessengerActivity", "Client-Server: Connection Establised with Emulator:"+(port/2));
				out = new ObjectOutputStream(multicastSocket.getOutputStream());
   			//Send Data To All peers
   			out.writeObject(message);
   			Log.v("GroupMessengerActivity", "Client-Server: Sent Data"+message.data+" to Emulator: "+(port/2)+"of type"+message.messageType);
	    		
   			multicastSocket.close();
   			//out.flush();
			}
		}
		catch(Exception e){
			e.printStackTrace();
		}
		// TODO Auto-generated method stub
		
	}
	
	private void insertCV(int key, String value) {
		
		// TODO Auto-generated method stub
		try {
			
			// generate appropriate Uri
			Uri uri = Uri.parse(GroupMessengerProvider.contentUri);
			if (uri == null)
				throw new IllegalArgumentException();
			ContentValues valuesCV = new ContentValues();
			valuesCV.put("key", key);
			valuesCV.put("value", value);

			// insert into ContentProvider i.e MessageProvider for my app
			getContentResolver().insert(uri, valuesCV);

		} catch (Exception e) {
			e.printStackTrace();
		}
	}
	
	
	
	@Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_group_messenger, menu);
        return true;
    }
}

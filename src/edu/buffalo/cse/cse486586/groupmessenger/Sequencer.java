package edu.buffalo.cse.cse486586.groupmessenger;

public class Sequencer extends Thread
{
	MessageFormat requestedMsg;
	Sequencer(MessageFormat requestedMsg)
	{
		this.requestedMsg=requestedMsg;
	}	
	
	public void run()
	{
		
	}

}

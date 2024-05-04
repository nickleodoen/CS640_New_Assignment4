import java.util.TimerTask;

// Individual timeout thread for each packet, removing objects when ACK received
public class TimeoutPacket extends TimerTask{

	public int curRetrans;
	public TCPpacket tcpPacket;
	private TimeoutManager toMan;

	public TimeoutPacket(TimeoutManager toMan, TCPpacket tcpPacket, int curRetrans){
		this.toMan = toMan;
		this.tcpPacket = tcpPacket;
		this.curRetrans = curRetrans;
	}

	public void run(){ // timeout wait thread
		curRetrans += 1;
		toMan.resendPacket(this);
	}
}
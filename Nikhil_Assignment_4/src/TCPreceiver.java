import java.util.PriorityQueue;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileOutputStream;


// Receives a file from sender using TCPbase
public class TCPreceiver extends TCPbase{
  PriorityQueue<TCPpacket> packetBuffer;
  FileOutputStream stream;

  public TCPreceiver(int port, int mtu, int sws, String fileName){
    super(port, fileName, mtu, sws);
    packetBuffer = new PriorityQueue<TCPpacket> ((p1, p2) -> p1.seqNum - p2.seqNum);

    try{
      stream = new FileOutputStream(fileName);
    }catch(FileNotFoundException e){
      System.out.println("File could not be created");
      return;
    }
  }

  public void handlePacket(TCPpacket packet){
    byte[] data = packet.payloadData;

    if (packet.seqNum < ackNum){
      outOfSeqPackets += 1;
      return;
    } else if(packet.seqNum > ackNum){
      outOfSeqPackets += 1;
      for (TCPpacket p : packetBuffer){ // check for duplicate packet
        if (p.seqNum == packet.seqNum)
          return;
        else if (p.seqNum > packet.seqNum)
          break;
      }

      packetBuffer.add(packet);
      return;
    }

    ackNum = packet.seqNum + Math.max(packet.payloadData.length, 1);
    dataTransfered += packet.payloadData.length;

    try{
      // using FileChannel for out-of-order data - // https://stackoverflow.com/questions/9558979/java-outputstream-skip-offset
      stream.write(data);

      if(packetBuffer.size() > 0)
        handlePacket(packetBuffer.poll());
    }catch(IOException e){
      System.out.println(e);
    }
  }

  public void disconnect(){
    super.disconnect(); 

    try{
      stream.close();  
    }catch(IOException e){
      System.out.println(e);
    }
  }
}
import java.net.InetAddress;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.lang.Math;

//Sends file to receiver
public class TCPsender extends TCPbase{

  public TCPsender(int port, InetAddress ip, int remotePort, String fileName, int mtu, int sws){
    super(port, fileName, mtu, sws);

    this.ip = ip; //IP of the remote peer.
    this.remotePort = remotePort; //remote port to send to.
  }

  //Send file over to receiver.
  public void sendFile(){
    while (established == false);
    if (running() == false)
      return;

    FileInputStream stream = null;
    
    try {
      stream = new FileInputStream(fileName);
    } catch(FileNotFoundException e){
      System.out.println("File not found");
    }

    try {
      while (stream.available() > 0){
        // Check if we are in the sliding window.
        toMan.waitTillPacketsLessThanNum(sws-1);

        // Read data into byte new array of maximum size
        byte[] data = new byte [Math.min(getMaxDataSize(), stream.available())];
        stream.read(data, 0, data.length);

        if (running() == false)
          return;

        dataTransfered += data.length;
        sendTCP(data, new Boolean[]{false, false, false});
      }

      stream.close();
    } catch(IOException e) {
      System.out.println(e);
    }

    toMan.waitTillPacketsLessThanNum(0);
    if(running() == false)
      return;

    initiatedClose = true;
    this.sendFIN();
  }

  public void handlePacket(TCPpacket packet){
    
  }

  int getMaxDataSize(){
    return this.mtu - TCPpacket.headerSize - 28;
  }
}
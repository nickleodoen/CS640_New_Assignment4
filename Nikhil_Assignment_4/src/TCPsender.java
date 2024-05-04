import java.net.InetAddress;
import java.lang.Math;
import java.nio.ByteBuffer;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.FileInputStream;

public class TCPsender extends TCPbase{

  public TCPsender(int port, InetAddress ip, int remotePort, String fileName, int mtu, int sws){
    super(port, fileName, mtu, sws);

    this.ip = ip; // remote IP
    this.remotePort = remotePort; // remote port (send to)
  }

  public void sendFile(){ // to receiver
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
        toMan.waitTillPacketsLessThanNum(sws-1); // check if sliding window

        byte[] data = new byte [Math.min(getMaxDataSize(), stream.available())]; // data into byte array of max size
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
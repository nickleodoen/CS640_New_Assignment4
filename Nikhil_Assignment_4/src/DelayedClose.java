public class DelayedClose extends Thread{
  private TCPbase tcp;
  private long time;
  
  //Closes the thread after a certain amount of time. Used in FIN process.
  public DelayedClose(TCPbase tcp, long time){
    this.tcp = tcp;
    this.time = time;
  }

  public void run(){
    System.out.println("Closing in " + time + " ms");
    try {
        Thread.sleep(time);
    } catch(InterruptedException ex){
        Thread.currentThread().interrupt();
    }

    tcp.stopThread();
  }
}

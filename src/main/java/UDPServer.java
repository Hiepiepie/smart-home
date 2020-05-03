import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;

public class UDPServer implements Runnable{
    private volatile boolean exit;
    private InetAddress ia;
    private final DatagramSocket ds;
    protected DatagramPacket dp;
    private byte[] buf = new byte[1024];

    public UDPServer(int port, InetAddress ia) throws SocketException{
        this.ia = ia;
        this.ds = new DatagramSocket(port);
        exit = false;
    }

    public void run(){

        System.out.println("Server Start");
        try{
            while (!exit) {
               receivePackage();
            }
        }
        catch( Exception e){
            e.printStackTrace();
        }
    }

    public void stop(){
        exit= true;
    }

    public void receivePackage() throws IOException {
        byte[] buf = new byte[1024];
        dp = new DatagramPacket(buf, buf.length);
        ds.receive(dp);
        String str = new String(dp.getData(), 0, dp.getLength());
        String ipAddress = String.valueOf(dp.getAddress());
        int port = dp.getPort();

        System.out.println("Server-> IP : " + ipAddress + " | Port : " + port + " | Information : " + str + "\n");
    }



}

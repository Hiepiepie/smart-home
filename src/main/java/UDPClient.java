import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;


abstract public class UDPClient implements Runnable {
    private volatile boolean exit;
    protected int port;
    protected InetAddress ia;
    protected DatagramSocket ds;
    protected DatagramPacket dp;
    protected final Random rand;

    UDPClient() throws Exception {
        ds = new DatagramSocket();
        rand = new Random();
        exit = false;
    }

    public void run() {
        System.out.println("Client Start");
        try {
            while (!exit) {
                sendPackage();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void stop(){
    exit = true;
    }

    public abstract String getType();
    public abstract String update();

    public void sendPackage() throws Exception{
        String msg = getType() + update();
        byte[] b = (msg).getBytes();
        dp = new DatagramPacket(b, b.length, ia, 1234);
        ds.send(dp);
    }
}






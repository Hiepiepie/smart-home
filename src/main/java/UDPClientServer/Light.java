package UDPClientServer;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.util.Random;

public class Light {

    public static void main(String[] args) throws Exception {
        Light h = new Light();
        while (true){
            h.sendPackage();
        }
    }

    private int bright;
    private final Random rand;
    protected int port = 1234;
    protected InetAddress ia;
    protected DatagramSocket ds;
    protected DatagramPacket dp;
    public Light() throws Exception{
        ia = InetAddress.getLocalHost();
        ds = new DatagramSocket();
        rand = new Random();
        bright = rand.nextInt(101);
    }

    public int getBright() {
        return bright;
    }

    public String getType(){
        return "Brightness ";
    }

    public String update() {
        bright += (rand.nextBoolean() ? 1 : -1);
        if(bright >= 100) bright -= 1;
        if(bright <= 0) bright += 1;
        return bright + "%";
    }
    public void sendPackage() throws Exception{
        String msg = getType() + update();
        byte[] b = (msg).getBytes();
        dp = new DatagramPacket(b, b.length, ia, port);
        ds.send(dp);
    }
}
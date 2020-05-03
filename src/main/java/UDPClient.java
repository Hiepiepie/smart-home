import java.io.IOException;
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.SocketException;
import java.util.Random;


abstract public class UDPClient extends Thread {
    protected int port;
    protected InetAddress ia;
    protected DatagramSocket ds;
    protected DatagramPacket dp;
    protected final Random rand;

    UDPClient() throws Exception {
        ds = new DatagramSocket();
        rand = new Random();
    }

    public void run() {
        try {
            while (true) {
                sendPackage();
                Thread.sleep(2000);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
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

class Thermometer extends UDPClient{
    private int temp;

    public Thermometer(int port, InetAddress ia) throws Exception{
        this.port = port;
        this.ia = ia;
        temp = rand.nextInt(46) - 5;
    }

    public int getTemp(){
        return temp;
    }

    public String getType(){
        return "Temperatur ";
    }

    public String update() {
        temp += (rand.nextBoolean() ? 1 : -1);
        if(temp >= 40) temp -= 1;
        if(temp <= -5) temp += 1;
        return temp + " °Celcius";
    }
}

class Light extends UDPClient{
    private int bright;

    public Light(int port, InetAddress ia) throws Exception{
        this.port = port;
        this.ia = ia;
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
}

class Hygrometer extends UDPClient{
    private int hum;

    public Hygrometer(int port, InetAddress ia) throws Exception{
        this.port = port;
        this.ia = ia;
        hum = rand.nextInt(101);
    }

    public int getHum() {
        return hum;
    }

    public String getType(){
        return "Humidity ";
    }

    public String update() {
        hum += (rand.nextBoolean() ? 1 : -1);
        if(hum >= 100) hum -= 1;
        if(hum <= 0) hum += 1;
        return hum + "%";
    }
}

import java.net.InetAddress;

public class Hygrometer extends UDPClient{

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        InetAddress ia = InetAddress.getByName(args[1]);
        Hygrometer hygrometer = new Hygrometer(port, ia);
        new Thread(hygrometer).start();
    }

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

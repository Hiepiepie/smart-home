import java.net.InetAddress;

public class Thermometer extends UDPClient{

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        InetAddress ia = InetAddress.getByName(args[1]);
        Thermometer thermometer =  new Thermometer(port, ia);
        new Thread(thermometer).start();
    }

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

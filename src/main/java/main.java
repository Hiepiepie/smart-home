import java.net.InetAddress;

public class main {

    public static void main(String[] args) throws Exception {
        int port = 1234;
        InetAddress ia = InetAddress.getLocalHost();
        Thermometer thermometer = new Thermometer(port, ia);
        Light light = new Light(port, ia);
        Hygrometer hygrometer = new Hygrometer(port, ia);
        UDPServer udpServer = new UDPServer(port, ia);
        thermometer.start();
        light.start();
        hygrometer.start();
        udpServer.start();
    }
}

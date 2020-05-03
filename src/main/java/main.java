import java.net.InetAddress;
import java.util.Scanner;

public class main {

    public static void main(String[] args) throws Exception {
        int port = 1234;
        InetAddress ia = InetAddress.getLocalHost();
        Thermometer thermometer = new Thermometer(port, ia);
        Light light = new Light(port, ia);
        Hygrometer hygrometer = new Hygrometer(port, ia);
        UDPServer udpServer = new UDPServer(port, ia);
        new Thread(thermometer).start();
        new Thread(light).start();
        new Thread(hygrometer).start();
        new Thread(udpServer).start();
        Scanner scanner = new Scanner(System.in);
        String stop = "";
        while(!(stop.equals("x"))) {
            stop = scanner.next();
        }
            thermometer.stop();
            light.stop();
            hygrometer.stop();
            udpServer.stop();
        System.out.println("All Devices and Server are shutted down");
    }
}

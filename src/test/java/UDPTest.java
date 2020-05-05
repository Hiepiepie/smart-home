import UDPClientServer.Hygrometer;
import UDPClientServer.Light;
import UDPClientServer.Thermometer;
import UDPClientServer.UDPServer;
import org.junit.*;

import java.net.InetAddress;

import static org.junit.Assert.*;

//Test to send and receive packages between Client and Server
public class UDPTest {
    private static Hygrometer hygrometer;
    private static Light light;
    private static Thermometer thermometer;
    private static UDPServer udpServer;
    private static int port;
    private static InetAddress ia;

    @BeforeClass
    public static void suiteSetup() throws Exception{
        port = 1234;
        ia = InetAddress.getLocalHost();
        hygrometer = new Hygrometer();
        light = new Light();
        thermometer = new Thermometer();
        udpServer = new UDPServer();
    }

    @AfterClass
    public static void suiteTeardown() {
        hygrometer = null;
        light = null;
        thermometer = null;
        udpServer = null;
    }

    @Test
    public void HygrometerTest() throws Exception {
        hygrometer.sendPackage();
        udpServer.receivePackage();

    }

    @Test
    public void ThermometerTest() throws Exception {
        thermometer.sendPackage();
        udpServer.receivePackage();

    }

    @Test
    public void LightTest() throws Exception {
        light.sendPackage();
        udpServer.receivePackage();

    }
}
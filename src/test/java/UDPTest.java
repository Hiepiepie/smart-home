import org.junit.*;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Arrays;

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
        hygrometer = new Hygrometer(port, ia);
        light = new Light(port, ia);
        thermometer = new Thermometer(port, ia);
        udpServer = new UDPServer(port, ia);
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
        String clientStr =  new String(hygrometer.dp.getData(), 0, hygrometer.dp.getLength());
        String serverStr = new String(udpServer.dp.getData(), 0 ,udpServer.dp.getLength());
        assertEquals(clientStr, serverStr);
    }

    @Test
    public void ThermometerTest() throws Exception {
        thermometer.sendPackage();
        udpServer.receivePackage();
        String clientStr =  new String(thermometer.dp.getData(), 0, thermometer.dp.getLength());
        String serverStr = new String(udpServer.dp.getData(), 0 ,udpServer.dp.getLength());
        assertEquals(clientStr, serverStr);
    }

    @Test
    public void LightTest() throws Exception {
        light.sendPackage();
        udpServer.receivePackage();
        String clientStr =  new String(light.dp.getData(), 0, light.dp.getLength());
        String serverStr = new String(udpServer.dp.getData(), 0 ,udpServer.dp.getLength());
        assertEquals(serverStr, clientStr);
    }
}
package UDPClientServer;

import org.junit.*;

import java.io.ByteArrayOutputStream;
import java.io.PrintStream;
import java.net.InetAddress;

import static org.junit.Assert.*;
import static org.junit.Assert.assertEquals;

//Test to send and receive packages between Client and Server
public class UDPTest {
    private static Hygrometer hygrometer;
    private static Light light;
    private static Thermometer thermometer;
    private static UDPServer udpServer;
    private static int port;
    private static InetAddress ia;
    private static String msg;

    private final ByteArrayOutputStream outContent = new ByteArrayOutputStream();
    private final PrintStream originalOut = System.out;

    public static final String ANSI_RED = "\u001B[31m";

    @BeforeClass
    public static void suiteSetup() throws Exception{
        port = 1234;
        ia = InetAddress.getLocalHost();
        hygrometer = new Hygrometer();
        light = new Light();
        thermometer = new Thermometer();
        udpServer = new UDPServer();
        msg = "Test Message";
    }

    @AfterClass
    public static void suiteTeardown() {
        hygrometer = null;
        light = null;
        thermometer = null;
        udpServer = null;
    }

    @Test
    public void hygrometerTest() throws Exception {
        hygrometer.sendPackage(msg);
        udpServer.receivePackage();
        //String clientStr =  new String(hygrometer.getDp().getData(), 0, hygrometer.getDp().getLength());
        String serverStr = new String(udpServer.dp.getData(), 0 ,udpServer.dp.getLength());
        assertEquals(msg, serverStr);

    }

    @Test
    public void thermometerTest() throws Exception {
        thermometer.sendPackage(msg);
        udpServer.receivePackage();
        //String clientStr =  new String(thermometer.getDp().getData(), 0, thermometer.getDp().getLength());
        String serverStr = new String(udpServer.dp.getData(), 0 ,udpServer.dp.getLength());
        assertEquals(msg, serverStr);

    }

    @Test
    public void lightTest() throws Exception {
        light.sendPackage(msg);
        udpServer.receivePackage();
        //String clientStr =  new String(light.getDp().getData(), 0, light.getDp().getLength());
        String serverStr = new String(udpServer.dp.getData(), 0 ,udpServer.dp.getLength());
        assertEquals(msg, serverStr);

    }

    @Test
    public void packetLossCheckTest() throws Exception{
        //redirect System.out.println
        System.setOut(new PrintStream(outContent));

        // test if method "packetCheck()" works properly
        String msg = hygrometer.getId() + ";" + hygrometer.getType() + ";" + hygrometer.getInfoUpdate();
        hygrometer.sendPackage(msg);
        udpServer.receivePackage();
        udpServer.extractPackage();
        udpServer.packetCheck();

        //skip 1 id to make a fake packet loss
        msg = (hygrometer.getId()+1) + ";" + hygrometer.getType() + ";" + hygrometer.getInfoUpdate();
        hygrometer.sendPackage(msg);
        udpServer.receivePackage();
        udpServer.extractPackage();
        udpServer.packetCheck();
        assertEquals(ANSI_RED + "Packet loss detected", outContent.toString().substring(0,25));
    }
}
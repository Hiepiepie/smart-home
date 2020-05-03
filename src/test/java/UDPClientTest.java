import org.junit.Test;
import org.junit.BeforeClass;
import org.junit.AfterClass;

import java.net.InetAddress;

import static org.junit.Assert.*;

//Test all the Client devices
public class UDPClientTest {
    private static Hygrometer hygrometer;
    private static Light light;
    private static Thermometer thermometer;
    private static int port;
    private static InetAddress ia;

    @BeforeClass
    public static void suiteSetup() throws Exception {
        // Set up something for the test cycle
        port = 1234;
        ia = InetAddress.getLocalHost();
        hygrometer = new Hygrometer(port, ia);
        light = new Light(port, ia);
        thermometer = new Thermometer(port, ia);
    }

    @AfterClass
    public static void suiteTeardown() {
        hygrometer = null;
        light = null;
        thermometer = null;
    }

    @Test
    public void getTypeTest() {
        // Test each derived Class
        assertEquals(hygrometer.getType(), "Humidity ");
        assertEquals(thermometer.getType(), "Temperatur ");
        assertEquals(light.getType(),"Brightness ");
    }

    @Test
    public void updateTest() {
        int hum = hygrometer.getHum();
        assertTrue(hum<=100 && hum >= 0);
        for (int i = 0; i <= 10 ; i++)
            hygrometer.update();
        hum = hygrometer.getHum();
        assertTrue(hum<=100 && hum >= 0);

        int bright = light.getBright();
        assertTrue(bright<=100 && bright >= 0);
        for (int i = 0; i <= 10 ; i++)
            light.update();
        bright = light.getBright();
        assertTrue(bright<=100 && bright >= 0);

        int temp = thermometer.getTemp();
        assertTrue(temp<=40 && temp >= -5);
        for (int i = 0; i <= 10 ; i++)
            thermometer.update();
        temp = thermometer.getTemp();
        assertTrue(temp<=40 && temp >= -5);
    }
}
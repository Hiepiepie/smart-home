import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThermometerTest {
    Thermometer t = new Thermometer();
    @Test
    public void getType() {

        assertEquals(t.getType(), "Temperatur ");
    }

    @Test
    public void update() {
        int t1 = t.update().length();
        int t2 = t.update().length()-7;
        assertEquals(t.update().substring(t2,t1),"Celcius");
    }
    }
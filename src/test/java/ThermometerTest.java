import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class ThermometerTest {

    @Test
    public void getType() {
        Thermometer t = new Thermometer();
        assertEquals(t.getType(), "Temperatur ");
    }

    @Test
    public void update() {
    }
    }
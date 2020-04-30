import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HygrometerTest {
    Hygrometer h = new Hygrometer();
    @Test
    public void getType() {

        assertEquals(h.getType(), "Humidity ");
    }

    @Test
    public void update() {
        assertEquals(h.update().charAt(h.update().length()-1),'%');
    }
}
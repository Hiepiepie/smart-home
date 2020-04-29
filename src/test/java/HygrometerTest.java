import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class HygrometerTest {

    @Test
    public void getType() {
        Hygrometer h = new Hygrometer();
        assertEquals(h.getType(),"Humidity ");
    }

    @Test
    public void update() {
    }

    @Before
    public void setUp() throws Exception {
    }

    @After
    public void tearDown() throws Exception {
    }

    @Test
    public void testGetType() {
    }
}
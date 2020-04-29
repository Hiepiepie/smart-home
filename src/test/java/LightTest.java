import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightTest {

    @Test
    public void getType() {
        Light l = new Light();
        assertEquals(l.getType(),"Brightness ");
    }

    @Test
    public void update() {
    }
}
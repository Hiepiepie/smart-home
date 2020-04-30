import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import static org.junit.Assert.*;

public class LightTest {
    Light l = new Light();
    @Test
    public void getType() {

        assertEquals(l.getType(),"Brightness ");
    }

    @Test
    public void update() {
        assertEquals(l.update().charAt(l.update().length()-1),'%');
    }
}
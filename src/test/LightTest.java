import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class LightTest {

    @Test
    void getType() {
        Light l = new Light();
        assertEquals(l.getType(),"Brightness ");
    }

    @Test
    void update() {
    }
}
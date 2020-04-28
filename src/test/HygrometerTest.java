import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HygrometerTest {

    @Test
    void getType() {
        Hygrometer h = new Hygrometer();
        assertEquals(h.getType(),"Humidity ");
    }

    @Test
    void update() {
    }
}
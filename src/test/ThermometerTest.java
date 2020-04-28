import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class ThermometerTest {

    @Test
    void getType() {
        Thermometer t = new Thermometer();
        Assertions.assertEquals(t.getType(), "Temperatur ");
    }

    @Test
    void update() {
    }
    }
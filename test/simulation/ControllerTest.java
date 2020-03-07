package simulation;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class ControllerTest {

    @Test
    void proceedMatchdayCountIncreased() {
        final int matchday = Controller.getMatchday();
        Controller.proceed();
        assertEquals(matchday + 1, Controller.getMatchday());
    }
}
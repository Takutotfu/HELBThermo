package com.example.helbthermo;

import org.junit.Before;
import org.junit.Test;

import static org.junit.jupiter.api.Assertions.assertTrue;

public class TemperatureDataParserTest {
    private final String simulFileName = "simul.data";

    private TemperatureDataParser parser;

    @Before
    public void setup() {
        parser = new TemperatureDataParser(simulFileName);
    }

    @Test
    public void testTemperatureDataParser() {
        while (parser.getCurrentIndex() < parser.getMaxIndex() - 1) {
            double currentTemperature = parser.getNextTemperature();
            assertTrue(currentTemperature >= 0 && currentTemperature <= 40);
        }
    }

}

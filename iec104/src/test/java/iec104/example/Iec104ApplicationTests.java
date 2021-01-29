package iec104.example;

import iec104.example.iec104.Iec104ConnectorHandler;
import iec104.example.iec104.Iec104ConnectorServer;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class Iec104ApplicationTests {

    @Test
    void contextLoads() {
    }

    public static void main(String[] args) {
        Iec104ConnectorHandler iec104ConnectorHandler = new Iec104ConnectorHandler();
        Iec104ConnectorServer iec104ConnectorServer = new Iec104ConnectorServer(iec104ConnectorHandler);

        iec104ConnectorServer.runSync();
    }
}

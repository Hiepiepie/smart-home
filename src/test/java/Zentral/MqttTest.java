package Zentral;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.PrintStream;
import org.apache.thrift.TException;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.junit.After;
import org.junit.AfterClass;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.ClassRule;
import org.junit.Test;
import org.testcontainers.containers.GenericContainer;

import static org.junit.Assert.*;


public class MqttTest implements MqttCallback {

  private static MqttClient publisher;
  private static MqttClient subscriber;

  private static ByteArrayOutputStream baos;
  private static PrintStream ps;
  private static PrintStream old;

  private String receivedMsg;

  @ClassRule
  public static GenericContainer mosquitto = new GenericContainer("eclipse-mosquitto");

  @BeforeClass
  public static void suiteSetup() throws MqttException {
    int port = mosquitto.getMappedPort(1883);
    String ipaddress = mosquitto.getHost();
    publisher = new MqttClient("tcp://"+ipaddress+":" +port, MqttClient.generateClientId());
    subscriber = new MqttClient("tcp://"+ipaddress+":" +port, MqttClient.generateClientId());
  }

  @AfterClass
  public static void suiteTeardown() {

  }

  @Before
  public void setUp() throws IOException, MqttException {
    baos = new ByteArrayOutputStream();
    ps = new PrintStream(baos);
    old = System.out;
    System.setOut(ps);

    MqttConnectOptions options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    publisher.connect(options);

    options = new MqttConnectOptions();
    options.setAutomaticReconnect(true);
    options.setCleanSession(true);
    options.setConnectionTimeout(10);
    subscriber.setCallback(this);
    subscriber.connect(options);
    subscriber.subscribe("Test");
  }

  @After
  public void tearDown() throws IOException, MqttException {
    System.out.flush();
    System.setOut(old);

    publisher.disconnect();
    subscriber.disconnect();
  }


  @Test
  public void publishMessageTest() throws TException, MqttException, InterruptedException {
    String sentMsg = "Test Message";
    MqttMessage  mqttMessage = new MqttMessage(sentMsg.getBytes());
    publisher.publish("Test", mqttMessage);
    Thread.sleep(5000);
    assertEquals(sentMsg,receivedMsg);
  }

  @Override
  public void connectionLost(Throwable throwable) {

  }

  @Override
  public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
    this.receivedMsg = new String(mqttMessage.getPayload());
    System.out.println("msg received");
  }

  @Override
  public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

  }
}

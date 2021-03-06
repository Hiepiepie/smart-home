package Zentral;

import java.io.File;
import java.io.IOException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;

import Zentral.JavaHTTPServer;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;


public class HTTPServerTest {

  MockSocket mockSocket;
  static ServerSocket serverSocket;
  String separator = File.separator;
  String PATH = "target" + separator+"classes" ;
  ProcessBuilder pbhttpServer;
  JavaHTTPServer httpServer;
  Zentral central;

  @Before
  public void setUp() throws IOException {
    //pbhttpServer = new ProcessBuilder("java", "-cp", PATH , "Zentral.JavaHTTPServer");
    mockSocket = new MockSocket();
    httpServer = new JavaHTTPServer(mockSocket);
  }

  @After
  public void tearDown() throws IOException {
    mockSocket.close();
  }

//  @Test
//  public void serverAcceptConnectionTest() throws IOException, InterruptedException {
//    // starts the `serverSocket`
//    Process process = pbhttpServer.inheritIO().start();
//
//
//    // create a `clientSocket` that will try to connect to a serverSocket
//    // that has the hostname 'localhost'
//    // and listens at port number 8080
//    Thread.sleep(1000);
//    try(Socket ableToConnect = new Socket(InetAddress.getLocalHost(), 8080)) {
//      assertTrue("Accepts connection when server in listening",
//          ableToConnect.isConnected());
//      // close the `clientSocket`
//    } catch (Exception e) {
//      System.out.println(e.getMessage());
//    }
//    Thread.sleep(1000);
//    //close the socket
//    process.destroy();
//    try {
//      // now that `serverSocket` is closed
//      // try to connect another `clientSocket` to the same `serverSocket`
//      new Socket(InetAddress.getLocalHost(), 8080);
//    } catch (Exception e) {
//      // assert that the exception is thrown and is the right exception
//      assertEquals("Connection refused", e.getMessage().trim());
  //  }
  //

  @Test
  public void serverAcceptRequestTest() throws IOException {
    String request = httpServer.getRequestFrom(mockSocket).readLine();
    assertEquals("GET / HTTP/1.1", request);
  }

  @Test
  public void serverSendsResponseTest() throws  IOException {
    httpServer.sendResponse(mockSocket,"test");
    String body = mockSocket.output();
    assertEquals("test", body);
  }

  @Test
  public void connectionTest() throws IOException, InterruptedException {
    int counter = 0;
    for (int i = 0; i < 10 ; i++){
      httpServer.sendResponse(mockSocket,"ping");
      String body = mockSocket.output();
      if (body.equals("ping"))  counter++;
      Thread.sleep(1000);
    }
    assertEquals(10, counter);
  }

}

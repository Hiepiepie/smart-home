package Zentral;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

public class MockSocket extends Socket {
  // create an empty list of bytes
  private List<Byte> bytesList = new ArrayList<>();

  public MockSocket() {
  }

  public InputStream getInputStream() {
    return new ByteArrayInputStream("GET / HTTP/1.1\nHost: localhost".getBytes());
  }

  public OutputStream getOutputStream() {
    return new OutputStream() {
      @Override
      // every time we call `write` (out.print),
      // we add the bytes to the list 'bytesList'
      public void write(int b) throws IOException {
        bytesList.add((byte) b);
      }
    };
  }

  // this method does not exist in the extended class 'Socket'
  // it is used to return the string formed by the bytes added to 'bytesList'
  public String output() {
    byte[] converted = toByteArray(bytesList);
    bytesList = new ArrayList<>();
    return new String(converted, StandardCharsets.UTF_8);
  }

  // convert a list of Bytes objects to an byte array
  private byte[] toByteArray(List<Byte> byteList) {
    byte[] byteArray = new byte[byteList.size()];
    int index = 0;
    for (byte b : byteList) {
      byteArray[index++] = b;
    }
    return byteArray;
  }
}
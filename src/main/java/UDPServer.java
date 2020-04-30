import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPServer {


    public static void main(String[] args) throws Exception
    {

        DatagramSocket ds = new DatagramSocket(1234);

        while (true){
            byte[] inServer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(inServer, inServer.length);
            ds.receive(dp);
            String str = new String(dp.getData());
            String ipAddress = String.valueOf(dp.getAddress());
            int port = dp.getPort();

            System.out.println("IP : " + ipAddress + " | Port : " + port + " | Type : " + str );


        }
    }
}

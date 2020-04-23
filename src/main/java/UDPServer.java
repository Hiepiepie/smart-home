
import java.net.DatagramSocket;
import java.net.DatagramPacket;
import java.net.InetAddress;

public class UDPServer {

    //    public UDPServer (){
//
//    }
//    public void run() {
//        try {
//
//            {
//                DatagramSocket ds = new DatagramSocket(1234);
//                while (true){
//                    byte[] inServer = new byte[1024];
//                    DatagramPacket dp = new DatagramPacket(inServer, inServer.length);
//                    ds.receive(dp);
//                    String str = new String(dp.getData());
//                    String ipAddress = String.valueOf(dp.getAddress());
//                    int port = dp.getPort();
//                    System.out.println("IP : " + ipAddress + " | Port : " + port + " | Type : " + str );
//
//                }
//
//            }
//        } catch( Exception e){
//            e.printStackTrace();
//        }
//    }
    public static void main(String[] args) throws Exception
    {
        DatagramSocket ds = new DatagramSocket(1234);
        //byte[] outServer= new byte[1024];
        while (true){
            byte[] inServer = new byte[1024];
            DatagramPacket dp = new DatagramPacket(inServer, inServer.length);
            ds.receive(dp);
            String str = new String(dp.getData());
            String ipAddress = String.valueOf(dp.getAddress());
            int port = dp.getPort();

            System.out.println("IP : " + ipAddress + " | Port : " + port + " | Type : " + str );

//            InetAddress IP = dp.getAddress();
//            str = "server :" + str.toUpperCase();
//            outServer = str.getBytes();
//            //send data back to Client
//            DatagramPacket sndPkt = new DatagramPacket(outServer, outServer.length, IP, port);
//            ds.send(sndPkt);

        }
    }
}

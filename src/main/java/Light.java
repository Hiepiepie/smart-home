import java.net.InetAddress;

public class Light extends UDPClient{

    public static void main(String[] args) throws Exception {
        int port = Integer.parseInt(args[0]);
        InetAddress ia = InetAddress.getByName(args[1]);
        Light light = new Light(port,ia);
        new Thread(light).start();
    }

    private int bright;

    public Light(int port, InetAddress ia) throws Exception{
        this.port = port;
        this.ia = ia;
        bright = rand.nextInt(101);
    }

    public int getBright() {
        return bright;
    }

    public String getType(){
        return "Brightness ";
    }

    public String update() {
        bright += (rand.nextBoolean() ? 1 : -1);
        if(bright >= 100) bright -= 1;
        if(bright <= 0) bright += 1;
        return bright + "%";
    }
}
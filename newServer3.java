import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.Map.*;

public class newServer3 {
    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(30000);
        while (true) {
            Socket s = ss.accept();
            socketList.add(s);
            new Thread(new ServerThread(s)).start();
        }
    }
}
class ServerThread implements Runnable {
    static int j = 0;
    static String[] clientsName = new String[15];
    public static Map clients = new HashMap<Socket, String>(30);
    Socket s = null;
    BufferedReader br = null;
    public ServerThread(Socket s) throws IOException {
        this.s = s;
        br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
    public void run() {
        try {
            String content;
            System.out.println(j);
            while ((content = readFromClient()) != null) {
                int x = 1;
                int y = newServer3.socketList.size();
                if (content.charAt(0) == '%') {
                    clientsName[j] = content;
                    clients.put(s, content.substring(1));
                    for (Socket socket : newServer3.socketList) {
                        PrintStream ps = new PrintStream(socket.getOutputStream());
                        if (x < y || x == 1) {
                            ps.println(clientsName[j]);
                            x++;
                        } else if (x == y) {
                            for (int i = 0; i <= j; i++) {
                                ps.println(clientsName[i]);
                            }
                        }
                    }
                }
                j++;
            }
            System.out.println("不再发送");
            clients.remove(s);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    private String readFromClient() {
        try {
            return  br.readLine();
        } catch (IOException e) {
            newServer3.socketList.remove(s);
        }
        return null;
    }
}

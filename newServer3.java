import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.Map.*;

public class newServer3 {
    public static List<Socket>socketList = Collections.synchronizedList(new ArrayList<>());
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
                //发送用户名
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
                //发判断收到文件，接收并将文件转发出去
                else if (content.charAt(0) == '$') {
                    String c = "";
                    int t = 1;
                    while ((content = readFromClient()) != null) {
                        if (t == 1) {
                            c = content;
                            t--;
                        } else {
                            c = c + "\n" + content;
                             System.out.println("c的内容是" + c);
                        }
                         if (content.charAt(0) == '#') {
                             break;
                         }else {
                        //System.out.print(content.charAt(0));
                        //System.out.println("没有结束");
                         }
                     }
                for (Socket socket : newServer3.socketList) {
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    System.out.println("有人发来媒体文件"+c);
                    ps.println("$");
                    ps.println(c);
                    ps.println("#");
                }
            } else {
                for (Socket socket : newServer3.socketList) {
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    ps.println(" " + clients.get(s) + " : " + content);
                }
            }
            j++;
        }
        System.out.println("不再发送");
        for (Socket socket : newServer3.socketList) {
            PrintStream ps = new PrintStream(socket.getOutputStream());
            ps.println("@" + clientsName[j - 1]);
        }
        clients.remove(s);

    } catch (IOException e) {
        e.printStackTrace();
        clients.remove(s);
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

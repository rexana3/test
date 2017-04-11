import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;
import java.util.Map.*;

public class newServer3 {
    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());
    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(30000);
        while(true){
            Socket s= ss.accept();
            String ip = s.getInetAddress().getHostAddress();
            socketList.add(s);
            new Thread(new ServerThread(s)).start();
        }
    }
}
class ServerThread implements Runnable {
    static int j=0;
    static String[] clientsName = new String[15];
    public static Map clients = new HashMap<Socket,String>(30);
    Socket s= null;
    BufferedReader br =null;
    public ServerThread(Socket s) throws IOException
    {
            this.s = s;
            br = new BufferedReader(new InputStreamReader(s.getInputStream()));
    }
    public void run() {
        try{
            String content = null;
            int flag  =0;
            while((content = readFromClient())!=null){
                for(Socket socket : newServer3.socketList){
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    if(content.charAt(0)=='%'){
                        String test =content.substring(1);
                        clientsName[j++]=content;
                        clients.put(s,test);
                       // ps.println(content);
                        for(int i=0;i<15&&clientsName[i]!=null;i++){
                            System.out.println(clientsName[i]+"上线了");
                            ps.println(clientsName[i]);
                        }
                    }
                    else if(content.charAt(0)=='#'){
                        System.out.println("有人发来媒体文件");
                        ps.println("#");
                    }
                    else{
                      ps.println(" "+clients.get(s)+" : "+content);
                    }

                }
            }
            System.out.println("不再发送");
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private String readFromClient(){
        try{
            return  br.readLine();

        }catch(IOException e){
            newServer3.socketList.remove(s);

        }
        return null;
    }
}





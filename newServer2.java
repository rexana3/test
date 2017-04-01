import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class newServer2 {
    public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());

    public static void main(String[] args) throws IOException {

        ServerSocket ss = new ServerSocket(30000);
        while(true){
            Socket s= ss.accept();
            String ip = s.getInetAddress().getHostAddress();
            System.out.println(ip+"......");
            socketList.add(s);
            new Thread(new ServerThread(s)).start();
        }

    }
}
class ServerThread implements Runnable {
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
            int flag  =97;
            while((content = readFromClient())!=null){
                //PrintStream ps = new PrintStream(socket.getOutputStream());
                for(Socket socket : newServer2.socketList){
                      PrintStream ps = new PrintStream(socket.getOutputStream());
                      if('a'==content.charAt(0)){
                            System.out.println("收到文件");
                            ps.println("a");
                        }
                       //PrintStream ps = new PrintStream(socket.getOutputStream());
                        else{
                             ps.println(content);
                         }
                }
            }
        }catch(IOException e){
            e.printStackTrace();
        }
    }
    private String readFromClient(){
        try{
            String temp = br.readLine();
            System.out.println(temp);

            return temp;
        }catch(IOException e){
            newServer2.socketList.remove(s);

        }
        return null;
    }

}





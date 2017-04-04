import java.io.*;
import java.net.*;
import java.util.*;
import java.util.List;

public class newServer3 {
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
            int flag  =0;
            while((content = readFromClient())!=null){
                for(Socket socket : newServer3.socketList){
                      PrintStream ps = new PrintStream(socket.getOutputStream());
                      if('a'==content.charAt(0)){
                            System.out.println("收到文件");
                            //实际上这里输出的只有a
                            ps.println(content);
                        }

                       }
                         ps.println(content);
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
            newServer3.socketList.remove(s);

        }
        return null;
    }

}





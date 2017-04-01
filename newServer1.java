import java.io.*;
import java.net.*;
import java.util.*;

import java.util.List;

public class newServer1 {
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

            while((content = readFromClient())!=null){
                //¸ÄÀàÃû
                for(Socket socket : newServer1.socketList){
                    PrintStream ps = new PrintStream(socket.getOutputStream());
                    ps.println(content);
                }
                //System.out.println(content);
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
            newServer1.socketList.remove(s);

        }
        return null;
    }

}





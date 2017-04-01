import java.io.*;
import java.net.*;
import java.util.*;


import java.util.List;

public class newServer {
	public static List<Socket> socketList = Collections.synchronizedList(new ArrayList<>());
	//public static String ip;
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
				for(Socket socket : newServer.socketList){
					PrintStream ps = new PrintStream(socket.getOutputStream());
					//String ip = socket.getInetAddress().getHostAddress();
					String ip = s.getInetAddress().toString();
					//ps.println(ip+"หตฃบ"+content);
					ps.println(content);

				}
			}
		}catch(IOException e){
			e.printStackTrace();
		}
	}
	private String readFromClient(){
		try{
			return br.readLine();
			
		}catch(IOException e){
			newServer.socketList.remove(s);
			
		}
		return null;
	}

}
		
		



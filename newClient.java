import java.awt.BorderLayout;
import java.awt.Window.Type;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.*;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.Socket;
import java.io.*;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.net.*;

import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import javax.swing.Box;

public class newClient{
	JFrame frame = new JFrame("mychat");

	JPanel contentPane = new JPanel();
	//JScrollPane srocllpane = new JScrollPane();

	JTextField textField =  new JTextField(50);
	JTextArea textArea  = new JTextArea(20,50);


	String message;
	Socket socket;
	PrintStream writer;
	BufferedReader reader;
	String ip; 

	public static void main(String[] args)throws Exception{
		newClient nc =new newClient();
		nc.connectServer();
		
	}

	public newClient(){
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(100, 100, 450, 300);
		textArea.setEditable(false);
		JScrollPane jsp = new JScrollPane(textArea);
		contentPane.add(jsp);
		contentPane.add(textField);
		frame.add(jsp,BorderLayout.NORTH);
		frame.add(contentPane, BorderLayout.SOUTH);
		
		
		
		frame.pack();
		frame.setVisible(true);
		//键盘监听
		textField.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				if(e.getKeyCode()==KeyEvent.VK_ENTER)
				send();
			}
		});
	
		
	
	}
	
	//执行发送
	public void send(){
		
		message = textField.getText().trim();
		try{
			writer = new PrintStream(socket.getOutputStream());
			
				message = textField.getText().trim();
				ip= InetAddress.getLocalHost().getHostAddress();
				//textArea.append(ip);
				//textArea.append(ip+"\n"+message+"\n");
				textField.setText(null);	
				writer.println(message);
		
		}catch(Exception e){
		}
	}

	//发送消息到服务端
	//public viod sendMessage(String message){
	//	writer.println(message);
	//	writer.flush();
	//}
		//连接服务端
	public void connectServer() throws Exception,IOException{
		socket = new Socket("192.168.0.108",30000);
		new Thread(new ClientThread(socket,textArea)).start();
		writer = new PrintStream(socket.getOutputStream());
		
			
	}
}
class ClientThread implements Runnable{
	JTextArea textArea;
	private Socket s;
	BufferedReader br =null;
	String content = null;
	public ClientThread(Socket s,JTextArea textArea) throws IOException{
		this.textArea = textArea;
		this.setS(s);
		br = new BufferedReader(new InputStreamReader(s.getInputStream()));
		
	}
	public void run(){
		try{
			while((content =br.readLine())!=null){
				System.out.println(content);
				textArea.append(content+"\n");
			}
		}catch(Exception e){
			e.printStackTrace();
		
		}
	}
	public Socket getS() {
		return s;
	}
	public void setS(Socket s) {
		this.s = s;
	}
}
		
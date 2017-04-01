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
import java.awt.event.*;

public class newClientd{
	JFrame frame = new JFrame("我的数智");

	JPanel contentPane = new JPanel();
	//JScrollPane srocllpane = new JScrollPane();

	JTextField textField =  new JTextField(50);
	JTextArea textArea  = new JTextArea(20,50);

	
  	JFrame frame1 = new JFrame("输入你的用户名");
	JPanel pane1= new JPanel();
	JTextField textField1 = new JTextField(20);
	JButton button1 = new JButton("确认");

	String name ;
	String message;
	Socket socket;
	PrintStream writer;
	BufferedReader reader;
	String ip; 
	

	public static void main(String[] args)throws Exception{
		

		InetAddress addr = InetAddress.getLocalHost();

		String dip=addr.getHostAddress().toString();
		newClientd nc =new newClientd();
		nc.login(dip);
		
		nc.connectServer();
		
	}
	//登陆界面，写入用户名。点击按钮获取用户名并且隐藏窗口,并且启动聊天框；
	public  void login(String dip)throws Exception{
	
		frame1.setBounds(500, 300, 1000, 100);
		frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);

		pane1.add(textField1,BorderLayout.WEST);
		pane1.add(button1,BorderLayout.EAST);

		frame1.add(pane1);
		
		frame1.pack();
		frame1.setVisible(true);
		button1.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				if(textField1.getText()!=null){
					name =textField1.getText();
				
					if(dip =="192.168.0.108"){
						name = "我是小女王";
					}
					else System.out.println(dip);	
				}
				frame1.setVisible(false);
				chatDraw();
					
			}
		});
	
	}

	public void chatDraw(){
		
		
		frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
		frame.setBounds(350, 100, 450, 300);
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
				textField.setText(null);	
				writer.println("  "+name+"："+message);
		
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
		
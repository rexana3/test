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

import javax.swing.JOptionPane.*;
import javax.swing.*;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextArea;
import javax.swing.JTextField;
import javax.swing.border.EmptyBorder;
import java.awt.event.*;

public class newClient4{
    //聊天界面设计
    JFrame frame = new JFrame("我的数智");
    JFrame frameChoose = new JFrame("选择你要发送的文件");
    JPanel contentPane = new JPanel();
    JPanel contentPane1 = new JPanel();
    JTextField textField =  new JTextField(50);
    JTextArea textArea  = new JTextArea(20,50);
    JButton button = new JButton("发送文件");

    //登陆界面设计
    JFrame frame1 = new JFrame("输入你的用户名");
    JPanel pane1= new JPanel();
    JTextField textField1 = new JTextField(16);
    JButton button1 = new JButton("确认");

    String name ;
    String message;
    Socket socket;
    PrintStream writer;
    BufferedReader reader;
    String ip;


    String fileString = null;
    String filePath =null;
    public static void main(String[] args)throws Exception{
        newClient3 nc =new newClient4();
        nc.login();

        nc.connectServer();
    }
    //登陆界面，写入用户名。点击按钮获取用户名并且隐藏窗口,并且启动聊天框；
    public void login(){
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
                }
                frame1.setVisible(false);
                chatDraw();
            }
        });
    }
    //聊天界面
    public void chatDraw(){
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(350, 100, 450, 300);
        textArea.setEditable(false);
        JScrollPane jsp = new JScrollPane(textArea);
        contentPane.add(textField,BorderLayout.WEST);
        contentPane.add(button,BorderLayout.EAST);
        frame.add(jsp,BorderLayout.NORTH);
        frame.add(contentPane, BorderLayout.SOUTH);

        frame.pack();
        frame.setVisible(true);
        //键盘监听
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    send();
                }
            }
        });
        //按钮监听
        button.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                choose();
            }
        });
    }

    public void choose (){
        //String fileString = null;
        //String filePath =null;
        frameChoose.setBounds(300,100,200,200);
        JButton buttonChoose = new JButton("发送");
        JButton buttonOpen  = new JButton("打开");
        JTextArea textChoose = new JTextArea(20,20);
        JTextField textFieldChoose = new JTextField(20);
        JPanel panelChoose = new JPanel();
        panelChoose.add(textFieldChoose,BorderLayout.WEST);
        panelChoose.add(buttonChoose,BorderLayout.EAST);
        panelChoose.add(buttonOpen,BorderLayout.CENTER);
        JScrollPane jspChoose = new JScrollPane(textChoose);
        frameChoose.add(jspChoose,BorderLayout.SOUTH);
        frameChoose.add(panelChoose,BorderLayout.NORTH);

        //设置单行框有默认路径
        //textFieldChoose.setText("e:\\11.txt");

        //当按打开按钮时获取输入的路径
        buttonOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //清空单行框
                textChoose.setText("");
                //获取单行框的内容
                fileString = textFieldChoose.getText();
                //System.out.println(fileString);
                File file =  new File(fileString);
                String fileNames[] = file.list();
                for(String fileName:fileNames){
                    textChoose.append(fileName+"\n");
                }
            }
        });
        //点击发送按钮
        buttonChoose.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    writer = new PrintStream(socket.getOutputStream());
                    filePath = textFieldChoose.getText();
                     //BufferedReader in
                       //  = new BufferedReader(new FileReader(filePath));
                    //FileReader fr = new FileReader(filePath);
                    BufferedReader br = new BufferedReader(new FileReader(filePath));

                    String line = null;
                    while ((line = br.readLine()) != null) {

                    //将文本打印到控制台
                        //System.out.println(line);
                        writer.println(line);

                    }
                    //System.out.println("12");
/*                    int ch = 0;
                    while((ch = fr.read())!= -1){
                        writer.print((char)ch);
                    }
                    writer.print((char)ch);
                    System.out.println(filePath);*/

                    // char[] buf = new char[1024];
                    // int len = 0;
                    // while((len = in.read(buf)) != -1){
                    //     System.out.print(new String(buf, 0, len));
                    //     writer.print(new String(buf,0,len));
                    //}
                }catch(Exception ex){
                }
            }
        });

        frameChoose.pack();
        frameChoose.setVisible(true);
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

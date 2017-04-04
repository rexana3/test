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

public class newClient5{
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
    //保存文件
    JFrame frameSave = new JFrame("选择你要发送的文件");
    String name ;
    String message;
    Socket socket;
    PrintStream writer;
    BufferedReader reader;
    String ip;


    String fileString = null;
    String filePath =null;
    public static void main(String[] args)throws Exception{
        newClient5 nc =new newClient5();
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
        textFieldChoose.setText("e:\\11.txt");

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
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    writer.println("a");
                    String line = null;
                    while ((line = br.readLine()) != null) {
                            writer.println(line);
                    }
                    frameChoose.setVisible(false);
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
 public void saveFile(String content){
        frameSave.setBounds(900,100,200,200);
        JPanel panelSave =new JPanel();
        JTextArea textAreaSave = new JTextArea(20,30);
        JTextField textFieldSave = new JTextField(20);
        JButton buttonOpen1 = new JButton("打开");
        JButton buttonSave = new JButton("保存");
        JScrollPane jspSave = new JScrollPane(textAreaSave);
        panelSave.add(textFieldSave,BorderLayout.WEST);
        panelSave.add(buttonOpen1,BorderLayout.EAST);
        panelSave.add(buttonSave,BorderLayout.CENTER);
        frameSave.add(jspSave,BorderLayout.SOUTH);
        frameSave.add(panelSave,BorderLayout.NORTH);
        textFieldSave.setText("e:\\12.txt");
        frameSave.pack();
        frameSave.setVisible(true);
        buttonOpen1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String fileSavePath;
                textAreaSave.setText("");
                fileSavePath = textFieldSave.getText();
                //System.out.println(fileString);
                File file =  new File(fileSavePath);
                String fileNames[] = file.list();
                for(String fileName:fileNames){
                    textAreaSave.append(fileName+"\n");
                }
            }
        });
        //保存按键的监听
        buttonSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                try{
                    String fileSavePath  = textFieldSave.getText();
                    BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSavePath)));
                    bufw.write(content);
                    System.out.println("save:"+content);
                    bufw.newLine();
                    bufw.flush();

                    frameSave.setVisible(false);
                }catch(Exception ex){

                }
            }
        });
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
            content =br.readLine();
            System.out.println("xsss");
            if('a'==content.charAt(0)){
                    String st = "";
                    System.out.println("进入判断");
                    while(true){
                        content =br.readLine();
                        System.out.println("进入第二次循环");
                        st+=content;
                        if(content==null) {
                            System.out.println(s);
                            new newClient5().saveFile(st);
                            break;
                        }
                    }
                    s.shutdownOutput();
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

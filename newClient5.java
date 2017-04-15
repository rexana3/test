import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.JList.*;
import javax.swing.JTree.*;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.*;
import java.util.*;

public class newClient5 {
    //登陆界面设计
    JFrame      frame1      = new JFrame("输入你的用户名");
    JPanel      pane1       = new JPanel();
    JTextField  textField1 = new JTextField(16);
    JButton     button1     = new JButton("确认");

    // 聊天界面设计
    static JFrame     frame        = new JFrame("我的数智");
    JFrame     frameChoose  = new JFrame("选择你要发送的文件");
    JPanel     contentPane  = new JPanel();
    static JPanel     contentPane1 = new JPanel();
    JTextField textField    = new JTextField(50);
    JTextArea  textArea     = new JTextArea(20, 50);
    JButton    button        = new JButton("发送文件");
    JScrollPane jsp            = new JScrollPane(textArea);
    static JTree      tree;
    static DefaultMutableTreeNode  online = new  DefaultMutableTreeNode ("在线");
    static DefaultTreeModel dt = new DefaultTreeModel(online);

    //选择文件发送界面
    JButton buttonChoose        = new JButton("发送");
    JButton buttonOpen          = new JButton("打开");
    JTextArea textChoose        = new JTextArea(20, 20);
    JTextField textFieldChoose = new JTextField(20);
    JPanel panelChoose         = new JPanel();
    JScrollPane jspChoose = new JScrollPane(textChoose);

    //保存文件界面
    JFrame          frameSave = new JFrame("选择你要发送的文件");
    JPanel panelSave = new JPanel();
    JTextArea textAreaSave = new JTextArea(20, 30);
    JTextField textFieldSave = new JTextField(20);
    JButton buttonOpen1 = new JButton("打开");
    JButton buttonSave = new JButton("保存");
    JScrollPane jspSave = new JScrollPane(textAreaSave);

    static String          fileSavePath;
    static String          name;
    static String          myName;
    static String          message;
    Socket          socket;
    PrintStream     writer;
    BufferedReader  reader;
    String           ip;

    String fileString   = null;
    String filePath     = null;
    static int j = 0;

    public static void main(String[] args)throws Exception {
        newClient5 nc = new newClient5();
        nc.login();
        nc.connectServer();
    }
    /*
    *函数名：login
    *作用：登陆界面，输入入用户名
    *点击按钮获取用户名
    *隐藏窗口和启动聊天框
    *@return void
    */
    public void login() {
        tree = new JTree(dt);
        frame1.setBounds(500, 300, 1000, 100);
        frame1.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        pane1.add(textField1, BorderLayout.WEST);
        pane1.add(button1, BorderLayout.EAST);
        frame1.add(pane1);
        frame1.pack();
        frame1.setVisible(true);
        //按钮监听
        button1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (textField1.getText() != null) {
                    name = "%" + textField1.getText();
                    //用户名发送给服务端
                    System.out.println("登陆界面的用户名" + name);
                    send(name);
                }
                frame1.setVisible(false);
                chatDraw();
            }
        });
    }
    //在线列表的初始化。
    /*    public  static void nameTree () {
            String xx =null;
            System.out.println("树" + name);
            DefaultMutableTreeNode  online = new  DefaultMutableTreeNode ("在线小伙伴");
                 String  perName= ClientThread.saveName(myName, 0);
                 while(xx!=perName){
                     DefaultMutableTreeNode  tName = new  DefaultMutableTreeNode (name);
                     online.add(tName);
                     xx=perName;
                }
                      tree = new JTree(online);
               }*/

    /*
    *函数名：chatDraw
    *初始化聊天框界面
    *设置键盘监听，按回车发送消息
    *设置按钮监听，发送文件
    *@return void
    */
    public void chatDraw() {
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setBounds(350, 100, 450, 300);
        textArea.setEditable(false);
        contentPane.add(textField, BorderLayout.WEST);
        contentPane.add(button, BorderLayout.EAST);
        contentPane1.add(jsp, BorderLayout.WEST);
        contentPane1.add(new JScrollPane(tree), BorderLayout.EAST);
        contentPane1.add(jsp, BorderLayout.WEST);
        frame.add(contentPane1, BorderLayout.NORTH);
        frame.add(contentPane, BorderLayout.SOUTH);
        frame.pack();
        frame.setVisible(true);
        //键盘监听,调用send方法发送消息
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    message = textField.getText().trim();
                    send(message);
                    textField.setText(null);
                }
            }
        });
        //按钮监听调用choose方法让用户选择文件
        button.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                choose();
            }
        });
    }
    /*choose
    *选择发送的文件
    *
    */
    public void choose () {
        frameChoose.setBounds(300, 100, 200, 200);
        JButton buttonChoose        = new JButton("发送");
        JButton buttonOpen          = new JButton("打开");
        JTextArea textChoose        = new JTextArea(20, 20);
        JTextField textFieldChoose = new JTextField(20);
        JPanel panelChoose         = new JPanel();
        panelChoose.add(textFieldChoose, BorderLayout.WEST);
        panelChoose.add(buttonChoose, BorderLayout.EAST);
        panelChoose.add(buttonOpen, BorderLayout.CENTER);
        JScrollPane jspChoose = new JScrollPane(textChoose);
        frameChoose.add(jspChoose, BorderLayout.SOUTH);
        frameChoose.add(panelChoose, BorderLayout.NORTH);

        //设置单行框有默认路径
        textFieldChoose.setText("e://1.txt");

        //当按打开按钮时获取输入的路径
        buttonOpen.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //清空单行框
                textChoose.setText("");
                //获取单行框的内容
                fileString = textFieldChoose.getText();
                File file =  new File(fileString);
                String fileNames[] = file.list();
                for (String fileName : fileNames) {
                    textChoose.append(fileName + "\n");
                }
            }
        });
        //点击发送按钮,此时就把文件发送出去了。
        buttonChoose.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    writer = new PrintStream(socket.getOutputStream());
                    filePath = textFieldChoose.getText();
                    BufferedReader br = new BufferedReader(new FileReader(filePath));
                    writer.println("$");
                    String line = null;
                    while ((line = br.readLine()) != null) {
                        writer.println(line);
                        System.out.println("发出去的文件内容" + line);
                    }
                    System.out.println("......结束.....");
                    writer.println("#");
                    writer.flush();
                    frameChoose.setVisible(false);
                } catch (Exception ex) {
                }
            }
        });

        frameChoose.pack();
        frameChoose.setVisible(true);
    }
    /*
    *函数名：send
    *执行消息的发送
    *
    *@return void
    */
    public void send(String message) {
        try {
            writer = new PrintStream(socket.getOutputStream());
            writer.println(message);
        } catch (Exception e) {
        }
    }
    /*
    *函数名：saveFile
    *保存文件的图形界面以及按钮的监听
    */
    public void saveFile(String content) {
        frameSave.setBounds(900, 100, 200, 200);

        panelSave.add(textFieldSave, BorderLayout.WEST);
        panelSave.add(buttonOpen1, BorderLayout.EAST);
        panelSave.add(buttonSave, BorderLayout.CENTER);
        frameSave.add(jspSave, BorderLayout.SOUTH);
        frameSave.add(panelSave, BorderLayout.NORTH);
        frameSave.pack();
        frameSave.setVisible(true);

        textFieldSave.setText("e:\\11.txt");
        //打开按钮监听
        buttonOpen1.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                textAreaSave.setText("");
                fileSavePath = textFieldSave.getText();
                File file =  new File(fileSavePath);
                String fileNames[] = file.list();
                //显示目录
                for (String fileName : fileNames) {
                    textAreaSave.append(fileName + "\n");
                }
            }
        });
        //保存按键的监听
        buttonSave.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                try {
                    fileSavePath  = textFieldSave.getText();
                    BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileSavePath)));
                    bufw.write(content);
                    bufw.newLine();
                    bufw.flush();
                    frameSave.setVisible(false);
                } catch (Exception ex) {
                }
            }
        });
    }
    public void connectServer() throws Exception, IOException {
        socket = new Socket("127.0.0.1", 30000);
        new Thread(new ClientThread(socket, textArea, tree, online, j, dt)).start();
        writer = new PrintStream(socket.getOutputStream());
    }
}
class ClientThread implements Runnable {
    Enumeration<?> enumeration;
    DefaultTreeModel dt;
    static int j;
    static String[] namelist = new String[15];
    static String myName = null;
    JFrame frame;
    JPanel     contentPane1;
    JTextArea textArea;
    static JTree tree;
    static DefaultMutableTreeNode  online ;
    private Socket s;
    FileInputStream fis;
    FileOutputStream fos;
    String content = null;
    String rMessage ;
    BufferedReader rbr = null;
    byte[] buf = new byte[100];
    char rflag;
    public ClientThread(Socket s, JTextArea textArea, JTree tree, DefaultMutableTreeNode  online, int j, DefaultTreeModel dt) throws IOException {
        this.textArea = textArea;
        this.setS(s);
        this.tree = tree;
        this.online = online;
        this.j = j;
        this.dt = dt;
        rbr = new BufferedReader(new InputStreamReader(s.getInputStream()));

    }
    public void run() {
        try {

            while ((content = rbr.readLine()) != null) {
                while (true) {
                    rflag = content.charAt(0);
                    System.out.println(rflag);
                    break;
                }
                if (rflag == '$') {
                    String tx = "";
                    int e = 0;
                    System.out.println(".......收到文件,请求保存......");
                    // new newClient5().saveFile(saveTxt());
                    while ((content = rbr.readLine()) != null) {
                        System.out.println("收到的文件" + content);
                        if (content.charAt(0) == '#') {
                            break;
                        }
                        if (e == 0) {
                            tx = content;
                            e--;
                        } else {
                            tx = tx + "\r\n" + content;
                        }


                    }
                    System.out.println("读取完毕");

                    new newClient5().saveFile(tx);
                    System.out.println("txtwenjian" + tx);
                    //保存名字
                } else if (rflag == '%') {
                    String temp = content.substring(1);
                    namelist[j++] = temp;
                    DefaultMutableTreeNode  ssss = new  DefaultMutableTreeNode (namelist[j - 1]);
                    online.add(ssss);
                    dt.reload();
                    tree = new JTree(online);
                    tree.setModel(dt);
                    System.out.println("......");
                } else if (rflag == '@') {
                    enumeration = online.children(); //遍历该节点的所有子节点.
                    //知道了遍历方式，开始遍历.
                    while (enumeration.hasMoreElements()) { //遍历枚举对象.
                        //先定义一个节点变量.
                        DefaultMutableTreeNode node;
                        node = (DefaultMutableTreeNode) enumeration.nextElement(); //将节点名称给node.
                        String name = node.toString();
                        System.out.println(name);
                        if (name.equals(content.substring(2))) {
                            dt.removeNodeFromParent(node);

                        }
                    }
                } else {
                    System.out.println("文本消息" + rflag);
                    saveMessage(content);
                }
            }

        } catch (Exception e) {
            System.out.println("发生异常");
            e.printStackTrace();
        }
    }
    /*saveMp3
    *保存非文本文件，用字符流读取和输出
    *@param 将要保存的地址
    *@return
    */
    public void saveMp3(String outPath) {
        try {
            FileOutputStream fos = new FileOutputStream(outPath);
            byte[] buf = new byte[1024];
            byte[] flag = new byte[2];
            flag[0] = '*';
            flag[1] = '*';
            int len = 0;
            while ((len = fis.read(buf)) != -1) {
                fos.write(buf, 0, len);
            }
            fos.write(flag, 0, 1);
            fos.close();
        } catch (Exception e) {}

    }
    /*saveTxt
    *读取文本文件
    *@param 将要保存的地址
    *@return
    */
    public String saveTxt() {
        String txt = "";
        try {
            int len = 0;
            System.out.println("文件读取完毕");


        } catch (Exception e) {
            System.out.println("保存文件出异常");
        }
        return txt;
    }
    /*saveMeaasge
    *收发聊天消息，并将消息存储在本地文件中
    *@param
    *@return
    */
    public void saveMessage(String test) {
        try {
            textArea.append(test + "\n");
            BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
            String content;
            while ((content = br.readLine()) != null) {
                textArea.append(content + "\n");
            }
        } catch (Exception e) {
        }
    }
    /*saveMp3
    *保存用户名
    *@param
    *@return name
    */
    /*    public static String saveName(String content, int i) {
            try {
                System.out.println("保存名字" + content.substring(i));
                 myName = content.substring(i);
                 System.out.println("成功保存名字");
            } catch (Exception e) {
                System.out.println("保存名字又异常");
            }
            return myName;
        }*/
    public Socket getS() {
        return s;
    }
    public void setS(Socket s) {
        this.s = s;
    }
}

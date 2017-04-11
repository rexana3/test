import java.awt.event.*;
import java.awt.*;
import java.io.*;
import java.net.*;
import javax.swing.*;
import javax.swing.JList.*;
import javax.swing.JTree.*;
import javax.swing.tree.DefaultMutableTreeNode;

public class newClient6 implements Runnable {
    //登陆界面设计
    JFrame      loginFrame  = new JFrame("输入你的用户名");
    JPanel      loginPane1       = new JPanel();
    JTextField  logintextField = new JTextField(16);
    JButton     loginbutton     = new JButton("确认");

    // 聊天界面设计
    JFrame     chatFrame        = new JFrame("我的数智");
    JPanel     chatTopPanel  = new JPanel();
    JPanel     chatUnderPanel = new JPanel();
    JPanel     chatButtonPanel  = new JPanel();
   JTextArea  chatSendTextArea    = new JTextArea(10,50);
    JTextArea  chatShowTextArea     = new JTextArea(20, 50);
    JButton    chatSendFileButton        = new JButton("发送文件");
    JButton    chatSendMeaasgeButton  = new JButton("发送");
    JButton    chatSendPictureButton  = new JButton("发送图片");
    //JScrollPane jsp            = new JScrollPane(textArea);
     JTree      chatTree;
     DefaultMutableTreeNode  online = new  DefaultMutableTreeNode ("在线小伙伴");


    //选择文件发送界面
    JFrame     chatChooseFrame  = new JFrame("选择你要发送的文件");
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

    static String          fileSavePath;//文件保存路径
    static String          name;//用户名
    static String          message;//聊天消息
    Socket          socket;
    PrintStream     writer;
    BufferedReader  reader;
    String           ip;

    String fileString   = null;
    String filePath     = null;
   
    FileInputStream fis;
    FileOutputStream fos;
    String content = null;
    String myName;
    String rMessage ;
    byte[] buf = new byte[100];
    char rflag;
    reader = new BufferedReader(new InputStreamReader(s.getInputStream()));


    public static void main(String[] args)throws Exception {
        newClient6 nc = new newClient6();
        nc.login();
        nc.connectServer();
    }

    public void login() {
        loginFrame.setBounds(500, 300, 1000, 100);
        loginFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        loginPane1.add(logintextField, BorderLayout.WEST);
        loginPane1.add(loginbutton, BorderLayout.EAST);
        loginFrame.add(loginPane1);
        loginFrame.pack();
        loginFrame.setVisible(true);
        //按钮监听
        loginbutton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                if (logintextField.getText() != null) {
                    name = "%" + logintextField.getText();
                    //用户名发送给服务端
                    System.out.println("登陆界面的用户名" + name);
                    send(name);
                   // nameTree();
                }
                loginFrame.setVisible(false);
                chatDraw();
            }
        });
    }
    /*
    *函数名：chatDraw
    *初始化聊天框界面
    *设置键盘监听，按回车发送消息
    *设置按钮监听，发送文件
    *@return void
    */
    public void chatDraw() {
        chatFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        chatFrame.setBounds(350, 100, 450, 300);
        chatShowTextArea.setEditable(false);
        chatTopPanel.add(new JScrollPane(chatShowTextArea), BorderLayout.EAST);
        chatTopPanel.add(new JScrollPane(chatTree), BorderLayout.EAST);
        chatButtonPanel.add(chatSendFileButton, BorderLayout.WEST);
        chatButtonPanel.add(chatSendPictureButton,BorderLayout.EAST);
        chatUnderPanel.add(chatShowTextArea,BorderLayout.WEST);
        chatUnderPanel.add(chatSendMeaasgeButton,BorderLayout.EAST);
        chatFrame.add(chatTopPanel, BorderLayout.NORTH);
        chatFrame.add(chatButtonPanel, BorderLayout.CENTER);
        chatFrame.add(chatUnderPanel, BorderLayout.SOUTH);
        chatFrame.pack();
        chatFrame.setVisible(true);
        //键盘监听,调用send方法发送消息
        chatSendTextArea.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    message = chatSendTextArea.getText().trim();
                    send(message);
                    chatSendTextArea.setText(null);
                }
            }
        });
        //按钮监听调用choose方法让用户选择文件
        chatSendFileButton.addActionListener(new ActionListener() {
            public void actionPerformed(ActionEvent e) {
                //choose();
            }
        });
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
 public void connectServer() throws Exception, IOException {
     socket = new Socket("127.0.0.1", 30000);
    newThread( new newClient6(socket)).start();
     writer = new PrintStream(socket.getOutputStream());
 }

     public void run() {
         try {
             private Socket s;
            char rflag;
             while ((content = reader.readLine()) != null) {
                 System.out.println("开始获取第一个字符");
                 while (true) {
                     rflag = content.charAt(0);
                     System.out.println("收到的消息" + content + "content的第一个字符" + rflag);
                     break;
                 }
                 System.out.println("判断是什么类型" + rflag);
                 if (rflag == '#') {
                     saveMp3(newClient6.fileSavePath);
                 } else if (rflag == '$') {
                     saveTxt(newClient6.fileSavePath);
                 } else if (rflag == '%') {
                     System.out.println("名字的第一个字符" + rflag);
                     saveName(content, 1);
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
             int len = 0;
             while ((len = fis.read(buf)) != -1) {
                 fos.write(buf, 0, len);
             }
             fos.write(flag, 0, 1);
             fos.close();
         } catch (Exception e) {}

     }
     /*saveTxt
     *保存文本文件，用字符流读取和输出
     *@param 将要保存的地址
     *@return
     */
     public void saveTxt(String outPath) {
         try {
             FileOutputStream fos = new FileOutputStream(outPath);
             byte[] buf = new byte[1024];
             byte[] flag = new byte[2];
             flag[0] = '*';
             int len = 0;
             while ((len = fis.read(buf)) != -1) {
                 fos.write(buf, 0, len);
             }
             fos.write(flag, 0, 1);

             fos.close();
         } catch (Exception e) {

         }
     }
     /*saveMeaasge
     *收发聊天消息，并将消息存储在本地文件中
     *@param
     *@return
     */
     public void saveMessage(String test) {
         try {
             chatShowTextArea.append(test + "\n");
             BufferedReader br = new BufferedReader(new InputStreamReader(s.getInputStream()));
             String content;
             while ((content = br.readLine()) != null) {
                 chatShowTextArea.append(content + "\n");
             }
         } catch (Exception e) {
         }
     }
     /*saveMp3
     *保存用户名
     *@param
     *@return name
     */
     public String saveName(String content, int i) {
         try {
             System.out.println("保存名字" + content.substring(i));
             myName = content.substring(i);
         } catch (Exception e) {
         }
         return myName;
     }
 }

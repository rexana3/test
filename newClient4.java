BufferedWriter;
public class newClient4{
    //ÁÄÌì½çÃæÉè¼Æ
    JFrame frame = new JFrame("ÎÒµÄÊýÖÇ");
    JFrame frameChoose = new JFrame("Ñ¡ÔñÄãÒª·¢ËÍµÄÎÄ¼þ");
    JPanel contentPane = new JPanel();
    JPanel contentPane1 = new JPanel();
    JTextField textField =  new JTextField(50);
    JTextArea textArea  = new JTextArea(20,50);
    JButton button = new JButton("·¢ËÍÎÄ¼þ");

    //µÇÂ½½çÃæÉè¼Æ
    JFrame frame1 = new JFrame("ÊäÈëÄãµÄÓÃ»§Ãû");
    JPanel pane1= new JPanel();
    JTextField textField1 = new JTextField(16);
    JButton button1 = new JButton("È·ÈÏ");
    //·¢ËÍÎÄ¼þ½çÃæ

    //±£´æÎÄ¼þ½çÃæ
    JFrame frameSave = new JFrame("Ñ¡ÔñÄãÒª±£´æµÄÂ·¾¶");

    String name ;
    String message;
    Socket socket;
    PrintStream writer;
    BufferedReader reader;
    String ip;

    String fileString = null;
    String filePath =null;
    public static void main(String[] args)throws Exception{
        newClient4 nc =new newClient4();
        nc.login();

        nc.connectServer();
    }
    //µÇÂ½½çÃæ£¬Ð´ÈëÓÃ»§Ãû¡£µã»÷°´Å¥»ñÈ¡ÓÃ»§Ãû²¢ÇÒÒþ²Ø´°¿Ú,²¢ÇÒÆô¶¯ÁÄÌì¿ò£»
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
    //ÁÄÌì½çÃæ
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
        //¼üÅÌ¼àÌý
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if(e.getKeyCode()==KeyEvent.VK_ENTER){
                    send();
                }
            }
        });
        //°´Å¥¼àÌý
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
        JButton buttonChoose = new JButton("·¢ËÍ");
        JButton buttonOpen  = new JButton("´ò¿ª");
        JTextArea textChoose = new JTextArea(20,20);
        JTextField textFieldChoose = new JTextField(20);
        JPanel panelChoose = new JPanel();
        panelChoose.add(textFieldChoose,BorderLayout.WEST);
        panelChoose.add(buttonChoose,BorderLayout.EAST);
        panelChoose.add(buttonOpen,BorderLayout.CENTER);
        JScrollPane jspChoose = new JScrollPane(textChoose);
        frameChoose.add(jspChoose,BorderLayout.SOUTH);
        frameChoose.add(panelChoose,BorderLayout.NORTH);

        //ÉèÖÃµ¥ÐÐ¿òÓÐÄ¬ÈÏÂ·¾¶
        textFieldChoose.setText("e:\\11.txt");

        //µ±°´´ò¿ª°´Å¥Ê±»ñÈ¡ÊäÈëµÄÂ·¾¶
        buttonOpen.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                //Çå¿Õµ¥ÐÐ¿ò
                textChoose.setText("");
                //»ñÈ¡µ¥ÐÐ¿òµÄÄÚÈÝ
                fileString = textFieldChoose.getText();
                //System.out.println(fileString);
                File file =  new File(fileString);
                String fileNames[] = file.list();
                for(String fileName:fileNames){
                    textChoose.append(fileName+"\n");
                }
            }
        });
        //µã»÷·¢ËÍ°´Å¥
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
    public void saveFile(){
        //±£´æÎÄ¼þ½çÃæµÄÉè¼Æ
       // JFrame frameSave = new JFrame("Ñ¡ÔñÄãÒª±£´æµÄÂ·¾¶");
        frameSave.setBounds(900,100,200,200);
        JPanel panelSave =new JPanel();
        JTextArea textAreaSave = new JTextArea(20,30);
        JTextField textFieldSave = new JTextField(20);
        JButton buttonOpen1 = new JButton("´ò¿ª");
        JButton buttonSave = new JButton("±£´æ");
        JScrollPane jspSave = new JScrollPane(textAreaSave);
        panelSave.add(textFieldSave,BorderLayout.WEST);
        panelSave.add(buttonOpen1,BorderLayout.EAST);
        panelSave.add(buttonSave,BorderLayout.CENTER);
        frameSave.add(jspSave,BorderLayout.SOUTH);
        frameSave.add(panelSave,BorderLayout.NORTH);
        textFieldSave.setText("e:\\11.txt");

        //µ±°´´ò¿ª°´Å¥Ê±»ñÈ¡ÊäÈëµÄÂ·¾¶
        buttonOpen1.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String fileSavePath;
                //Çå¿Õµ¥ÐÐ¿ò
                textAreaSave.setText("");
                //»ñÈ¡µ¥ÐÐ¿òµÄÄÚÈÝ
                fileSavePath = textFieldSave.getText();
                //System.out.println(fileString);
                File file =  new File(fileSavePath);
                String fileNames[] = file.list();
                for(String fileName:fileNames){
                    textAreaSave.append(fileName+"\n");
                }
            }
        });
        //±£´æ°´Å¥µÄ¼àÌý
        buttonSave.addActionListener(new ActionListener(){
            public void actionPerformed(ActionEvent e){
                String fileSavePath  = textFieldSave.getText();
                BufferedWriter bw =null;
                bw=new BufferedWriter(new OutputStreamWriter(new FileWriter(fileSavePath)));
            }
        });
        frameSave.pack();
        frameSave.setVisible(true);
    }

    //Ö´ÐÐ·¢ËÍ
    public void send(){
        message = textField.getText().trim();
        try{
            writer = new PrintStream(socket.getOutputStream());

            message = textField.getText().trim();
            ip= InetAddress.getLocalHost().getHostAddress();
            textField.setText(null);
            writer.println("  "+name+"£º"+message);
        }catch(Exception e){
        }
    }


    //Á¬½Ó·þÎñ¶Ë
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
                if('a'==content.charAt(0)){
                    new newClient4().saveFile();
                    System.out.println("ÎÒÖªµÀÁË");
                }
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

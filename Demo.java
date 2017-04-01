import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

class Demo {
	JFrame  f = new JFrame();
	JButton buttonChoose = new JButton("buttonChoose");
	JPanel p = new JPanel();

	public static void main(String[] args)throws Exception{
		String content = "a";
			          if("a"==content){
                        System.out.println("aaa");
                    }
	}
	/*public void init(){
			p.add(buttonChoose);
			f.add(p);
			f.pack();
			f.setVisible(true);

		buttonChoose.addActionListener(new ActionListener(){
			public void actionPerformed(ActionEvent e){
				try{
					FileReader fr = new FileReader("e:\\11.txt");
					char[] buf = new char[1024];
					int len = 0;
					while((len = fr.read(buf)) !=-1){
						System.out.print(new String(buf,0,len));
					}
				}catch(Exception ex){

				}
			}
		});
	}
	*/
	
}
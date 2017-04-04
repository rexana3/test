import javax.swing.*;
import java.awt.*;
import java.io.*;
import java.awt.event.*;

class Demo {
	public static void main(String[] args)throws Exception{
		BufferedReader bufr = new BufferedReader(new InputStreamReader(System.in));
		BufferedWriter bufw = new BufferedWriter(new OutputStreamWriter(new FileOutputStream("a.txt")));
		String line = null;
		while((line = bufr.readLine())!=null){
		  if("over".equals(line))break;
		  bufw.write(line.toUpperCase());
		  bufw.newLine();
		  bufw.flush();
		}
	}
}
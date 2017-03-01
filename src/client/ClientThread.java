package client;

import java.awt.Color;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.Socket;

import javax.swing.DefaultListModel;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

/**
 * 
 * @author 何群芳
 * 该类负责接收服务器发送过来的消息，消息中由系统添加了一些特殊符号来区分消息的性质
 * 其中USERNAME：用户名
 *    COLOR:表示需要使用不同的颜色
 *    PRIVATE:表示私聊消息
 *    EXIT：表示退出
 *    @：为分隔符，分割开来两个不同意义的消息
 *    PRIVATE EXIT:表示私聊对象的退出，不会影响到群聊
 *该类通过对消息的分离，截取不同的特殊符号，来达到做出不同的响应的效果
 *并在该类得到服务器传过来的用户更新在线用户列表
 *
 */
//该线程只负责接收服务器发送过来的消息
//发送消息在框架线程中执行，因为发送是主动的，无法确定发送的时间
public class ClientThread extends Thread{
//	以USER结尾表示该信息为用户信息
	private ClientMainJframe mainJframe;
	private JTextPane textArea;
	private DefaultListModel userList;
	private BufferedReader reader;
	private Socket socket;
	public interface Callbacks{
		public void Mess(String Mess);
	}
  public ClientThread(Socket socket,JTextPane textArea,DefaultListModel usJList,ClientMainJframe mainJframe) throws IOException {
	// TODO Auto-generated constructor stub
	  this.socket = socket;
	  this.textArea=textArea;
	  this.userList=usJList;
	  reader=new BufferedReader(new InputStreamReader(socket.getInputStream()));
	 this.mainJframe=mainJframe;
}
	@Override
	public void run(){
		// TODO Auto-generated method stub
//		采用循环的方式不断地读取服务器发送过来的消息
	    	System.out.println("正在接收服务器发送过来的消息;");
	    		this.receiveMessage();
	}
public void receiveMessage() {
//	接收服务器发送过来的消息
	String contents="";
	try {
		while ((contents=reader.readLine())!=null) {
			if (contents.endsWith("USERNAME")) {
//				将在线用户信息添加到在线用户LIst中
				userList.addElement(contents.substring(0, contents.length()-8));
			}else {
				if (contents.endsWith("COLOR")) {
//					使得用户说的颜色字体不一样
					this.setDocs(contents.substring(0, contents.length()-5), Color.green, true, 20);
				
				}else {
					if (contents.startsWith("PRIVATE")) {
						contents=contents.substring(7);
						String name=contents.substring(0, contents.indexOf("@"));
						contents=contents.substring(contents.indexOf("@")+1);
						if (contents.endsWith("EXIT")) {
							contents="";
						}else {
							if (ClientMainJframe.listPrivas.containsKey(name)) {
//								说明当前用户和发送私聊用户存在一个私聊框，只需要直接把消息放入到消息显示区即可
								PersonnalChatJFrame frame=ClientMainJframe.listPrivas.get(name);
								frame.frame.setVisible(true);
								Document document=frame.messageShow.getDocument();
								document.insertString(document.getLength(), "\n"+contents, null);
				                
							}else {
//								一个是群聊窗口，一个是私聊窗口
//								群聊窗口管理私聊窗口
								PersonnalChatJFrame frame=new PersonnalChatJFrame(name,mainJframe);
								mainJframe.listPrivas.put(name, frame);
								Document document=frame.messageShow.getDocument();
								document.insertString(document.getLength(), contents, null);
							}
							
						}
						continue;
					}else {
						if (contents.endsWith("EXIT")&&!contents.startsWith("PRIVATE")) {
//							在内容后面加上\N还是以EXIT结尾
							System.out.println(contents);
							userList.removeElement(contents.substring(0, contents.indexOf("@")));
//							将替代以后的字符串返回，contents还是原来的内容
							contents=contents.replace('@', ' ');
							this.setDocs(contents.substring(0, contents.length()-4)+"\n", Color.red, false, 16);
						}
						else {
							this.setDocs(contents, Color.black, false, 16);
						}
					}
					
				}
				
			}
		}
		reader.reset();
	} catch (Exception e) {
		// TODO: handle exception
	}
}
public   void   insert(String   str,   AttributeSet   attrSet)   {   
    Document   doc   =   textArea.getDocument();   
    str   ="\n"   +   str   ;   
    try   {   
        doc.insertString(doc.getLength(),   str,   attrSet);   
    }   
    catch   (BadLocationException   e)   {   
        System.out.println("BadLocationException:   "   +   e);   
    }   
}   

public   void   setDocs(String   str,Color   col,boolean   bold,int   fontSize)   {  
//	简单属性集合
    SimpleAttributeSet   attrSet   =   new   SimpleAttributeSet();   
    StyleConstants.setForeground(attrSet,   col);   
    //颜色   
    if(bold==true){   
        StyleConstants.setBold(attrSet,   true);   
    }//字体类型   
    StyleConstants.setFontSize(attrSet,   fontSize);   
    //字体大小   
    insert(str,   attrSet);   
}   

}

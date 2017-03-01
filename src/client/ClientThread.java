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
 * @author ��Ⱥ��
 * ���ฺ����շ��������͹�������Ϣ����Ϣ����ϵͳ�����һЩ���������������Ϣ������
 * ����USERNAME���û���
 *    COLOR:��ʾ��Ҫʹ�ò�ͬ����ɫ
 *    PRIVATE:��ʾ˽����Ϣ
 *    EXIT����ʾ�˳�
 *    @��Ϊ�ָ������ָ��������ͬ�������Ϣ
 *    PRIVATE EXIT:��ʾ˽�Ķ�����˳�������Ӱ�쵽Ⱥ��
 *����ͨ������Ϣ�ķ��룬��ȡ��ͬ��������ţ����ﵽ������ͬ����Ӧ��Ч��
 *���ڸ���õ����������������û����������û��б�
 *
 */
//���߳�ֻ������շ��������͹�������Ϣ
//������Ϣ�ڿ���߳���ִ�У���Ϊ�����������ģ��޷�ȷ�����͵�ʱ��
public class ClientThread extends Thread{
//	��USER��β��ʾ����ϢΪ�û���Ϣ
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
//		����ѭ���ķ�ʽ���ϵض�ȡ���������͹�������Ϣ
	    	System.out.println("���ڽ��շ��������͹�������Ϣ;");
	    		this.receiveMessage();
	}
public void receiveMessage() {
//	���շ��������͹�������Ϣ
	String contents="";
	try {
		while ((contents=reader.readLine())!=null) {
			if (contents.endsWith("USERNAME")) {
//				�������û���Ϣ��ӵ������û�LIst��
				userList.addElement(contents.substring(0, contents.length()-8));
			}else {
				if (contents.endsWith("COLOR")) {
//					ʹ���û�˵����ɫ���岻һ��
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
//								˵����ǰ�û��ͷ���˽���û�����һ��˽�Ŀ�ֻ��Ҫֱ�Ӱ���Ϣ���뵽��Ϣ��ʾ������
								PersonnalChatJFrame frame=ClientMainJframe.listPrivas.get(name);
								frame.frame.setVisible(true);
								Document document=frame.messageShow.getDocument();
								document.insertString(document.getLength(), "\n"+contents, null);
				                
							}else {
//								һ����Ⱥ�Ĵ��ڣ�һ����˽�Ĵ���
//								Ⱥ�Ĵ��ڹ���˽�Ĵ���
								PersonnalChatJFrame frame=new PersonnalChatJFrame(name,mainJframe);
								mainJframe.listPrivas.put(name, frame);
								Document document=frame.messageShow.getDocument();
								document.insertString(document.getLength(), contents, null);
							}
							
						}
						continue;
					}else {
						if (contents.endsWith("EXIT")&&!contents.startsWith("PRIVATE")) {
//							�����ݺ������\N������EXIT��β
							System.out.println(contents);
							userList.removeElement(contents.substring(0, contents.indexOf("@")));
//							������Ժ���ַ������أ�contents����ԭ��������
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
//	�����Լ���
    SimpleAttributeSet   attrSet   =   new   SimpleAttributeSet();   
    StyleConstants.setForeground(attrSet,   col);   
    //��ɫ   
    if(bold==true){   
        StyleConstants.setBold(attrSet,   true);   
    }//��������   
    StyleConstants.setFontSize(attrSet,   fontSize);   
    //�����С   
    insert(str,   attrSet);   
}   

}

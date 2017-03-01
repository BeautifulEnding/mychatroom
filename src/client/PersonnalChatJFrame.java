package client;

import java.awt.Color;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.text.AttributeSet;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;

import user.User;

/**
 * 
 * @author ��Ⱥ��
 * ����Ϊ˽�Ĵ��ڣ�������ʾ��ǰ�û����ں�˭˽��
 * ��������е�������ʾ����Ϣ�����ȥ
 * ������Ϣ�������ɫ�����ʾ�Լ�˵�Ļ�����ɫ�����ʾ�Է�˵�Ļ���ͻ�����
 * ���Է��˳�˽�ĺ󣬴�������ʾ�Է����˳�˽�ģ���ʱ���������Է�����˽����Ϣ��
 * �Է������µ���˽�Ĵ��ڣ������ϴε�˽��������Ȼ����
 * �ڸ������в���ֱ�ӽ���Ϣ�ϴ������������������ýӿڽ���Ϣ���������������߳�
 * �ɴ������߳̽���Ϣ���͸�����������Ϊ�������̲߳���������ʾ���������ڣ�����������Ϣ��������
 * ͨ����ʵ����Ϣ��ǰ������������PRIVATE��ʾ����ϢΪ˽����Ϣ
 */
public class PersonnalChatJFrame extends Thread{
	private String name="";
	public JFrame frame;
	public  JTextPane messageShow;
	private JTextArea message;
	private JButton sendButton;
	private Callbacks callbacks;
	private User user;
//	����ص��ӿ�
	public interface Callbacks{
		public void Message(String nameString,String Message);
	}
    public PersonnalChatJFrame(String name2,ClientMainJframe mainJframe) {
    	this.user=user;
    	frame=new JFrame();
    	this.name=name2;
    	frame.setTitle("���ں�"+name+"˽��");
    	callbacks=(Callbacks)mainJframe;
	// TODO Auto-generated constructor stub
    	this.init();
    	sendButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
				Send();
			}
		});
//    	�������������Ϣ����ʾ�˳�˽��
    	frame.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
//    			ע��һ���ü�\n����Ϊ����������ʱ������Ϊ��־
    			callbacks.Message(name,"PRIVATE"+name+"@"+"EXIT\n");
//    			���ٴ��ںͶԷ���˽�Ĵ���
//    			ClientMainJframe.listPrivas.remove(name);
    			frame.setVisible(false);
    		}
		});
    	message.addKeyListener(new KeyAdapter() {
    		@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			if (e.getKeyCode()==10) {
				Send();
			}
			}
		});
    }
    public void init() {
    			// ���ô��ڴ�С
    			frame.setSize(400, 600);
    			frame.setIconImage(new ImageIcon("E:\\MyEclipse EE\\photo\\person.png").getImage());
    			// ���ô���λ����Ļ�м�
    			frame.setLocationRelativeTo(null);
    			// ���ô�С���ɸ���
    			frame.setResizable(false);
    			frame.setLayout(null);
    			JPanel contentJPanel=(JPanel) frame.getContentPane();
    			contentJPanel.setOpaque(false);
    			messageShow=new JTextPane();
    			messageShow.setEditable(false);
    			JScrollPane pScrollPane=new JScrollPane(messageShow);
    			pScrollPane.setBounds(0, 0, 400, 500);
    			message=new JTextArea();
    			message.setLineWrap(true);
    			message.setFocusable(true);
    			JScrollPane pane=new JScrollPane(message);
    			pane.setBounds(0, 500, 300, 100);
    			contentJPanel.add(pane);
    			contentJPanel.add(pScrollPane);
    			sendButton =new JButton("����");
    			sendButton.setBounds(300, 500, 100,75);
    			contentJPanel.add(sendButton);
    			// ���ô��ڿɼ�
    			frame.setVisible(true);
    }
	public   void   insert(String   str,   AttributeSet   attrSet)   {   
	    Document   doc   =   messageShow.getDocument();   
	    str   ="\n"   +   str   ;   
	    try   {   
	        doc.insertString(doc.getLength(),   str,   attrSet);   
	    }   
	    catch   (BadLocationException   e)   {   
	        System.out.println("BadLocationException:   "   +   e);   
	    }   
	}   

	public   void   setDocs(String   str,Color   col,boolean   bold,int   fontSize)   {  
//		�����Լ���
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
	private void Send() {
		// TODO Auto-generated method stub
		if (message.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "������Ϣ����Ϊ��");
		}
		else {
			setDocs(message.getText()+"\n", Color.green, true, 16);
			callbacks.Message(name,"PRIVATE"+name+"@"+message.getText()+"\n");
			message.setText(null);
		}
	}

}

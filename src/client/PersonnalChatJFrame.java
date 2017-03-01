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
 * @author 何群芳
 * 该类为私聊窗口，标题显示当前用户正在和谁私聊
 * 将输入框中的内容显示到消息面板中去
 * 其中消息面板中绿色字体表示自己说的话，黑色字体表示对方说的话，突出身份
 * 当对方退出私聊后，窗口则显示对方已退出私聊，此时若继续给对方发送私聊消息，
 * 对方会重新弹出私聊窗口，其中上次的私聊内容依然存在
 * 在给窗口中并不直接将消息上传给服务器，而是利用接口将消息传给聊天主窗口线程
 * 由窗口主线程将消息发送给服务器，因为窗口主线程不仅负责显示聊天主窗口，还负责发送消息给服务器
 * 通过在实际消息的前面加上特殊符号PRIVATE表示该消息为私聊消息
 */
public class PersonnalChatJFrame extends Thread{
	private String name="";
	public JFrame frame;
	public  JTextPane messageShow;
	private JTextArea message;
	private JButton sendButton;
	private Callbacks callbacks;
	private User user;
//	定义回调接口
	public interface Callbacks{
		public void Message(String nameString,String Message);
	}
    public PersonnalChatJFrame(String name2,ClientMainJframe mainJframe) {
    	this.user=user;
    	frame=new JFrame();
    	this.name=name2;
    	frame.setTitle("正在和"+name+"私聊");
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
//    	向服务器发送消息，表示退出私聊
    	frame.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
//    			注意一定得加\n，因为服务器读的时候以行为标志
    			callbacks.Message(name,"PRIVATE"+name+"@"+"EXIT\n");
//    			不再存在和对方的私聊窗口
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
    			// 设置窗口大小
    			frame.setSize(400, 600);
    			frame.setIconImage(new ImageIcon("E:\\MyEclipse EE\\photo\\person.png").getImage());
    			// 设置窗口位于屏幕中间
    			frame.setLocationRelativeTo(null);
    			// 设置大小不可更改
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
    			sendButton =new JButton("发送");
    			sendButton.setBounds(300, 500, 100,75);
    			contentJPanel.add(sendButton);
    			// 设置窗口可见
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
//		简单属性集合
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
	private void Send() {
		// TODO Auto-generated method stub
		if (message.getText().equals("")) {
			JOptionPane.showMessageDialog(null, "发送消息不能为空");
		}
		else {
			setDocs(message.getText()+"\n", Color.green, true, 16);
			callbacks.Message(name,"PRIVATE"+name+"@"+message.getText()+"\n");
			message.setText(null);
		}
	}

}

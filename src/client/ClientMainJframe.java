package client;
import java.awt.Color;

import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.PrintStream;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.Map;

import javax.swing.DefaultListModel;
import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JFrame;
import javax.swing.JList;
import javax.swing.JOptionPane;
import javax.swing.JScrollPane;
import javax.swing.JSplitPane;
import javax.swing.JTextArea;
import javax.swing.JTextPane;
import javax.swing.border.TitledBorder;
import javax.swing.event.ListSelectionEvent;
import javax.swing.event.ListSelectionListener;

import user.User;
/**
 * 
 * @author 何群芳
 * 该类负责维持聊天主窗口并且向服务器发送用户消息，窗口标题显示当前用户的用户名
 * 左边面板显示在线用户名，右边面板显示聊天内容，其中用户名为绿色字体，
 * 聊天内容为黑色字体，突出发送消息的用户
 * 最下面为内容输入框，当点击发送按钮后，Socket将消息发送给服务器对应的Socket，
 * 并将消息显示在消息面板中，输入框中的内容为群聊内容
 * 当在线用户列表不为空时，选中在线用户名，则私聊按钮可点击，可选择与该用户私聊
 * 当有用户退出聊天室时，则会在主面板上用红色字体显示该用户已退出聊天室
 *
 */
//让JFrame作为一个单独的线程
public class ClientMainJframe extends Thread implements PersonnalChatJFrame.Callbacks{
//	定义私聊对象的数组
//	保存的是对方的名字，窗口是自己的窗口
//	如果关闭窗口，那么应该是自己的私聊数组里少了一个
 public static Map<String, PersonnalChatJFrame> listPrivas=new HashMap<String ,PersonnalChatJFrame>();
	private boolean exist=false;
//	定义客户端信息
	private Socket socket;
	private User user;
	private JList<String> userList;
	private JScrollPane rightScroll;
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;
	private DefaultListModel listModel;
	private JButton send,privaButton;
//	定义一个输入框输入消息
	private JTextPane messageShow;
	private JTextArea message;
//	定义一个窗口
	private JFrame frame;
//	定义Socket的输出流
	private PrintStream writer;
    private ClientThread clientThread;
    public ClientMainJframe(User user) {
    	this.user=user;
    	init();
    	this.Listener();
	}
//   这只是一个普通的方法，只是该线程类会自动调用它而已
//    不要想多了
    @Override
	public void run() {
    	try {
			socket = new Socket("127.0.0.1",30000);
			writer=new PrintStream(socket.getOutputStream());
		    ObjectOutputStream stream=new ObjectOutputStream(socket.getOutputStream());
		    stream.writeObject(this.user);
//			把客户端对应的Socket和User传到专门处理数据的线程中去
			clientThread=new ClientThread(socket, messageShow,listModel,ClientMainJframe.this);
			clientThread.start();
		} catch (UnknownHostException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		} catch (IOException e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
	}
    public void sendMessage(String message2) {
		writer.print(message2);
    	writer.flush();
	    message.setText(null);
	}
    public void  Listener() {
    	frame.addWindowListener(new WindowAdapter() {
    		@Override
    		public void windowClosing(WindowEvent e) {
//    			注意一定得加\n，因为服务器读的时候以行为标志
    			sendMessage("EXIT\n");
    			try {
    				if (clientThread!=null) {
//    					退出聊天室，关闭输入输出和Socket资源
						clientThread.stop();
					}
    				writer.close();
					socket.close();
				} catch (IOException e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}
    			System.exit(0);
    		}
		});
    	privaButton.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				实现私聊
				String resultString=userList.getSelectedValue();
				if (listPrivas.containsKey(resultString)) {
					exist=true;
				}
				if (exist) {
					JOptionPane.showMessageDialog(null, "正在和"+resultString+"私聊！");
				}else {
					PersonnalChatJFrame frame=new PersonnalChatJFrame(resultString,ClientMainJframe.this);
//					将窗口看成一个对象，将该用户和另一个用户私聊的信息放入Map集合中
					listPrivas.put(resultString, frame);
				}
				privaButton.setEnabled(false);
				userList.setSelectedIndex(-1);
			}
		});
    	userList.addListSelectionListener(new ListSelectionListener() {
			
			@Override
			public void valueChanged(ListSelectionEvent e) {
				// TODO Auto-generated method stub
				privaButton.setEnabled(true);
			}
		});
     send.addActionListener(new ActionListener() {
			
			@Override
			public void actionPerformed(ActionEvent e) {
				// TODO Auto-generated method stub
//				将消息发送给服务器
				if (message.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "消息不能为空");
				}
				else {
					sendMessage(message.getText()+"\n");
				}
			}
		});
	}
    public void init() {
    	frame=new JFrame();
		// TODO Auto-generated constructor stub
//      设置标题
    	frame.setTitle(user.getName());
    	frame.setIconImage(new ImageIcon("E:\\MyEclipse EE\\photo\\person.png").getImage());
//    	设置窗口无布局
    	frame.setLayout(null);
//    	设置窗口大小
    	frame.setSize(800, 500);
//    	设置窗口位于屏幕中间
    	frame.setLocationRelativeTo(null);
//    	设置大小不可更改
    	frame.setResizable(false);
//    	设置退出程序
//    	设置总消息的文本域
    	 messageShow=new JTextPane();
//    	设置文本域不可编辑
    	messageShow.setEditable(false);
    	listModel = new DefaultListModel();
		userList = new JList(listModel);
    	rightScroll=new JScrollPane(messageShow);
    	rightScroll.setBounds(0, 0, 800, 400);
    	rightScroll.setBorder(new TitledBorder("消息显示区"));
    	rightScroll.setBounds(210, 0, 590, 400);
		leftScroll = new JScrollPane(userList);
		leftScroll.setBorder(new TitledBorder("在线用户"));
		leftScroll.setBounds(0, 0, 200, 400);
    	centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll,
				rightScroll);
    	centerSplit.setBounds(200, 0, 10, 400);
		centerSplit.setDividerLocation(50);
		frame.getContentPane().add(leftScroll);
		frame.getContentPane().add(centerSplit);
		frame.getContentPane().add(rightScroll);
    	message=new JTextArea();
//    	设置自动换行
    	message.setLineWrap(true);
    	message.setBackground(Color.white);
    	message.setFocusable(true);
    	JScrollPane jScrollPane=new JScrollPane(message);
    	jScrollPane.setBounds(0, 400, 600, 100);
    	frame.getContentPane().add(jScrollPane);
//    	发送按钮
    	 send=new JButton("发送");
    	send.setBounds(600, 400, 100, 65);
//    	私聊按钮
    	privaButton=new JButton("私聊");
    	privaButton.setBounds(700, 400, 100, 65);
    	privaButton.setEnabled(false);
      	frame.add(privaButton);
    	frame.add(send);
//    	设置窗口可见
    	frame.setVisible(true);
    	frame.setBackground(Color.darkGray);
	}
	@Override
	public void Message(String nameString,String Message) {
	
		// TODO Auto-generated method stub
		this.sendMessage(Message);
	}
}

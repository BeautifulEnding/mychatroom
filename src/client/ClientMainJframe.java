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
 * @author ��Ⱥ��
 * ���ฺ��ά�����������ڲ���������������û���Ϣ�����ڱ�����ʾ��ǰ�û����û���
 * ��������ʾ�����û������ұ������ʾ�������ݣ������û���Ϊ��ɫ���壬
 * ��������Ϊ��ɫ���壬ͻ��������Ϣ���û�
 * ������Ϊ��������򣬵�������Ͱ�ť��Socket����Ϣ���͸���������Ӧ��Socket��
 * ������Ϣ��ʾ����Ϣ����У�������е�����ΪȺ������
 * �������û��б�Ϊ��ʱ��ѡ�������û�������˽�İ�ť�ɵ������ѡ������û�˽��
 * �����û��˳�������ʱ���������������ú�ɫ������ʾ���û����˳�������
 *
 */
//��JFrame��Ϊһ���������߳�
public class ClientMainJframe extends Thread implements PersonnalChatJFrame.Callbacks{
//	����˽�Ķ��������
//	������ǶԷ������֣��������Լ��Ĵ���
//	����رմ��ڣ���ôӦ�����Լ���˽������������һ��
 public static Map<String, PersonnalChatJFrame> listPrivas=new HashMap<String ,PersonnalChatJFrame>();
	private boolean exist=false;
//	����ͻ�����Ϣ
	private Socket socket;
	private User user;
	private JList<String> userList;
	private JScrollPane rightScroll;
	private JScrollPane leftScroll;
	private JSplitPane centerSplit;
	private DefaultListModel listModel;
	private JButton send,privaButton;
//	����һ�������������Ϣ
	private JTextPane messageShow;
	private JTextArea message;
//	����һ������
	private JFrame frame;
//	����Socket�������
	private PrintStream writer;
    private ClientThread clientThread;
    public ClientMainJframe(User user) {
    	this.user=user;
    	init();
    	this.Listener();
	}
//   ��ֻ��һ����ͨ�ķ�����ֻ�Ǹ��߳�����Զ�����������
//    ��Ҫ�����
    @Override
	public void run() {
    	try {
			socket = new Socket("127.0.0.1",30000);
			writer=new PrintStream(socket.getOutputStream());
		    ObjectOutputStream stream=new ObjectOutputStream(socket.getOutputStream());
		    stream.writeObject(this.user);
//			�ѿͻ��˶�Ӧ��Socket��User����ר�Ŵ������ݵ��߳���ȥ
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
//    			ע��һ���ü�\n����Ϊ����������ʱ������Ϊ��־
    			sendMessage("EXIT\n");
    			try {
    				if (clientThread!=null) {
//    					�˳������ң��ر����������Socket��Դ
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
//				ʵ��˽��
				String resultString=userList.getSelectedValue();
				if (listPrivas.containsKey(resultString)) {
					exist=true;
				}
				if (exist) {
					JOptionPane.showMessageDialog(null, "���ں�"+resultString+"˽�ģ�");
				}else {
					PersonnalChatJFrame frame=new PersonnalChatJFrame(resultString,ClientMainJframe.this);
//					�����ڿ���һ�����󣬽����û�����һ���û�˽�ĵ���Ϣ����Map������
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
//				����Ϣ���͸�������
				if (message.getText().equals("")) {
					JOptionPane.showMessageDialog(null, "��Ϣ����Ϊ��");
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
//      ���ñ���
    	frame.setTitle(user.getName());
    	frame.setIconImage(new ImageIcon("E:\\MyEclipse EE\\photo\\person.png").getImage());
//    	���ô����޲���
    	frame.setLayout(null);
//    	���ô��ڴ�С
    	frame.setSize(800, 500);
//    	���ô���λ����Ļ�м�
    	frame.setLocationRelativeTo(null);
//    	���ô�С���ɸ���
    	frame.setResizable(false);
//    	�����˳�����
//    	��������Ϣ���ı���
    	 messageShow=new JTextPane();
//    	�����ı��򲻿ɱ༭
    	messageShow.setEditable(false);
    	listModel = new DefaultListModel();
		userList = new JList(listModel);
    	rightScroll=new JScrollPane(messageShow);
    	rightScroll.setBounds(0, 0, 800, 400);
    	rightScroll.setBorder(new TitledBorder("��Ϣ��ʾ��"));
    	rightScroll.setBounds(210, 0, 590, 400);
		leftScroll = new JScrollPane(userList);
		leftScroll.setBorder(new TitledBorder("�����û�"));
		leftScroll.setBounds(0, 0, 200, 400);
    	centerSplit = new JSplitPane(JSplitPane.HORIZONTAL_SPLIT, leftScroll,
				rightScroll);
    	centerSplit.setBounds(200, 0, 10, 400);
		centerSplit.setDividerLocation(50);
		frame.getContentPane().add(leftScroll);
		frame.getContentPane().add(centerSplit);
		frame.getContentPane().add(rightScroll);
    	message=new JTextArea();
//    	�����Զ�����
    	message.setLineWrap(true);
    	message.setBackground(Color.white);
    	message.setFocusable(true);
    	JScrollPane jScrollPane=new JScrollPane(message);
    	jScrollPane.setBounds(0, 400, 600, 100);
    	frame.getContentPane().add(jScrollPane);
//    	���Ͱ�ť
    	 send=new JButton("����");
    	send.setBounds(600, 400, 100, 65);
//    	˽�İ�ť
    	privaButton=new JButton("˽��");
    	privaButton.setBounds(700, 400, 100, 65);
    	privaButton.setEnabled(false);
      	frame.add(privaButton);
    	frame.add(send);
//    	���ô��ڿɼ�
    	frame.setVisible(true);
    	frame.setBackground(Color.darkGray);
	}
	@Override
	public void Message(String nameString,String Message) {
	
		// TODO Auto-generated method stub
		this.sendMessage(Message);
	}
}

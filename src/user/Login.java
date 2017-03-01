package user;

import java.awt.Color;
import java.awt.FlowLayout;
import java.awt.Font;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.FocusEvent;
import java.awt.event.FocusListener;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.awt.event.KeyListener;
import java.net.Socket;

import javax.swing.ImageIcon;
import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JOptionPane;
import javax.swing.JPasswordField;
import javax.swing.JTextField;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;

import mysql.MySqlUtil;

//import org.jvnet.substance.skin.SubstanceOfficeBlue2007LookAndFeel;

import server.server;
import client.ClientMainJframe;
import client.ClientThread;
/**
 * 
 * @author ��Ⱥ��
 * ����Ϊ��¼���棬����ȷ���û������
 * �����ȷ����ť���߰���enter��ʱ���ж��û�����Ƿ�ɿ������ɿ����½�һ���û�����һ���߳��½�һ��Socket�����������
 * ͨ�ţ��������촰�ڣ��رյ�¼����
 * �����ɿ����򵯳��������ʾ�û���¼ʧ�ܣ��û�������������޷�������������ҳ��
 */


public class Login extends JFrame implements ActionListener {
	private JTextField id;
	private JPasswordField password;
	private JButton sureButton;
//	����һ��ȫ�ֵ�Socket��Ҫ�Ǿֲ������Ļ�����ִ����SocketҲ����ServerSocket�Ͽ�������
	public static void main(String[] args) {
		try {
//    	    ����Ĭ�ϵĴ���װ�ޣ����Ը��Ĵ��ڱ��⴦�Ĵ��ڷ��
    	  JFrame.setDefaultLookAndFeelDecorated(true);
    	  JDialog.setDefaultLookAndFeelDecorated(true);
    	  new Login();
    	} catch (Exception e1) {
    		// TODO Auto-generated catch block
    		e1.printStackTrace();
    	}
	}
	public Login() {
		// TODO Auto-generated constructor stub
		this.init();
		sureButton.addActionListener(this);
		password.addKeyListener(new KeyAdapter() {
			@Override
			public void keyPressed(KeyEvent e) {
				// TODO Auto-generated method stub
			if (e.getKeyCode()==10) {
				ifLogin();
			}
			}
		});
	}
	@Override
	public void actionPerformed(ActionEvent e) {
		// TODO Auto-generated method stub
		this.ifLogin();
	}
	public void init() {
    	// ���ñ���
    			this.setTitle("��¼");
    			// ���ô��ڴ�С
    			this.setSize(600, 400);
    			this.setIconImage(new ImageIcon("E:\\MyEclipse EE\\photo\\person.png").getImage());
    			// ���ô���λ����Ļ�м�
    			this.setLocationRelativeTo(null);
    			// ���ô�С���ɸ���
    			this.setResizable(false);
    			// �����˳�����
    			this.setDefaultCloseOperation(EXIT_ON_CLOSE);
    			this.setLayout(null);
    			JLabel labelbg=new JLabel(new ImageIcon("E:\\MyEclipse EE\\photo\\bg.png"));
    			labelbg.setBounds(200, 0, 200, 200);
    			this.getContentPane().add(labelbg);
    			id = new JTextField(15);
//    			id.setBackground(Color.);
    			id.setBounds(210, 200, 200, 40);
    			password = new JPasswordField(15);
    			password.setBounds(210, 250, 200, 40);
    			sureButton = new JButton("ȷ��");
    			sureButton.setBounds(150, 300, 260,50);
    			sureButton.setFont(new Font("����", 2, 30));
    			JLabel label=new JLabel("����:  ");
    			label.setFont(new Font("����", 2, 20));
    			label.setBounds(150, 200, 100, 50);
    			JLabel label2=new JLabel("����:");
    			label2.setFont(new Font("����", 2, 20));
    			label2.setBounds(150, 250, 100, 50);
    			this.getContentPane().add(label);
    			this.getContentPane().add(id);
    			this.getContentPane().add(label2);
    			this.getContentPane().add(password);
    			this.getContentPane().add(sureButton);
    			// ���ô��ڿɼ�
    			this.setVisible(true);
	}
	private void ifLogin() {
		// TODO Auto-generated method stub
		// System.out.println("���ӳɹ�");
		 boolean login=false;
		  String nameString=id.getText().trim();
		  String pass=password.getText().trim();
		  String sqlString="select * from LOGIN where ID='"+nameString+"' and PASSWORD='"+pass+"'";
		  boolean have=MySqlUtil.connectionSql(sqlString);
		  if (have) {
			    if (!server.serverSockets.isEmpty()) {
			    	for (User user:server.serverSockets.keySet()) {
						if (user.getName().equals(nameString)) {
							login=true;
						}
					}
				}
			    else {
					System.out.println("��������Ϊ0");
				}
			    if (login) {
			    	JOptionPane.showMessageDialog(null, "���û��Ѿ���¼����˶Ժ�����");
				}else {
				    User user = new User(nameString, pass);
					// �½�һ���û�����Ϊ���û��½�һ���߳����������������
					new ClientMainJframe(user).start();
//					��¼���ڲ��ɼ�
					this.dispose();
				}
		}
		else {
			JOptionPane.showMessageDialog(null, "�û����������������������", "��֤�û���Ϣʧ��", 0);
		}
	} 
	
}

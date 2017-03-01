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
 * @author 何群芳
 * 该类为登录界面，负责确认用户的身份
 * 当点击确定按钮或者按下enter键时，判断用户身份是否可靠，若可靠，新建一个用户，另建一个线程新建一个Socket与服务器进行
 * 通信，并打开聊天窗口，关闭登录窗口
 * 若不可靠，则弹出警告框，提示用户登录失败，用户名或密码错误，无法进入聊天室主页面
 */


public class Login extends JFrame implements ActionListener {
	private JTextField id;
	private JPasswordField password;
	private JButton sureButton;
//	定义一个全局的Socket，要是局部变量的话方法执行完Socket也就与ServerSocket断开连接了
	public static void main(String[] args) {
		try {
//    	    设置默认的窗口装修，可以更改窗口标题处的窗口风格
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
    	// 设置标题
    			this.setTitle("登录");
    			// 设置窗口大小
    			this.setSize(600, 400);
    			this.setIconImage(new ImageIcon("E:\\MyEclipse EE\\photo\\person.png").getImage());
    			// 设置窗口位于屏幕中间
    			this.setLocationRelativeTo(null);
    			// 设置大小不可更改
    			this.setResizable(false);
    			// 设置退出程序
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
    			sureButton = new JButton("确定");
    			sureButton.setBounds(150, 300, 260,50);
    			sureButton.setFont(new Font("宋体", 2, 30));
    			JLabel label=new JLabel("姓名:  ");
    			label.setFont(new Font("宋体", 2, 20));
    			label.setBounds(150, 200, 100, 50);
    			JLabel label2=new JLabel("密码:");
    			label2.setFont(new Font("宋体", 2, 20));
    			label2.setBounds(150, 250, 100, 50);
    			this.getContentPane().add(label);
    			this.getContentPane().add(id);
    			this.getContentPane().add(label2);
    			this.getContentPane().add(password);
    			this.getContentPane().add(sureButton);
    			// 设置窗口可见
    			this.setVisible(true);
	}
	private void ifLogin() {
		// TODO Auto-generated method stub
		// System.out.println("连接成功");
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
					System.out.println("在线人数为0");
				}
			    if (login) {
			    	JOptionPane.showMessageDialog(null, "该用户已经登录，请核对后再试");
				}else {
				    User user = new User(nameString, pass);
					// 新建一个用户，并为该用户新建一个线程与服务器建立连接
					new ClientMainJframe(user).start();
//					登录窗口不可见
					this.dispose();
				}
		}
		else {
			JOptionPane.showMessageDialog(null, "用户名或密码错误，请重新输入", "验证用户信息失败", 0);
		}
	} 
	
}

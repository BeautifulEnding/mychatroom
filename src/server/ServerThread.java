package server;

import java.io.BufferedReader;
import java.io.DataInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.io.PrintWriter;
import java.net.Socket;

import user.User;

/**
 * 
 * @author 何群芳
 * 该类负责与对应的客户端进行数据交流
 * 在第一次时，先得到客户端传送过来的用户，并将该user，和Socket保存到serverSockets，
 * 通过遍历该集合中的Socket，向其中的每个Socket发送消息，达到群聊的效果
 * 通过user找到集合中特定的Socket，达到私聊的效果
 *  1、接收消息：
 *     通过对客户端发送过来的消息，截取特殊符号，
 *    其中USERNAME：用户名
 *    COLOR:表示需要使用不同的颜色
 *    PRIVATE:表示私聊消息
 *    EXIT：表示退出
 *    @：为分隔符，分割开来两个不同意义的消息
 *    PRIVATE EXIT:表示私聊对象的退出，不会影响到群聊
 *    通过对特殊符号的判断来做出不同的响应，
 *  2、发送消息
 *    通过接收到的消息的特殊符号的判断，将传送过来的消息做相应的处理，得到用户实际需要发送的消息，
 *    然后再加上响应的特殊符号，告诉客户端的接收线程应该如何处理该条消息
 * 
 *
 */
public class ServerThread extends Thread{
	private static int flag=0;
//	定义输入、输出流
	private DataInputStream reader;
//定义与客户端进行数据通信的Socket
	private Socket socket;
	private User user;
	private PrintStream writer;
	public ServerThread(Socket socket) throws IOException {
		// TODO Auto-generated constructor stub
		this.socket=socket;
		reader=new DataInputStream(socket.getInputStream());
		writer=new PrintStream(socket.getOutputStream());
	}
	public void run() {
//		先接收客户端发送过来的用户信息
//		如果接收到了客户端的数据，根据数据做出相应的回复
		try {
			ObjectInputStream stream=new ObjectInputStream(socket.getInputStream());
			user = (User)stream.readObject();
			server.serverSockets.put(user, socket);
			System.out.println("新用户登录："+user.getName());
//			将新用户信息输出到每个客户端的在线人数上
//			还要将之前在线的用户输出到新上线的列表上
			for (User user:server.serverSockets.keySet()) {
				if (!user.getName().equals(this.user.getName())) {
					Socket socket=server.serverSockets.get(user);
					PrintStream stream2=new PrintStream(socket.getOutputStream());
//					在用户名后加上USERNAME作为标识符，表明信息的身份
					stream2.println(this.user.getName()+"USERNAME");
					writer.println(user.getName()+"USERNAME");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		采用循环的方式不断的接收从客户端发送过来的消息
//      不需要循环，一旦执行了该方法，就会一直在方法中的while里循环
			this.receiveMessage();
	}
	public void receiveMessage(){
//		一直在while语句中循环，所以while语句外面的内容直接执行不到
//		一个用户只执行到了一次receivMessage
//		System.out.println("正在接收消息");
//		用字符流每次只能读一行，不如用字节流来的方便，一次读取1024个字节的内容，只要不超过
//		该长度，都不会出现问题
	    byte[] bytes=new byte[1024];
	    int len=0;
	    String result="";
		try {
			while ((len=reader.read(bytes))!=-1) {
				try {
					if ((result=new String(bytes)).startsWith("EXIT")) {
//						群发
						server.serverSockets.remove(this.user);
						for (User user:server.serverSockets.keySet()) {
							Socket socket=server.serverSockets.get(user);
							PrintStream stream2=new PrintStream(socket.getOutputStream());
							String textString=this.user.getName()+"@已退出聊天室EXIT\n";
//							一定得加上\n不然在客户端的时候按行读的时候，这里没有换行，输入流就不停地往后想读行
//							在内容中加换行和用println在一行的时候效果相同
							stream2.print(textString);
							stream2.flush();
					       }
						reader.close();
						writer.close();
						Thread.currentThread().stop();
					}else {
						if ((result=new String(bytes)).startsWith("PRIVATE")) {
//							如果是私聊
							String name=result.substring(7, result.indexOf("@"));
							for (User user:server.serverSockets.keySet()) {
//								得到私聊对象的用户名
								if (user.getName().equals(name)){
									Socket socket=server.serverSockets.get(user);
									PrintStream stream2=new PrintStream(socket.getOutputStream());
									stream2.println("PRIVATE"+this.user.getName()+"@"+result.substring(result.indexOf("@")+1));
									stream2.flush();
								}
						}
//							continue;
						}
						else {
//							群发
							for (User user:server.serverSockets.keySet()) {
								Socket socket=server.serverSockets.get(user);
								PrintStream stream2=new PrintStream(socket.getOutputStream());
//								实现用户说的颜色字体和普通文本内容颜色字体不一样
								String textString=this.user.getName()+"说:COLOR\n";
								stream2.print(textString);
								stream2.write(bytes, 0, len);
//							清空输出流中的内容，否则会多次发送到客户端
								stream2.flush();
						}
						}
					}
				} catch (Exception e) {
					// TODO: handle exception
					e.printStackTrace();
				}
				}
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	}

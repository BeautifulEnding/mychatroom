package server;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.ObjectInputStream;
import java.io.PrintStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

import user.User;
/**
 * 
 * @author 何群芳
 * 该类为服务器总控制台，负责建立与客户端对应的新的Socket，并建立新的线程去处理与客户端的数据传送
 *
 */
public class server {
//	定义一个集合保存服务器所有与客户端进行通信的Socket
//	定义一个线程安全的Map集合
//	定义输出流
	private PrintStream writer;
	public static Map<User, Socket> serverSockets=new HashMap<User, Socket>();
public static void main(String[] args) throws IOException {
	ServerSocket socket=new ServerSocket(30000);
	while (true) {
		Socket socket2=socket.accept();
		new ServerThread(socket2).start();
	}
}
}
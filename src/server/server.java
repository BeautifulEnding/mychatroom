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
 * @author ��Ⱥ��
 * ����Ϊ�������ܿ���̨����������ͻ��˶�Ӧ���µ�Socket���������µ��߳�ȥ������ͻ��˵����ݴ���
 *
 */
public class server {
//	����һ�����ϱ��������������ͻ��˽���ͨ�ŵ�Socket
//	����һ���̰߳�ȫ��Map����
//	���������
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
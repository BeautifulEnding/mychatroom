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
 * @author ��Ⱥ��
 * ���ฺ�����Ӧ�Ŀͻ��˽������ݽ���
 * �ڵ�һ��ʱ���ȵõ��ͻ��˴��͹������û���������user����Socket���浽serverSockets��
 * ͨ�������ü����е�Socket�������е�ÿ��Socket������Ϣ���ﵽȺ�ĵ�Ч��
 * ͨ��user�ҵ��������ض���Socket���ﵽ˽�ĵ�Ч��
 *  1��������Ϣ��
 *     ͨ���Կͻ��˷��͹�������Ϣ����ȡ������ţ�
 *    ����USERNAME���û���
 *    COLOR:��ʾ��Ҫʹ�ò�ͬ����ɫ
 *    PRIVATE:��ʾ˽����Ϣ
 *    EXIT����ʾ�˳�
 *    @��Ϊ�ָ������ָ��������ͬ�������Ϣ
 *    PRIVATE EXIT:��ʾ˽�Ķ�����˳�������Ӱ�쵽Ⱥ��
 *    ͨ����������ŵ��ж���������ͬ����Ӧ��
 *  2��������Ϣ
 *    ͨ�����յ�����Ϣ��������ŵ��жϣ������͹�������Ϣ����Ӧ�Ĵ����õ��û�ʵ����Ҫ���͵���Ϣ��
 *    Ȼ���ټ�����Ӧ��������ţ����߿ͻ��˵Ľ����߳�Ӧ����δ��������Ϣ
 * 
 *
 */
public class ServerThread extends Thread{
	private static int flag=0;
//	�������롢�����
	private DataInputStream reader;
//������ͻ��˽�������ͨ�ŵ�Socket
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
//		�Ƚ��տͻ��˷��͹������û���Ϣ
//		������յ��˿ͻ��˵����ݣ���������������Ӧ�Ļظ�
		try {
			ObjectInputStream stream=new ObjectInputStream(socket.getInputStream());
			user = (User)stream.readObject();
			server.serverSockets.put(user, socket);
			System.out.println("���û���¼��"+user.getName());
//			�����û���Ϣ�����ÿ���ͻ��˵�����������
//			��Ҫ��֮ǰ���ߵ��û�����������ߵ��б���
			for (User user:server.serverSockets.keySet()) {
				if (!user.getName().equals(this.user.getName())) {
					Socket socket=server.serverSockets.get(user);
					PrintStream stream2=new PrintStream(socket.getOutputStream());
//					���û��������USERNAME��Ϊ��ʶ����������Ϣ�����
					stream2.println(this.user.getName()+"USERNAME");
					writer.println(user.getName()+"USERNAME");
				}
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
//		����ѭ���ķ�ʽ���ϵĽ��մӿͻ��˷��͹�������Ϣ
//      ����Ҫѭ����һ��ִ���˸÷������ͻ�һֱ�ڷ����е�while��ѭ��
			this.receiveMessage();
	}
	public void receiveMessage(){
//		һֱ��while�����ѭ��������while������������ֱ��ִ�в���
//		һ���û�ִֻ�е���һ��receivMessage
//		System.out.println("���ڽ�����Ϣ");
//		���ַ���ÿ��ֻ�ܶ�һ�У��������ֽ������ķ��㣬һ�ζ�ȡ1024���ֽڵ����ݣ�ֻҪ������
//		�ó��ȣ��������������
	    byte[] bytes=new byte[1024];
	    int len=0;
	    String result="";
		try {
			while ((len=reader.read(bytes))!=-1) {
				try {
					if ((result=new String(bytes)).startsWith("EXIT")) {
//						Ⱥ��
						server.serverSockets.remove(this.user);
						for (User user:server.serverSockets.keySet()) {
							Socket socket=server.serverSockets.get(user);
							PrintStream stream2=new PrintStream(socket.getOutputStream());
							String textString=this.user.getName()+"@���˳�������EXIT\n";
//							һ���ü���\n��Ȼ�ڿͻ��˵�ʱ���ж���ʱ������û�л��У��������Ͳ�ͣ�����������
//							�������мӻ��к���println��һ�е�ʱ��Ч����ͬ
							stream2.print(textString);
							stream2.flush();
					       }
						reader.close();
						writer.close();
						Thread.currentThread().stop();
					}else {
						if ((result=new String(bytes)).startsWith("PRIVATE")) {
//							�����˽��
							String name=result.substring(7, result.indexOf("@"));
							for (User user:server.serverSockets.keySet()) {
//								�õ�˽�Ķ�����û���
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
//							Ⱥ��
							for (User user:server.serverSockets.keySet()) {
								Socket socket=server.serverSockets.get(user);
								PrintStream stream2=new PrintStream(socket.getOutputStream());
//								ʵ���û�˵����ɫ�������ͨ�ı�������ɫ���岻һ��
								String textString=this.user.getName()+"˵:COLOR\n";
								stream2.print(textString);
								stream2.write(bytes, 0, len);
//							���������е����ݣ�������η��͵��ͻ���
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

package mysql;

import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
/**
 * 
 * @author ��Ⱥ��
 * ���ฺ�������ݿ⽻�������ڹ�����
 * �����û���Ϣ������java���ݿ�login����
 *
 */
public class MySqlUtil {
	public static boolean connectionSql(String sql) {
//		����Mysql������
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			��ȡ���ݿ�����
			Connection connection=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/JAVA","root","14045102");
//			ʹ��Connection������һ��Statement����
			Statement statement=(Statement) connection.createStatement();
//			�õ�ִ��sql����Ľ����
			ResultSet set=statement.executeQuery(sql);
			if (set.next()) {
				return true;
			}
		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return false;
	}

}

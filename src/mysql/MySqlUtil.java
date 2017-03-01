package mysql;

import java.sql.DriverManager;
import java.sql.ResultSet;

import com.mysql.jdbc.Connection;
import com.mysql.jdbc.Statement;
/**
 * 
 * @author 何群芳
 * 该类负责与数据库交流，属于工具类
 * 其中用户信息保存在java数据库login表中
 *
 */
public class MySqlUtil {
	public static boolean connectionSql(String sql) {
//		加载Mysql的驱动
		try {
			Class.forName("com.mysql.jdbc.Driver");
//			获取数据库连接
			Connection connection=(Connection) DriverManager.getConnection("jdbc:mysql://localhost:3306/JAVA","root","14045102");
//			使用Connection来创建一个Statement对象
			Statement statement=(Statement) connection.createStatement();
//			得到执行sql语句后的结果集
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

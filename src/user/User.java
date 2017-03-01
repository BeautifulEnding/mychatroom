package user;

import java.io.Serializable;
//使该对象可序列化
/**
 * 
 * @author 何群芳
 * 该类为用户对象
 *
 */
public class User implements Serializable{

private String name;
private String password;
public User(String name,String password) {
	// TODO Auto-generated constructor stub
	this.name=name;
	this.password=password;
}
public User() {
	// TODO Auto-generated constructor stub
}
public String getName() {
	return name;
}
public void setName(String id) {
	this.name = id;
}
public String getPassword() {
	return password;
}
public void setPassword(String password) {
	this.password = password;
}

}

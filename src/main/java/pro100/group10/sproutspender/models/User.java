package pro100.group10.sproutspender.models;

import java.io.Serializable;

@SuppressWarnings("serial")
public class User implements Serializable{
	private String username;
	private String password;
	private int Id;
	
	public User(String username, String password) {
		this.username = username;
		this.password = password;
	}
	
	public User(String username, String password, int id) {
		this.username = username;
		this.password = password;
		Id = id;
	}



	public int getId() {
		return Id;
	}

	public String getUsername() {
		return username;
	}
	
	public String getPassword() {
		return password;
	}
}

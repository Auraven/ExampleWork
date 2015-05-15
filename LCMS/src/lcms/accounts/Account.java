package lcms.accounts;

import java.sql.ResultSet;
import java.sql.SQLException;

public class Account {
	private boolean enabled, active;
	private String username, firstname, lastname, age;
	private ResultSet userSet;
	
	public Account(ResultSet userSet){
		this.userSet = userSet;
		try {
			username = userSet.getString("username");
			firstname = userSet.getString("firstname");
			lastname = userSet.getString("lastname");
			age = userSet.getString("age");
		} catch (SQLException e) {
			e.printStackTrace();
		}
		
	}
	
	public void setEnabled(boolean enabled){
		this.enabled = enabled;
	}
	public void setActive(boolean active){
		this.active = active;
	}
	public boolean isEnabled(){
		return enabled;
	}
	public boolean isActive(){
		return active;
	}
	public String getUsername(){
		return username;
	}
	public String getFirstname(){
		return firstname;
	}
	public String getLastname(){
		return lastname;
	}
	public String getAge(){
		return age;
	}
}

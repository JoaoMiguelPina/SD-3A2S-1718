package org.binas.domain;

import java.util.HashMap;

import org.binas.ws.EmailExists_Exception;
import org.binas.ws.InvalidEmail_Exception;
import org.binas.ws.UserView;

public class User {
	
	private String email;
	private boolean hasBina;
	private int credit;
	
	private HashMap<String, User> users = new HashMap<String, User>();
	
	public User (String email, boolean hasBina, int credit) throws InvalidEmail_Exception, EmailExists_Exception {
		checkEmail(email);
		this.email = email;
		this.hasBina = hasBina;
		this.credit = credit;
	}
	
	public void checkEmail(String email) throws InvalidEmail_Exception, EmailExists_Exception {
		
		String pattern = "^(([a-zA-Z0-9]+)|([a-zA-Z0-9]+\\.?[a-zA-Z0-9]+)+)@(([a-zA-Z0-9]+)|([a-zA-Z0-9]+\\.?[a-zA-Z0-9]+)+)";
		if(users.containsKey(email)) throw new EmailExists_Exception(email, null);
		if(!email.matches(pattern)) throw new InvalidEmail_Exception(email, null);
	}
	
	public String getEmail() {
		return email;
	}
	
	public boolean doesHaveBina() {
		return hasBina;
	}
	
	public int getCredit() {
		return credit;
	}
	
	public void setEmail(String email) {
		this.email = email;
	}
	
	public void setHasBina(boolean value) {
		this.hasBina = value;
	}
	
	public void setCredit(int credit) {
		this.credit = credit;
	}
	
	public void addUser(User user) {
		users.put(user.getEmail(), user);
	}
	
	public void addCredit (int credit) {
		this.credit += credit;
	}
	
	public void addOneCredit () {
		this.credit += 1;
	}
	
	public void removeCredit (int credit) {
		this.credit -= credit;
	}
	
	public void removeOneCredit () {
		this.credit -= 1;
	}
	
	public User getUser(String email) throws InvalidEmail_Exception{
		if (users.get(email) == null) throw new InvalidEmail_Exception("The email " + email + " is not registered.", null);
		return users.get(email);
	}
	
	public void deleteUser(String email) {
		users.remove(email);
	}
	
	public UserView getUserView() {
		UserView userView = new UserView();
		userView.setEmail(this.getEmail());
		userView.setHasBina(this.doesHaveBina());
		userView.setCredit(this.getCredit());
		return userView;
	}
	
}

package org.binas.station.domain;

import java.util.concurrent.atomic.AtomicInteger;


public class UserBalance {

	private String _email;
	private AtomicInteger _balance;
	private int _tag;
	
	public UserBalance(String email, AtomicInteger balance, int tag) {
		set_email(email);
		set_balance(balance);
		set_tag(tag);
	}

	public AtomicInteger get_balance() {
		return _balance;
	}

	public void set_balance(AtomicInteger _balance) {
		this._balance = _balance;
	}

	public String get_email() {
		return _email;
	}

	public void set_email(String _email) {
		this._email = _email;
	}

	public int get_tag() {
		return _tag;
	}

	public void set_tag(int _tag) {
		this._tag = _tag;
	}
	
}

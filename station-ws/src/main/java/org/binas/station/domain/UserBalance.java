package org.binas.station.domain;



public class UserBalance {

	private int _value;
	private int _tag;
	
	public UserBalance(int value, int tag) {
		set_value(value);
		set_tag(tag);
	}

	public int get_value() {
		return _value;
	}

	public void set_value(int _value) {
		this._value = _value;
	}

	public int get_tag() {
		return _tag;
	}

	public void set_tag(int _tag) {
		this._tag = _tag;
	}
	
}

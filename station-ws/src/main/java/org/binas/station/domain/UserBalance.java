package org.binas.station.domain;



public class UserBalance {

	private double _value;
	private int _tag;
	
	public UserBalance(double value, int tag) {
		set_value(value);
		set_tag(tag);
	}

	public double get_value() {
		return _value;
	}

	public void set_value(double _value) {
		this._value = _value;
	}

	public int get_tag() {
		return _tag;
	}

	public void set_tag(int _tag) {
		this._tag = _tag;
	}
	
}

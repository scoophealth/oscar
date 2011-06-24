package com.indivica.olis.parameters;

/**
 * First Name (Z50 Query only)
 * @author jen
 *
 */
public class PID52 implements Parameter {

	private String value;
	
	public PID52(String value) {
	    this.value = value;
    }

	public PID52() {
	}

	@Override
	public String toOlisString() {
		return getQueryCode() + "^" + (value != null ? value : "");
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof String)
			this.value = (String) value;
	}

	@Override
	public void setValue(Integer part, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public void setValue(Integer part, Integer part2, Object value) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String getQueryCode() {
		return "@PID.5.2";
	}

}

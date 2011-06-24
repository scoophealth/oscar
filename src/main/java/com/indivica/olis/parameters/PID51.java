package com.indivica.olis.parameters;

/**
 * Last Name (Z50 Query only)
 * @author jen
 *
 */
public class PID51 implements Parameter {

	private String value;
	
	public PID51(String value) {
	    this.value = value;
    }

	public PID51() {
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
		return "@PID.5.1";
	}

}

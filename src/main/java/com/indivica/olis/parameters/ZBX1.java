package com.indivica.olis.parameters;

/**
 * Retrieve All Test Results
 * @author jen
 *
 */
public class ZBX1 implements Parameter {

	private String value;
	
	public ZBX1(String value) {
	    this.value = value;
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
		return "@ZBX.1";
	}

}

package com.indivica.olis.parameters;

/**
 * Patient consent block-all indicator
 * @author jen
 *
 */
public class ZPD3 implements Parameter {

	private String value;
	
	public ZPD3(String value) {
	    this.value = value;
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + "^" + value;
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
		return "@ZPD.3";
	}

}

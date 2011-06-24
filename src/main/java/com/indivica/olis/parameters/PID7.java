package com.indivica.olis.parameters;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Date of Birth (Z50 Query only)
 * @author jen
 *
 */
public class PID7 implements Parameter {

	private String value;
	private SimpleDateFormat dateFormatter = new SimpleDateFormat("yyyyMMdd");
	
	public PID7(String value) {
	    this.value = value;
    }

	public PID7() {
	}

	@Override
	public String toOlisString() {
		return getQueryCode() + "^" + (value != null ? value : "");
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Date)
			this.value = dateFormatter.format((Date) value);
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
		return "@PID.7";
	}

}

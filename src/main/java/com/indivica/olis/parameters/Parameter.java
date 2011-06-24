package com.indivica.olis.parameters;

public interface Parameter {

	public String toOlisString();
	
	public void setValue(Object value);
	
	public void setValue(Integer part, Object value);
	
	public void setValue(Integer part, Integer part2, Object value);

	public String getQueryCode();
}

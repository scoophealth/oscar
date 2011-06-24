package com.indivica.olis.parameters;

public class QRD7 implements Parameter {

	private Integer queryNum;
	
	public QRD7(Integer queryNum) {
	    this.queryNum = queryNum;
    }

	@Override
	public String toOlisString() {
		return getQueryCode() + ".1^" + queryNum + "~" + getQueryCode() + ".2.1^RD~" + getQueryCode() + ".2.3^HL70126";
	}

	@Override
	public void setValue(Object value) {
		if (value instanceof Integer) {
			queryNum = (Integer) value;
		}
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
		return "@QRD.7";
	}

}

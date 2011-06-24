package com.indivica.olis.parameters;

/**
 * Destination Laboratory (Z05 Only - Deprecated in Z01) 
 * @author jen
 *
 */
public class ZBR8 implements Parameter {

	private String universalId;
	private String universalIdType;
	
	public ZBR8(String universalId, String universalIdType) {
	    this.universalId = universalId;
	    this.universalIdType = universalIdType;
    }

	public ZBR8() {
    }

	@Override
    public String toOlisString() {
	    return getQueryCode() + ".6.2" + (universalId != null ? universalId : "") + "~" +
	    	getQueryCode() + ".6.3" + (universalIdType != null ? universalIdType : "");
    }

	@Override
    public void setValue(Object value) {
		throw new UnsupportedOperationException();
    }

	@Override
    public void setValue(Integer part, Object value) {
		throw new UnsupportedOperationException();
    }

	@Override
    public void setValue(Integer part, Integer part2, Object value) {
	    if (part == 6 && part2 == 2)
	    	universalId = (String) value;
	    else if (part == 6 && part2 == 3)
	    	universalIdType = (String) value;
    }

	@Override
    public String getQueryCode() {
	    return "@ZBR.8";
    }

}

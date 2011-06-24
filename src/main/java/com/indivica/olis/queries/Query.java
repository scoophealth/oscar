package com.indivica.olis.queries;

public interface Query {

	public String getQueryHL7String();
	
	public QueryType getQueryType();
}

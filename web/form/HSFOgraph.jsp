<%@ page
	import="org.jfree.chart.ChartUtilities,org.jfree.chart.ChartFactory,java.util.*,java.io.*,org.jfree.data.time.TimeSeriesCollection,org.jfree.data.time.Day,org.jfree.data.time.TimeSeries,org.jfree.chart.JFreeChart"%>
%><%
OutputStream o = response.getOutputStream(); 

org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();

String id = (String) request.getParameter("id");
Hashtable[] harray = (Hashtable[]) session.getAttribute(id);


for (Hashtable dat : harray){
    String name = (String) dat.get("name");
    Hashtable[] setData = (Hashtable[]) dat.get("data");
    TimeSeries s1 = new TimeSeries(name, Day.class); 
    for(Hashtable d : setData){
        if(d !=null){
           s1.addOrUpdate(new Day( (Date) d.get("date")) ,
                Double.parseDouble(""+d.get("data")) );
        }
    }
    dataset.addSeries(s1);
}
String chartTitle = "";

JFreeChart chart = ChartFactory.createTimeSeriesChart( 
chartTitle, 
"Date", "", 
dataset, 
true, 
true, 
false 
); 


response.setContentType("image/png"); 
ChartUtilities.writeChartAsPNG(o, chart, 350, 275); 
session.removeAttribute(id);
out.close(); 
%>
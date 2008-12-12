<%@ page
	import="org.jfree.chart.ChartUtilities,org.jfree.chart.ChartFactory,java.util.*,java.io.*,org.jfree.data.time.TimeSeriesCollection,org.jfree.data.time.Day,org.jfree.data.time.TimeSeries,org.jfree.chart.JFreeChart"%>
%><%
OutputStream o = response.getOutputStream(); 

org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();

String id = (String) request.getParameter("id");
Hashtable[] harray = (Hashtable[]) session.getAttribute(id);


for (Hashtable dat : harray){
    String name = (String) dat.get("name");
    System.out.println("name is "+name);
    Hashtable[] setData = (Hashtable[]) dat.get("data");
    TimeSeries s1 = new TimeSeries(name, Day.class); 
    for(Hashtable d : setData){
        System.out.println(d);
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
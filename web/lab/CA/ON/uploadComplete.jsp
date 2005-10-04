<%@page contentType="text/xml"%><%@page import="javax.xml.parsers.*, org.w3c.dom.*, oscar.util.*"%><%
DocumentBuilder builder = DocumentBuilderFactory.newInstance().newDocumentBuilder();
Document doc = builder.newDocument();

Element element = doc.createElement("labUploadResult");
doc.appendChild(element);
Element outcome = doc.createElement("outcome");
element.appendChild(outcome);
String s = (String) request.getAttribute("outcome");
if (s == null) s = "accessDenied";
outcome.appendChild(doc.createTextNode(s));
out.write(UtilXML.toXML(doc));
%>


<%@page import="oscar.oscarRx.data.RxDrugData,java.util.*,oscar.oscarRx.util.*" %>
<ul>
<%
String searchString = request.getParameter("searchString");

RxDrugData drugData = new RxDrugData();
RxDrugRef rxref = new RxDrugRef();

RxDrugData.DrugSearch drugSearch = null;

if (searchString != null){

  System.out.println(" ### in search2.jsp, searchString="+searchString);
    
    //drugSearch = drugData.listDrugFromElement(searchString);
    //drugSearch = drugData.listDrugByRoute(searchString, searchRoute);
    //drugSearch = drugData.

    Vector<Hashtable> vec = rxref.list_drug_element2(searchString);
  //  System.out.println("vec size="+""+vec.size());
    for(Hashtable drug : vec){
        String cat = ""+drug.get("category");
        if (cat.equals("18")){
  //          System.out.println("drug.get(id)="+drug.get("id"));
  //          System.out.println("drug.get(name)="+drug.get("name"));
        %>
        <li id="g_<%=drug.get("id")%>"><%=drug.get("name")%></li>
    <%}

    }

    
    ArrayList<RxDrugData.MinDrug> genList = new ArrayList();//  drugSearch.getGen();
    if(drugSearch != null){
        for(RxDrugData.MinDrug drug: genList){
    %>
        <li id="g_<%=drug.pKey%>"><%=drug.name%></li>
    <%
        }
    }

    ArrayList genWBrand = new ArrayList();

    ArrayList<RxDrugData.MinDrug> brandList =  new ArrayList();//drugSearch.getBrand();
    if(drugSearch != null){
        for(RxDrugData.MinDrug drug: brandList){
            //String genName = drugData.getGenericName(drug.pKey);
            //if (!genWBrand.contains(genName)){
             //   genWBrand.add(genName);
                %>
                <li id="b_<%=drug.pKey%>" ><%=drug.name%>  </li>
                <%
         //}
        }
    }
}
%>
</ul>

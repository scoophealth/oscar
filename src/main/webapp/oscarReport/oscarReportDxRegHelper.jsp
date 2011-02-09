<%@page import="java.util.Iterator"%>
<%@page import="java.util.List"%>
<%@page import="org.oscarehr.common.dao.Icd9Dao"%>
<%@page import="org.oscarehr.common.model.Icd9"%>
<%@ include file="/common/webAppContextAndSuperMgr.jsp"%>
<%

            Icd9Dao icd9dao = (Icd9Dao) webApplicationContext.getBean("Icd9DAO");


            String query = request.getParameter("q");

            List<Icd9> Icd9List = icd9dao.getIcd9(query);

            if (Icd9List != null && Icd9List.size() > 0) {
                Iterator<Icd9> iterator = Icd9List.iterator();
                while (iterator.hasNext()) {
                    Icd9 icd9 = (Icd9) iterator.next();
                    String code = icd9.getIcd9();
                    String description = icd9.getDescription();
                    out.println(code + " --> " + description);
                }
            }
%>
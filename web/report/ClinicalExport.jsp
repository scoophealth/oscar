
<%@page import="org.oscarehr.util.MiscUtils"%><%@page import="org.apache.poi.hssf.usermodel.HSSFRow,org.apache.poi.hssf.usermodel.HSSFSheet,org.apache.poi.hssf.usermodel.HSSFWorkbook,com.Ostermiller.util.CSVParser"%><%

    String csv = (String) session.getAttribute("clinicalReportCSV");
    String action = request.getParameter("getCSV");
        if (action != null) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"oscarReport.csv\"");
            try {
                response.getWriter().write(csv);
            } catch (Exception ioe) {
            	MiscUtils.getLogger().error("Error", ioe);
            }
        }
        action = request.getParameter("getXLS");
        if (action != null) {
            response.setContentType("application/octet-stream");
            response.setHeader("Content-Disposition", "attachment; filename=\"oscarReport.xls\"");
            String[][] data = CSVParser.parse(csv);
            HSSFWorkbook wb = new HSSFWorkbook();
            HSSFSheet sheet = wb.createSheet("OSCAR_Report");
            for (int x=0; x<data.length; x++) {
                HSSFRow row = sheet.createRow((short)x);
                for (int y=0; y<data[x].length; y++) {
                    try{
                       double d = Double.parseDouble(data[x][y]);
                        row.createCell((short)y).setCellValue(d);
                    }catch(Exception e){
                       row.createCell((short)y).setCellValue(data[x][y]);
                    }
                }
            }
            try {    
                wb.write(response.getOutputStream());
            } catch(Exception e) {
            	MiscUtils.getLogger().error("Error", e);
            }
            
        }
       

%>
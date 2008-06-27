/*
 * 
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 * This software is published under the GPL GNU General Public License. 
 * This program is free software; you can redistribute it and/or 
 * modify it under the terms of the GNU General Public License 
 * as published by the Free Software Foundation; either version 2 
 * of the License, or (at your option) any later version. * 
 * This program is distributed in the hope that it will be useful, 
 * but WITHOUT ANY WARRANTY; without even the implied warranty of 
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
 * GNU General Public License for more details. 
 * 
 * You should have received a copy of the GNU General Public License 
 * along with this program; if not, write to the Free Software 
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
 * 
 * <OSCAR TEAM>
 * 
 * This software was written for the 
 * Department of Family Medicine 
 * McMaster University 
 * Hamilton 
 * Ontario, Canada 
 */
package oscar.oscarEncounter.oscarMeasurements.pageUtil;

import java.awt.Color;
import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.Collection;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;
import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartUtilities;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.axis.NumberAxis;
import org.jfree.chart.labels.StandardXYToolTipGenerator;
import org.jfree.chart.plot.XYPlot;
import org.jfree.chart.renderer.xy.StandardXYItemRenderer;
import org.jfree.chart.renderer.xy.XYItemRenderer;
import org.jfree.data.time.Day;
import org.jfree.data.time.TimeSeries;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBean;
import oscar.oscarEncounter.oscarMeasurements.bean.EctMeasurementsDataBeanHandler;

/**
 *
 * @author jaygallagher
 */
public class MeasurementGraphAction2 extends Action {

    private static Log log = LogFactory.getLog(MeasurementGraphAction2.class);
        
    public ActionForward execute(ActionMapping mapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        log.debug("In MeasurementGraphAction2");
        String userrole = (String) request.getSession().getAttribute("userrole");
        if (userrole == null) response.sendRedirect("../logout.jsp");
        String roleName$ = (String) userrole + "," + (String) request.getSession().getAttribute("user");

        
        String demographicNo = request.getParameter("demographic_no");
        String typeIdName = request.getParameter("type");
        String typeIdName2 = request.getParameter("type2");
        
        
        String patientName = oscar.oscarDemographic.data.DemographicNameAgeString.getInstance().getNameAgeString(demographicNo);
        String chartTitle = "Data Graph for " + patientName;

        log.debug("Creating graph for demo "+demographicNo+" type1 :"+typeIdName+" type2 :"+typeIdName2);
        
        
        org.jfree.data.time.TimeSeriesCollection dataset = new org.jfree.data.time.TimeSeriesCollection();
       
        ArrayList<EctMeasurementsDataBean> list = getList( demographicNo,  typeIdName);

        String typeYAxisName = "";
        
        if (typeIdName.equals("BP")){
            log.debug("Using BP LOGIC FOR type 1 ");
            EctMeasurementsDataBean sampleLine = (EctMeasurementsDataBean) list.get(0);
            typeYAxisName = sampleLine.getTypeDescription(); 
            TimeSeries systolic = new TimeSeries("Systolic", Day.class);
            TimeSeries diastolic = new TimeSeries("Diastolic", Day.class);
            for (EctMeasurementsDataBean mdb : list){ // dataVector) {
                String[] str = mdb.getDataField().split("/");
                
                systolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[0]));
                diastolic.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(str[1]));
            }
            dataset.addSeries(diastolic);
            dataset.addSeries(systolic);
        
        }else{
            log.debug("Not Using BP LOGIC FOR type 1 ");
            // get the name from the TimeSeries
            EctMeasurementsDataBean sampleLine = (EctMeasurementsDataBean) list.get(0);
            String typeLegendName = sampleLine.getTypeDisplayName();
            typeYAxisName = sampleLine.getTypeDescription(); // this should be the type of measurement

            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list ) { //dataVector) {
                newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
            }
            dataset.addSeries(newSeries);
        }
      
        
        
        
        JFreeChart chart = ChartFactory.createTimeSeriesChart(chartTitle, "Days", typeYAxisName, dataset, true, true, true);
        
        if (typeIdName2 != null){
            log.debug("type id name 2"+typeIdName2);
            
            ArrayList<EctMeasurementsDataBean> list2 = getList( demographicNo,  typeIdName2);
            org.jfree.data.time.TimeSeriesCollection dataset2 = new org.jfree.data.time.TimeSeriesCollection();
            
            log.debug("list2 "+list2);
            
            
            EctMeasurementsDataBean sampleLine2 = (EctMeasurementsDataBean) list2.get(0);
            String typeLegendName = sampleLine2.getTypeDisplayName();
            String typeYAxisName2 = sampleLine2.getTypeDescription(); // this should be the type of measurement

            TimeSeries newSeries = new TimeSeries(typeLegendName, Day.class);
            for (EctMeasurementsDataBean mdb : list2 ) { //dataVector) {

                newSeries.addOrUpdate(new Day(mdb.getDateObservedAsDate()), Double.parseDouble(mdb.getDataField()));
            }
            dataset2.addSeries(newSeries);
            
            ////
            final XYPlot plot = chart.getXYPlot();
            final NumberAxis axis2 = new NumberAxis(typeYAxisName2);
            axis2.setAutoRangeIncludesZero(false);
            plot.setRangeAxis(1, axis2);
            plot.setDataset(1, dataset2);
            plot.mapDatasetToRangeAxis(1, 1);
            final XYItemRenderer renderer = plot.getRenderer();
            renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
            if (renderer instanceof StandardXYItemRenderer) {
                final StandardXYItemRenderer rr = (StandardXYItemRenderer) renderer;
                //rr.setPlotShapes(true);
                rr.setShapesFilled(true);
            }

            final StandardXYItemRenderer renderer2 = new StandardXYItemRenderer();
            renderer2.setSeriesPaint(0, Color.black);
            //renderer2.setPlotShapes(true);
            renderer.setToolTipGenerator(StandardXYToolTipGenerator.getTimeSeriesInstance());
            plot.setRenderer(1, renderer2);
            ////
            
        }
        
        
        
        
        response.setContentType("image/png");
        OutputStream o = response.getOutputStream();
        ChartUtilities.writeChartAsPNG(o, chart, 800, 400);
        o.close();


        return null;
    }
    
    
        ArrayList<EctMeasurementsDataBean> getList(String demographicNo, String typeIdName){
           EctMeasurementsDataBeanHandler ectMeasure = new EctMeasurementsDataBeanHandler(demographicNo, typeIdName);
           Collection<EctMeasurementsDataBean> dataVector = ectMeasure.getMeasurementsDataVector();
           ArrayList<EctMeasurementsDataBean> list = new ArrayList(dataVector);
        return list;
        }
    
    
}

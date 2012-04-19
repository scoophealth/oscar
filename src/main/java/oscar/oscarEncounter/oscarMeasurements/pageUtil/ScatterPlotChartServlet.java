/**
 * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 * This software is published under the GPL GNU General Public License.
 * This program is free software; you can redistribute it and/or
 * modify it under the terms of the GNU General Public License
 * as published by the Free Software Foundation; either version 2
 * of the License, or (at your option) any later version. 
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 * This software was written for the
 * Department of Family Medicine
 * McMaster University
 * Hamilton
 * Ontario, Canada
 */


package oscar.oscarEncounter.oscarMeasurements.pageUtil;


import java.awt.Color;
import java.awt.Font;
import java.awt.Paint;
import java.awt.Shape;
import java.awt.Stroke;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.sql.Date;
import java.sql.ResultSet;
import java.sql.SQLException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.jCharts.axisChart.AxisChart;
import org.jCharts.axisChart.ScatterPlotAxisChart;
import org.jCharts.chartData.AxisChartDataSet;
import org.jCharts.chartData.DataSeries;
import org.jCharts.chartData.ScatterPlotDataSeries;
import org.jCharts.chartData.ScatterPlotDataSet;
import org.jCharts.encoders.ServletEncoderHelper;
import org.jCharts.properties.AxisProperties;
import org.jCharts.properties.ChartProperties;
import org.jCharts.properties.DataAxisProperties;
import org.jCharts.properties.LegendProperties;
import org.jCharts.properties.LineChartProperties;
import org.jCharts.properties.PointChartProperties;
import org.jCharts.properties.ScatterPlotProperties;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.types.ChartType;
import org.oscarehr.util.MiscUtils;

import oscar.oscarDB.DBHandler;
import oscar.oscarEncounter.pageUtil.EctSessionBean;

public class ScatterPlotChartServlet extends HttpServlet
{
	//---all of my charts serviced by this Servlet will have the same properties.
	protected LegendProperties legendProperties;
	protected AxisProperties axisProperties;
	protected ChartProperties chartProperties;

	protected int width = 550;
	protected int height = 360;


	/**********************************************************************************************
	 *
	 **********************************************************************************************/
	public void init()
	{
		this.legendProperties = new LegendProperties();
		this.chartProperties = new ChartProperties();
		this.axisProperties = new AxisProperties( true );
		ChartFont axisScaleFont = new ChartFont( new Font( "Georgia Negreta cursiva", Font.PLAIN, 13 ), Color.black );
		axisProperties.getXAxisProperties().setScaleChartFont( axisScaleFont );
		axisProperties.getYAxisProperties().setScaleChartFont( axisScaleFont );

		ChartFont axisTitleFont = new ChartFont( new Font( "Arial Narrow", Font.PLAIN, 14 ), Color.black );
		axisProperties.getXAxisProperties().setTitleChartFont( axisTitleFont );
		axisProperties.getYAxisProperties().setTitleChartFont( axisTitleFont );

		ChartFont titleFont = new ChartFont( new Font( "Georgia Negreta cursiva", Font.PLAIN, 14 ), Color.black );
		this.chartProperties.setTitleFont( titleFont );
	}


	/******************************************************************************************
	 *
	 *
	 ******************************************************************************************/
	private ScatterPlotProperties createScatterPlotProperties()
	{
		Stroke[] strokes = new Stroke[]{LineChartProperties.DEFAULT_LINE_STROKE};
		Shape[] shapes = new Shape[]{PointChartProperties.SHAPE_CIRCLE};

		return new ScatterPlotProperties( strokes, shapes );
	}
        
        /******************************************************************************************
	 *
	 *
	 ******************************************************************************************/
	private LineChartProperties createLineChartProperties()
	{
		Stroke[] strokes= { LineChartProperties.DEFAULT_LINE_STROKE, LineChartProperties.DEFAULT_LINE_STROKE };
		Shape[] shapes= { PointChartProperties.SHAPE_TRIANGLE, PointChartProperties.SHAPE_CIRCLE };

		return new LineChartProperties( strokes, shapes );
	}


	/*****************************************************************************************
	 * Generates dataset
	 *
	 * @return scatterPlotDataSeries
	 ******************************************************************************************/
	private ScatterPlotDataSeries createScatterPlotDataSeries(String demo, String type, String mInstrc)
	{                
                long[][] results = generateResult(demo, type, mInstrc);
                String chartTitle = type + "-" + mInstrc;                
                ScatterPlotDataSet scatterPlotDataSet = new ScatterPlotDataSet( this.createScatterPlotProperties() );
                ScatterPlotDataSeries scatterPlotDataSeries = null;
                
                if (results!=null){
                                        
                    if (type.compareTo("BP")!=0){                        
                        Point2D.Double[] points = new Point2D.Double[ results[0].length ];
                        for( int x = 0; x < results[0].length; x++ )
                        {                                
                                points[ x ] = ScatterPlotDataSet.createPoint2DDouble();
                                points[ x ].setLocation( results[0][x]-results[0][0], results[1][x] );
                        }                    
                       
        
                        scatterPlotDataSet.addDataPoints( points, Color.red, chartTitle );                    
                        scatterPlotDataSeries = new ScatterPlotDataSeries( scatterPlotDataSet,
                                                                           "Day (note: only the last data on the same observation date is plotted)",
                                                                           "Test Results",
                                                                            chartTitle);
                    }
                }
                
		return scatterPlotDataSeries;
	}

	/*****************************************************************************************
	 * Generates Blood pressure dataset
	 *
	 * @return DataSeries
	 ******************************************************************************************/
	private DataSeries createBloodPressureDataSeries(String demo, String type, String mInstrc)
	{
                
                long[][] results = generateResult(demo, type, mInstrc);
                DataSeries dataSeries = null;           
                String chartTitle = type + "-" + mInstrc;                
                String xAxisTitle= "Tests (note: only the last data on the same observation date is plotted)";
                String yAxisTitle= "Hgmm";
                
                if (results!=null){
                                        
                    if (type.compareTo("BP")==0){
                        double[][] points = new double[2][results[1].length/2];
                        String[] legendLabels= { "Systolic", "Diastolic"};
                        Paint[] paints= {Color.red, Color.blue};
                        LineChartProperties lineChartProperties = this.createLineChartProperties();                        
                        int offset = results[1].length/2;                               
                        String[] xAxisLabels= new String[offset];
                        
                        for( int x = 0; x < results[1].length/2; x++ )
                        {                                
                                MiscUtils.getLogger().debug("systolic" + x + " " + results[1][x]);
                                points[0][x] = results[1][x];
                                int testNum = x + 1;
                                String xAxisLabel = "test" + testNum;
                                xAxisLabels[x] = xAxisLabel;
                                MiscUtils.getLogger().debug("xAxisLabel is " + xAxisLabels[x]);
                        }                                           
                        
                        for( int x = 0; x < results[1].length/2; x++ )
                        {                                                               
                                MiscUtils.getLogger().debug("Diastolic" + x + results[1][x+offset]);
                                points[1][x] = results[1][x+offset];                                                               
                        }
                        
                        try{
                            AxisChartDataSet acds = new AxisChartDataSet(points, legendLabels, paints,ChartType.LINE, lineChartProperties );
                            dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle, chartTitle );
                            dataSeries.addIAxisPlotDataSet(acds);
                            MiscUtils.getLogger().debug("the diastolic data has been added successfully");
                        }
                        catch(Exception e)
                        {
                                MiscUtils.getLogger().debug("debug", e);
                        }                                                                        
                        
                    }
                    
                }               
            return dataSeries;
	}

        /*****************************************************************************************
	 * Generates generate result from the database
	 *
	 * @return DataSeries
	 ******************************************************************************************/			                
        private long[][] generateResult(String demo, String type, String mInstrc){
            //plot only last data of the day?!
                        
            long[][] points = null;
            
            try{
                if(isNumeric(type, mInstrc)){
                    
                    String sql = "SELECT DISTINCT dateObserved FROM measurements WHERE demographicNo = '" + demo + "' AND type='"+ type + "' AND measuringInstruction='" + mInstrc 
                                 + "' ORDER BY dateObserved";
                    MiscUtils.getLogger().debug("SQL Statement: " + sql);
                    ResultSet rs;
                    rs = DBHandler.GetSQL(sql);                
                    rs.last();
                    int nbData = rs.getRow();
                    rs.first();
                    points = new long[2][nbData];
                    
                    for(int i=0; i<nbData; i++){ 
                        sql =   "SELECT * FROM measurements WHERE demographicNo='" + demo + "' AND type='"+ type 
                                + "' AND dateObserved='"+oscar.Misc.getString(rs, "dateObserved") + "' ORDER BY dateEntered DESC limit 1";
                        ResultSet rsData;
                        rsData = DBHandler.GetSQL(sql);
                        if(rsData.next()){
                            Date dateObserved = rs.getDate("dateObserved");
                            points[0][i] = dateObserved.getTime()/1000/60/60/24;                        
                            points[1][i] = rsData.getLong("dataField");
                            MiscUtils.getLogger().debug("Date: " + points[0][i] + " Value: " + points[1][i]);
                        }
                        rsData.close();
                        rs.next();
                    }
                    rs.close();
                }
                else if (type.compareTo("BP")==0){
                    
                    String sql = "SELECT dateObserved FROM measurements WHERE demographicNo = '" + demo + "' AND type='"+ type + "' AND measuringInstruction='" + mInstrc 
                                 + "' GROUP BY dateObserved ORDER BY dateObserved";
                    MiscUtils.getLogger().debug("SQL Statement: " + sql);
                    ResultSet rs;
                    rs = DBHandler.GetSQL(sql);                
                    rs.last();                    
                    int nbPatient = rs.getRow();
                    rs.first();
                    rs.previous();
                    points = new long[2][nbPatient*2];                    
                    
                   
                    MiscUtils.getLogger().debug("number of record: " + Integer.toString(nbPatient));
                    for(int i=0; i<nbPatient; i++){
                        if(rs.next()){                            
                            sql =   "SELECT * FROM measurements WHERE demographicNo='" + demo + "' AND type='"+ type 
                                    + "' AND dateObserved='"+oscar.Misc.getString(rs, "dateObserved") + "' ORDER BY dateEntered DESC limit 1";
                            MiscUtils.getLogger().debug("sql dateObserved: " + sql);
                            ResultSet rsData;
                            rsData = DBHandler.GetSQL(sql);
                            if(rsData.next()){
                                String bloodPressure = rsData.getString("dataField");
                                MiscUtils.getLogger().debug("bloodPressure: " + bloodPressure);
                                int slashIndex = bloodPressure.indexOf("/");            
                                if (slashIndex >= 0){
                                    String systolic = bloodPressure.substring(0, slashIndex);
                                    Date dateObserved = rs.getDate("dateObserved");
                                    points[0][i] = dateObserved.getTime()/1000/60/60/24;                                
                                    points[1][i] = Long.parseLong(systolic);
                                    MiscUtils.getLogger().debug("systolic: " + i + " " + systolic);

                                    String diastolic = bloodPressure.substring(slashIndex+1);
                                    points[0][i+nbPatient] = dateObserved.getTime()/1000/60/60/24;
                                    points[1][i+nbPatient] = Long.parseLong(diastolic);
                                    MiscUtils.getLogger().debug("diastolic: " + points[1][i+nbPatient]);
                                }                                                                          
                            }
                            rsData.close();
                        }
                    }
                    /*for(int i=0; i<nbPatient; i++){ 
                        MiscUtils.getLogger().debug("the result is: " + points[i]);
                    }*/
                    MiscUtils.getLogger().debug("Store blood pressure data to a new array successfully" );
                    rs.close();
                }
                
            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
            
            return points;
        }
        
        private boolean isNumeric(String type, String mInstrc){
            boolean isNumeric = false;
            
            try{
                
                String sql = "SELECT * FROM measurementType WHERE type='"+ type + "' AND measuringInstruction='" + mInstrc + "'";
                MiscUtils.getLogger().debug("SQL Statement: " + sql);
                ResultSet rs;
                rs = DBHandler.GetSQL(sql);                
                rs.next();
                String validation = oscar.Misc.getString(rs, "validation");
                rs.close();
                
                sql = "SELECT * FROM validations WHERE id='"+ validation + "'";
                rs = DBHandler.GetSQL(sql);
                rs.next();
                if(rs.getInt("isNumeric")==1){
                    isNumeric = true;
                }
                
                rs.close();

            }
            catch(SQLException e)
            {
                MiscUtils.getLogger().error("Error", e);
            }
            
            return isNumeric;
        }
	/**********************************************************************************************
	 *
	 **********************************************************************************************/
	public void service( HttpServletRequest request, HttpServletResponse httpServletResponse ) throws ServletException, IOException
	{		
                String type = request.getParameter("type");
                String mInstrc = request.getParameter("mInstrc");                
                EctSessionBean bean = (EctSessionBean)request.getSession().getAttribute("EctSessionBean");
                String demographicNo = null;
                if ( bean != null){
                    demographicNo = bean.getDemographicNo();                    
                }
                try
		{			
                        //addIAxisPlotDataSet(IAxisPlotDataSet 
			DataAxisProperties xAxisProperties = new DataAxisProperties();	                        
			DataAxisProperties yAxisProperties = new DataAxisProperties();						
                        
			ChartProperties chartProperties = new ChartProperties();
			LegendProperties legendProperties = new LegendProperties();

                        if(type.compareTo("BP")==0){
                            AxisProperties axisProperties = new AxisProperties( false );
                            DataSeries dataSeries = this.createBloodPressureDataSeries(demographicNo, type, mInstrc);
                            AxisChart axisChart = new AxisChart(dataSeries, chartProperties, axisProperties,legendProperties, 550, 360);
                            ServletEncoderHelper.encodeJPEG13( axisChart, 1.0f, httpServletResponse );
                        }
                        else{ 
                            AxisProperties axisProperties = new AxisProperties(xAxisProperties, yAxisProperties);
                            ScatterPlotDataSeries scatterPlotDataSeries = this.createScatterPlotDataSeries(demographicNo, type, mInstrc);
                            if(scatterPlotDataSeries!=null){                                
                                ScatterPlotAxisChart scatterPlotAxisChart = new ScatterPlotAxisChart( scatterPlotDataSeries,
                                                                                                         chartProperties,
                                                                                                         axisProperties,
                                                                                                         legendProperties,
                                                                                                         500,
                                                                                                         400 );

                                ServletEncoderHelper.encodeJPEG13( scatterPlotAxisChart, 1.0f, httpServletResponse );
                            }
                        }
		}
		catch( Throwable t )
		{			
			MiscUtils.getLogger().error("Error", t);
		}

	}
}

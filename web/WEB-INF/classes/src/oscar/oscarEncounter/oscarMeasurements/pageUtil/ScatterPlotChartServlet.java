// -----------------------------------------------------------------------------------------------------------------------
// *
// *
// * Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
// * This software is published under the GPL GNU General Public License. 
// * This program is free software; you can redistribute it and/or 
// * modify it under the terms of the GNU General Public License 
// * as published by the Free Software Foundation; either version 2 
// * of the License, or (at your option) any later version. * 
// * This program is distributed in the hope that it will be useful, 
// * but WITHOUT ANY WARRANTY; without even the implied warranty of 
// * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
// * GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
// * along with this program; if not, write to the Free Software 
// * Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
// * 
// * <OSCAR TEAM>
// * This software was written for the 
// * Department of Family Medicine 
// * McMaster Unviersity 
// * Hamilton 
// * Ontario, Canada 
// *
// -----------------------------------------------------------------------------------------------------------------------

package oscar.oscarEncounter.oscarMeasurements.pageUtil;


import org.jCharts.axisChart.*;
import org.jCharts.chartData.*;
import org.jCharts.encoders.ServletEncoderHelper;
import org.jCharts.properties.*;
import org.jCharts.properties.util.ChartFont;
import org.jCharts.test.TestDataGenerator;
import org.jCharts.types.ChartType;

import javax.servlet.ServletException;
import javax.servlet.http.*;
import java.awt.*;
import java.awt.geom.Point2D;
import java.io.IOException;
import java.lang.*;
import java.sql.ResultSet;
import java.sql.SQLException;

import oscar.oscarDB.DBHandler;
import oscar.OscarProperties;
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
                
                double[] results = generateResult(demo, type, mInstrc);
                String chartTitle = type + "-" + mInstrc;                
                ScatterPlotDataSet scatterPlotDataSet = new ScatterPlotDataSet( this.createScatterPlotProperties() );
                ScatterPlotDataSeries scatterPlotDataSeries = null;
                
                if (results!=null){
                                        
                    if (type.compareTo("BP")!=0){                        
                        Point2D.Double[] points = new Point2D.Double[ results.length ];
                        for( int x = 0; x < results.length; x++ )
                        {                                
                                points[ x ] = ScatterPlotDataSet.createPoint2DDouble();
                                points[ x ].setLocation( x, results[x] );
                        }                    
                       
                        scatterPlotDataSet.addDataPoints( points, Color.red, chartTitle );                    
                        scatterPlotDataSeries = new ScatterPlotDataSeries( scatterPlotDataSet,
                                                                           "Test Performed",
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
                
                double[] results = generateResult(demo, type, mInstrc);
                DataSeries dataSeries = null;           
                String chartTitle = type + "-" + mInstrc;                
                String xAxisTitle= "Tests";
                String yAxisTitle= "Hgmm";
                
                if (results!=null){
                                        
                    if (type.compareTo("BP")==0){
                        double[][] points = new double[2][results.length/2];
                        String[] legendLabels= { "Systolic", "Diastolic"};
                        Paint[] paints= {Color.red, Color.blue};
                        LineChartProperties lineChartProperties = this.createLineChartProperties();                        
                        int offset = results.length/2;                               
                        String[] xAxisLabels= new String[offset];
                        
                        for( int x = 0; x < results.length/2; x++ )
                        {                                
                                System.out.println("systolic" + x + " " + results[x]);
                                points[0][x] = results[x];
                                int testNum = x + 1;
                                String xAxisLabel = "test" + testNum;
                                xAxisLabels[x] = xAxisLabel;
                                System.out.println("xAxisLabel is " + xAxisLabels[x]);
                        }                                           
                        
                        for( int x = 0; x < results.length/2; x++ )
                        {                                                               
                                System.out.println("Diastolic" + x + results[x+offset]);
                                points[1][x] = results[x+offset];                                                               
                        }
                        
                        try{
                            AxisChartDataSet acds = new AxisChartDataSet(points, legendLabels, paints,ChartType.LINE, lineChartProperties );
                            dataSeries = new DataSeries( xAxisLabels, xAxisTitle, yAxisTitle, chartTitle );
                            dataSeries.addIAxisPlotDataSet(acds);
                            System.out.println("the diastolic data has been added successfully");
                        }
                        catch(Exception e)
                        {
                                System.out.println(e);
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
        private double[] generateResult(String demo, String type, String mInstrc){
                        
            double[] points = null;
            try{
                if(isNumeric(type, mInstrc)){
                    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                    String sql = "SELECT * FROM measurements WHERE demographicNo = '" + demo + "' AND type='"+ type + "' AND measuringInstruction='" + mInstrc 
                                 + "' ORDER BY dateObserved";
                    System.out.println("SQL Statement: " + sql);
                    ResultSet rs;
                    rs = db.GetSQL(sql);                
                    rs.last();
                    int nbPatient = rs.getRow();
                    rs.first();
                    points = new double[nbPatient];

                    for(int i=0; i<nbPatient; i++){
                        points[i] = rs.getDouble("dataField");
                        rs.next();
                    }
                    rs.close();
                    db.CloseConn();
                }
                else if (type.compareTo("BP")==0){
                    DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                    String sql = "SELECT * FROM measurements WHERE demographicNo = '" + demo + "' AND type='"+ type + "' AND measuringInstruction='" + mInstrc 
                                 + "' ORDER BY dateObserved";
                    System.out.println("SQL Statement: " + sql);
                    ResultSet rs;
                    rs = db.GetSQL(sql);                
                    rs.last();
                    int nbPatient = rs.getRow();
                    rs.first();
                    rs.previous();
                    points = new double[nbPatient*2];                    
                    boolean hasNext = true;
                    System.out.println("size of array: " + points.length);
                    for(int i=0; i<nbPatient; i++){                        
                        if(rs.next()){
                            String bloodPressure = rs.getString("dataField");
                            int slashIndex = bloodPressure.indexOf("/");            
                            if (slashIndex >= 0){
                                String systolic = bloodPressure.substring(0, slashIndex);
                                points[i] = Double.parseDouble(systolic);
                                System.out.println("systolic: " + i + " " + systolic);

                                String diastolic = bloodPressure.substring(slashIndex+1);
                                points[i+nbPatient] = Double.parseDouble(diastolic);
                                System.out.println("diastolic: " + points[i+nbPatient]);
                            }                                                                          
                        }
                    }
                    for(int i=0; i<nbPatient; i++){ 
                        System.out.println("the result is: " + points[i]);
                    }
                    System.out.println("Store blood pressure data to a new array successfully" );
                    rs.close();
                    db.CloseConn();
                }
                
            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            
            return points;
        }
        
        private boolean isNumeric(String type, String mInstrc){
            boolean isNumeric = false;
            
            try{
                DBHandler db = new DBHandler(DBHandler.OSCAR_DATA);
                String sql = "SELECT * FROM measurementType WHERE type='"+ type + "' AND measuringInstruction='" + mInstrc + "'";
                System.out.println("SQL Statement: " + sql);
                ResultSet rs;
                rs = db.GetSQL(sql);                
                rs.next();
                String validation = rs.getString("validation");
                rs.close();
                
                sql = "SELECT * FROM validations WHERE id='"+ validation + "'";
                rs = db.GetSQL(sql);
                rs.next();
                if(rs.getInt("isNumeric")==1){
                    isNumeric = true;
                }
                
                rs.close();
                db.CloseConn();

            }
            catch(SQLException e)
            {
                System.out.println(e.getMessage());
            }
            
            return isNumeric;
        }
	/**********************************************************************************************
	 *
	 **********************************************************************************************/
	public void service( HttpServletRequest request, HttpServletResponse httpServletResponse ) throws ServletException, IOException
	{		
                String type = (String) request.getParameter("type");
                String mInstrc = (String) request.getParameter("mInstrc");                
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
		catch( Throwable throwable )
		{			
			throwable.printStackTrace();
		}

	}
}

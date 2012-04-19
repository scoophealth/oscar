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


package oscar.eform.util;

import java.awt.BasicStroke;
import java.awt.Color;
import java.awt.Font;
import java.awt.Graphics2D;
import java.awt.image.BufferedImage;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import javax.imageio.ImageIO;

public class GraphicalCanvasToImage {

	Color getColor(String name) {
		if(name.equals("black")) {
			return Color.BLACK;
		}
		if(name.equals("grey")) {
			return Color.GRAY;
		}
		if(name.equals("magenta")) {
			return Color.MAGENTA;
		}
		if(name.equals("red")) {
			return Color.RED;
		}
		if(name.equals("yellow")) {
			return Color.YELLOW;
		}
		if(name.equals("blue")) {
			return Color.BLUE;
		}
		if(name.equals("cyan")) {
			return Color.CYAN;
		}
		if(name.equals("lime")) {
			return new Color(50,205,50);
		}
		
		return Color.BLACK;
	}
	
	void drawFreehand(Graphics2D ig2,int[] x, int[] y, String strokeColor, int strokeThickness) {		
		ig2.setColor(getColor(strokeColor));
		ig2.setStroke(new BasicStroke(strokeThickness));
		if(x.length>1) {
			ig2.drawPolyline(x, y, x.length);			
		}
	}
	
	void drawText(Graphics2D ig2, int x, int y, String txt, String strokeColor, int fontSize) {
		ig2.setColor(getColor(strokeColor));
	     Font font = new Font("sans-serif", Font.PLAIN, fontSize);	     
	     ig2.setFont(font);
	     int y1 = y-10;
	     if(txt != null && !txt.equals("")) {
	    	 ig2.drawString(txt, x, y1);
	     }		
	}
	
	void drawSymbolPattern(Graphics2D ig2, int[] x, int[] y, String sym, String strokeColor, int symbolSize) {
		ig2.setColor(getColor(strokeColor));
		ig2.setFont(new Font("sans-serif", Font.PLAIN, symbolSize));
		if(sym != null && !sym.isEmpty()) {
			for(int j=0;j<x.length;j++) {
				int xtmp = x[j];
				int ytmp = y[j]-10;				
				ig2.drawString(sym, xtmp, ytmp);
			}
		}
	}
	
	void redrawImage(Graphics2D ig2, String[] parameters)  {
		String drawingType = parameters[0];
		if(drawingType.equals("Freehand")) {
			String[] xstr = parameters[1].split(":");
			String[] ystr = parameters[2].split(":");
			int[] x = new int[xstr.length];
			int[] y = new int[ystr.length];
			for(int i=0;i<x.length;i++) {
				x[i] = Integer.parseInt(xstr[i]);
			}
			for(int i=0;i<y.length;i++) {
				y[i] = Integer.parseInt(ystr[i]);
			}
			String strokeColor = parameters[3];
			int strokeThickness = Integer.parseInt(parameters[4]);
			drawFreehand(ig2, x,y,strokeColor,strokeThickness);
		}
		if(drawingType.equals("Text")) {
			int x = Integer.parseInt(parameters[1]);
			int y = Integer.parseInt(parameters[2]);
			String txt = parameters[3];
			String strokeColor = parameters[4];
			int fontSize = Integer.parseInt(parameters[5]);
			drawText(ig2,x,y,txt,strokeColor,fontSize);		
		}
		
		if(drawingType.equals("SymbolPattern")) {
			String[] xstr = parameters[1].split(":");
			String[] ystr = parameters[2].split(":");
			int[] x = new int[xstr.length];
			int[] y = new int[ystr.length];
			for(int i=0;i<x.length;i++) {
				x[i] = Integer.parseInt(xstr[i]);
			}
			for(int i=0;i<y.length;i++) {
				y[i] = Integer.parseInt(ystr[i]);
			}
			String sym = parameters[3];
			String strokeColor = parameters[4];
			int symbolSize = Integer.parseInt(parameters[5]);
			drawSymbolPattern(ig2,x,y,sym,strokeColor,symbolSize);		
		}
	}
	public void convertToImage(InputStream image, String drawData, String outputFormat, OutputStream out) throws IOException {
		 BufferedImage img = ImageIO.read(image); 
        int width = img.getWidth();
        int height = img.getHeight();
        BufferedImage bi = new BufferedImage(width, height, BufferedImage.TYPE_INT_RGB);          
     	Graphics2D ig2 = bi.createGraphics();
     	ig2.drawImage(img,0,0,null);
     	       
     	String[] TempData = drawData.split(",");       
     	for(String td:TempData) {     	  
     		String[] parameters = td.split("\\|");     	  
     		redrawImage(ig2,parameters);       
     	}
              
     	ImageIO.write(bi, outputFormat, out);      
	}
	public void convertToImage(String imageFile, String drawData, String outputFormat, OutputStream out) throws IOException {   
		FileInputStream istream = new FileInputStream(imageFile);
		convertToImage(istream,drawData,outputFormat,out);
	}

}

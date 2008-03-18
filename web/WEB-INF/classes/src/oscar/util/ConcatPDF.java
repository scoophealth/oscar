package oscar.util;

/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved. *
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version. *
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details. * * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. *
 *
 *  Jason Gallagher
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada   
 *
 * ConcatPDF.java
 *
 * Created on January 5, 2006, 12:08 PM
 *
 * Edited to accept an array list of filenames
 */


/*
 * $Id: ConcatPDF.java,v 1.2 2008-03-18 19:41:35 tedleung Exp $
 * $Name:  $
 *
 * This code is free software. It may only be copied or modified
 * if you include the following copyright notice:
 *
 * This class by Mark Thompson. Copyright (c) 2002 Mark Thompson.
 *
 * This code is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.
 *
 * itext@lowagie.com
 */

/**
 * This class demonstrates copying a PDF file using iText.
 * @author Mark Thompson
 */

import java.io.FileOutputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import com.lowagie.text.Document;
import com.lowagie.text.pdf.PRAcroForm;
import com.lowagie.text.pdf.PdfCopy;
import com.lowagie.text.pdf.PdfImportedPage;
import com.lowagie.text.pdf.PdfReader;
import com.lowagie.text.pdf.SimpleBookmark;

public class ConcatPDF {
    
    
    public static void concat (ArrayList alist,String filename) {
        try{
        OutputStream os = new FileOutputStream(filename);
        concat(alist,os);
        }catch(Exception e){
            e.printStackTrace();
        }
    }
    
    
    
    
    /**
     * This class can be used to concatenate existing PDF files.
     * (This was an example known as PdfCopy.java)
     * @param args the command line arguments
     */
    public static void concat(ArrayList alist,OutputStream out) {
        
        try {
            int pageOffset = 0;
            ArrayList master = new ArrayList();
            int f =0;
            String outFile = "/Users/jay/test.pdf";
            Document document = null;
            PdfCopy  writer = null;
            System.out.println("Size of list = "+alist.size());
            while (f < alist.size()) {
                // we create a reader for a certain document
                String name = (String) alist.get(f);
                
                PdfReader reader = new PdfReader(name);
                reader.consolidateNamedDestinations();
                // we retrieve the total number of pages
                int n = reader.getNumberOfPages();
                List bookmarks = SimpleBookmark.getBookmark(reader);
                if (bookmarks != null) {
                    if (pageOffset != 0)
                        SimpleBookmark.shiftPageNumbers(bookmarks, pageOffset, null);
                    master.addAll(bookmarks);
                }
                pageOffset += n;
                System.out.println("There are " + n + " pages in " + name);

                if (f == 0) {
                    // step 1: creation of a document-object
                    document = new Document(reader.getPageSizeWithRotation(1));
                    // step 2: we create a writer that listens to the document
                    writer = new PdfCopy(document, out);
                    // step 3: we open the document
                    document.open();
                }
                // step 4: we add content
                PdfImportedPage page;
                for (int i = 0; i < n; ) {
                    ++i;
                    page = writer.getImportedPage(reader, i);
                    writer.addPage(page);
                    System.out.println("Processed page " + i);
                }
                PRAcroForm form = reader.getAcroForm();
                if (form != null)
                    writer.copyAcroForm(reader);
                f++;
            }
            if (master.size() > 0)
                writer.setOutlines(master);
            // step 5: we close the document
            document.close();
        }
        catch(Exception e) {
            e.printStackTrace();
        }
    }
   
}


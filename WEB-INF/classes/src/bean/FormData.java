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
package bean; 


public class FormData { 

private int demographic_no=0 ; 
private String form_name="" ; 
private String form_date="" ; 
private String form_provider="" ; 
private String form_data="" ; 
 
public FormData() { 
} 
 
 public void setForm_name(String str) { 
   form_name = str; 
}
 public void setForm_date(String str) { 
   form_date = str; 
}
 public void setForm_provider(String str) { 
   form_provider = str; 
}
 public void setForm_data(String str) { 
   form_data = str; 
}
 public void setDemographic_no(int str) { 
   demographic_no = str; 
}
 public String getForm_name() { 
   return form_name ; 
}
 public String getForm_date() { 
   return form_date ; 
}
 public String getForm_provider() { 
   return form_provider ; 
}
 public String getForm_data() { 
   return form_data ; 
}
 public int getDemographic_no() { 
   return demographic_no ; 
}


 
} 

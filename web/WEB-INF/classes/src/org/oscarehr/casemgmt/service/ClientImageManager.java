/*
* 
* Copyright (c) 2001-2002. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved. *
* This software is published under the GPL GNU General Public License. 
* This program is free software; you can redistribute it and/or 
* modify it under the terms of the GNU General Public License 
* as published by the Free Software Foundation; either version 2 
* of the License, or (at your option) any later version. * 
* This program is distributed in the hope that it will be useful, 
* but WITHOUT ANY WARRANTY; without even the implied warranty of 
* MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the 
* GNU General Public License for more details. * * You should have received a copy of the GNU General Public License 
* along with this program; if not, write to the Free Software 
* Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA. * 
* 
* <OSCAR TEAM>
* 
* This software was written for 
* Centre for Research on Inner City Health, St. Michael's Hospital, 
* Toronto, Ontario, Canada 
*/

package org.oscarehr.casemgmt.service;

import org.oscarehr.casemgmt.dao.ClientImageDAO;
import org.oscarehr.casemgmt.model.ClientImage;

public class ClientImageManager {
    protected ClientImageDAO clientImageDAO;

    public void setClientImageDAO(ClientImageDAO dao) {
        this.clientImageDAO = dao;
    }

    public byte[] getClientImage(String id, String image_type) {
        //System.out.println("QQQQQQQQQQQQQQQQQQQQQ"+id +image_type);
        ClientImage image = clientImageDAO.getClientImage(id, image_type);
        if (image != null) {
            return image.getImage_data();
        }
        return null;
    }

    public void saveClientImage(String id, byte[] image_data, String image_type) {
        ClientImage clientImage = new ClientImage();
        clientImage.setDemographic_no(Long.valueOf(id).longValue());
        clientImage.setImage_data(image_data);
        clientImage.setImage_type(image_type);
        clientImageDAO.saveClientImage(clientImage);
    }

    public ClientImage getClientImage(String clientId) {
        return clientImageDAO.getClientImage(clientId);
    }

    public void saveClientImage(ClientImage img) {
        clientImageDAO.saveClientImage(img);
    }

}

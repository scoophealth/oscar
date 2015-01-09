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
package org.oscarehr.integration.mchcv;

public class HCMagneticStripe {

    private String healthNumber;
    private String firstName;
    private String lastName;
    private String expiryDate;
    private String sex;
    private String birthDate;
    private String cardVersion;
    private String issueDate;

    /**
     * Constructor
     *
     * @param stripe
     */
    public HCMagneticStripe(String stripe) {

        if ((stripe == null) || (stripe.isEmpty())) {
            throw new RuntimeException("Card number is null");
        }
        
        String[] tmp = stripe.split(";",-1);
        stripe = tmp[0];
        
        if (stripe.length() < 78 || stripe.length() > 79 ) {
            throw new RuntimeException("Card number must contain 78 or 79 characters");
        }

        healthNumber = stripe.substring(8, 18);
        String[] names = stripe.substring(19, 45).split("/");
        lastName = names[0].trim();
        firstName = names[1].trim();

        birthDate = stripe.substring(54, 62);

        expiryDate = stripe.substring(46, 50);                       
        expiryDate = "20" + expiryDate;
        
        expiryDate = expiryDate + birthDate.substring(6, 8);

        sex = stripe.substring(53, 54);
        if (sex.equalsIgnoreCase("2")) {
            sex = "F";
        } else {
            sex = "M";
        }

        cardVersion = stripe.substring(62, 64);

        issueDate = stripe.substring(69, 75);        
        issueDate = "20" + issueDate;
        
    }

    public String getHealthNumber() {
        return healthNumber;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public String getExpiryDate() {
        return expiryDate;
    }

    public String getSex() {
        return sex;
    }

    public String getBirthDate() {
        return birthDate;
    }

    public String getCardVersion() {
        return cardVersion;
    }

    public String getIssueDate() {
        return issueDate;
    }
}
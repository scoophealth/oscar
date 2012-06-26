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

package oscar.entities;
/**
 * Encapsulates data from table hl7_message
 *
 */
public class Hl7Message {
  /**
   * auto_increment
   */
  private int messageId;
  private String dateTime;
  private String notes;

  /**
   * Class constructor with no arguments.
   */
  public Hl7Message() {}

  /**
   * Full constructor
   *
   * @param messageId int
   * @param dateTime String
   * @param notes String
   */
  public Hl7Message(int messageId, String dateTime, String notes) {
    this.messageId = messageId;
    this.dateTime = dateTime;
    this.notes = notes;
  }

  /**
   * Gets the messageId
   * @return int messageId
   */
  public int getMessageId() {
    return messageId;
  }

  /**
   * Gets the dateTime
   * @return String dateTime
   */
  public String getDateTime() {
    return dateTime;
  }

  /**
   * Gets the notes
   * @return String notes
   */
  public String getNotes() {
    return (notes != null ? notes : "");
  }

  /**
   * Sets the messageId
   * @param messageId int
   */
  public void setMessageId(int messageId) {
    this.messageId = messageId;
  }

  /**
   * Sets the dateTime
   * @param dateTime String
   */
  public void setDateTime(String dateTime) {
    this.dateTime = dateTime;
  }

  /**
   * Sets the notes
   * @param notes String
   */
  public void setNotes(String notes) {
    this.notes = notes;
  }

}

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
 * Encapsulates data from table hl7_orc
 *
 */
public class Hl7Orc {
  /**
   * auto_increment
   */
  private int orcId;
  private int pidId;
  private String orderControl;
  private String placerOrderNumber1;
  private String fillerOrderNumber;
  private String placerOrderNumber2;
  private String orderStatus;
  private String responseFlag;
  private String quantityTiming;
  private String parent;
  private String dateTimeOfTransaction;
  private String enteredBy;
  private String verifiedBy;
  private String orderingProvider;
  private String entererLocation;
  private String callbackPhoneNumber;
  private String orderEffectiveDateTime;
  private String orderControlCodeReason;
  private String enteringOrganization;
  private String enteringDevice;
  private String actionBy;

  /**
   * Class constructor with no arguments.
   */
  public Hl7Orc() {}

  /**
   * Full constructor
   *
   * @param orcId int
   * @param pidId int
   * @param orderControl String
   * @param placerOrderNumber1 String
   * @param fillerOrderNumber String
   * @param placerOrderNumber2 String
   * @param orderStatus String
   * @param responseFlag String
   * @param quantityTiming String
   * @param parent String
   * @param dateTimeOfTransaction String
   * @param enteredBy String
   * @param verifiedBy String
   * @param orderingProvider String
   * @param entererLocation String
   * @param callbackPhoneNumber String
   * @param orderEffectiveDateTime String
   * @param orderControlCodeReason String
   * @param enteringOrganization String
   * @param enteringDevice String
   * @param actionBy String
   */
  public Hl7Orc(int orcId, int pidId, String orderControl,
                String placerOrderNumber1, String fillerOrderNumber,
                String placerOrderNumber2, String orderStatus,
                String responseFlag, String quantityTiming, String parent,
                String dateTimeOfTransaction, String enteredBy,
                String verifiedBy, String orderingProvider,
                String entererLocation, String callbackPhoneNumber,
                String orderEffectiveDateTime,
                String orderControlCodeReason, String enteringOrganization,
                String enteringDevice, String actionBy) {
    this.orcId = orcId;
    this.pidId = pidId;
    this.orderControl = orderControl;
    this.placerOrderNumber1 = placerOrderNumber1;
    this.fillerOrderNumber = fillerOrderNumber;
    this.placerOrderNumber2 = placerOrderNumber2;
    this.orderStatus = orderStatus;
    this.responseFlag = responseFlag;
    this.quantityTiming = quantityTiming;
    this.parent = parent;
    this.dateTimeOfTransaction = dateTimeOfTransaction;
    this.enteredBy = enteredBy;
    this.verifiedBy = verifiedBy;
    this.orderingProvider = orderingProvider;
    this.entererLocation = entererLocation;
    this.callbackPhoneNumber = callbackPhoneNumber;
    this.orderEffectiveDateTime = orderEffectiveDateTime;
    this.orderControlCodeReason = orderControlCodeReason;
    this.enteringOrganization = enteringOrganization;
    this.enteringDevice = enteringDevice;
    this.actionBy = actionBy;
  }

  /**
   * Gets the orcId
   * @return int orcId
   */
  public int getOrcId() {
    return orcId;
  }

  /**
   * Gets the pidId
   * @return int pidId
   */
  public int getPidId() {
    return pidId;
  }

  /**
   * Gets the orderControl
   * @return String orderControl
   */
  public String getOrderControl() {
    return (orderControl != null ? orderControl : "");
  }

  /**
   * Gets the placerOrderNumber1
   * @return String placerOrderNumber1
   */
  public String getPlacerOrderNumber1() {
    return (placerOrderNumber1 != null ? placerOrderNumber1 : "");
  }

  /**
   * Gets the fillerOrderNumber
   * @return String fillerOrderNumber
   */
  public String getFillerOrderNumber() {
    return (fillerOrderNumber != null ? fillerOrderNumber : "");
  }

  /**
   * Gets the placerOrderNumber2
   * @return String placerOrderNumber2
   */
  public String getPlacerOrderNumber2() {
    return (placerOrderNumber2 != null ? placerOrderNumber2 : "");
  }

  /**
   * Gets the orderStatus
   * @return String orderStatus
   */
  public String getOrderStatus() {
    return (orderStatus != null ? orderStatus : "");
  }

  /**
   * Gets the responseFlag
   * @return String responseFlag
   */
  public String getResponseFlag() {
    return (responseFlag != null ? responseFlag : "");
  }

  /**
   * Gets the quantityTiming
   * @return String quantityTiming
   */
  public String getQuantityTiming() {
    return (quantityTiming != null ? quantityTiming : "");
  }

  /**
   * Gets the parent
   * @return String parent
   */
  public String getParent() {
    return (parent != null ? parent : "");
  }

  /**
   * Gets the dateTimeOfTransaction
   * @return String dateTimeOfTransaction
   */
  public String getDateTimeOfTransaction() {
    return dateTimeOfTransaction;
  }

  /**
   * Gets the enteredBy
   * @return String enteredBy
   */
  public String getEnteredBy() {
    return (enteredBy != null ? enteredBy : "");
  }

  /**
   * Gets the verifiedBy
   * @return String verifiedBy
   */
  public String getVerifiedBy() {
    return (verifiedBy != null ? verifiedBy : "");
  }

  /**
   * Gets the orderingProvider
   * @return String orderingProvider
   */
  public String getOrderingProvider() {
    return (orderingProvider != null ? orderingProvider : "");
  }

  /**
   * Gets the entererLocation
   * @return String entererLocation
   */
  public String getEntererLocation() {
    return (entererLocation != null ? entererLocation : "");
  }

  /**
   * Gets the callbackPhoneNumber
   * @return String callbackPhoneNumber
   */
  public String getCallbackPhoneNumber() {
    return (callbackPhoneNumber != null ? callbackPhoneNumber : "");
  }

  /**
   * Gets the orderEffectiveDateTime
   * @return String orderEffectiveDateTime
   */
  public String getOrderEffectiveDateTime() {
    return orderEffectiveDateTime;
  }

  /**
   * Gets the orderControlCodeReason
   * @return String orderControlCodeReason
   */
  public String getOrderControlCodeReason() {
    return (orderControlCodeReason != null ? orderControlCodeReason : "");
  }

  /**
   * Gets the enteringOrganization
   * @return String enteringOrganization
   */
  public String getEnteringOrganization() {
    return (enteringOrganization != null ? enteringOrganization : "");
  }

  /**
   * Gets the enteringDevice
   * @return String enteringDevice
   */
  public String getEnteringDevice() {
    return (enteringDevice != null ? enteringDevice : "");
  }

  /**
   * Gets the actionBy
   * @return String actionBy
   */
  public String getActionBy() {
    return (actionBy != null ? actionBy : "");
  }

  /**
   * Sets the orcId
   * @param orcId int
   */
  public void setOrcId(int orcId) {
    this.orcId = orcId;
  }

  /**
   * Sets the pidId
   * @param pidId int
   */
  public void setPidId(int pidId) {
    this.pidId = pidId;
  }

  /**
   * Sets the orderControl
   * @param orderControl String
   */
  public void setOrderControl(String orderControl) {
    this.orderControl = orderControl;
  }

  /**
   * Sets the placerOrderNumber1
   * @param placerOrderNumber1 String
   */
  public void setPlacerOrderNumber1(String placerOrderNumber1) {
    this.placerOrderNumber1 = placerOrderNumber1;
  }

  /**
   * Sets the fillerOrderNumber
   * @param fillerOrderNumber String
   */
  public void setFillerOrderNumber(String fillerOrderNumber) {
    this.fillerOrderNumber = fillerOrderNumber;
  }

  /**
   * Sets the placerOrderNumber2
   * @param placerOrderNumber2 String
   */
  public void setPlacerOrderNumber2(String placerOrderNumber2) {
    this.placerOrderNumber2 = placerOrderNumber2;
  }

  /**
   * Sets the orderStatus
   * @param orderStatus String
   */
  public void setOrderStatus(String orderStatus) {
    this.orderStatus = orderStatus;
  }

  /**
   * Sets the responseFlag
   * @param responseFlag String
   */
  public void setResponseFlag(String responseFlag) {
    this.responseFlag = responseFlag;
  }

  /**
   * Sets the quantityTiming
   * @param quantityTiming String
   */
  public void setQuantityTiming(String quantityTiming) {
    this.quantityTiming = quantityTiming;
  }

  /**
   * Sets the parent
   * @param parent String
   */
  public void setParent(String parent) {
    this.parent = parent;
  }

  /**
   * Sets the dateTimeOfTransaction
   * @param dateTimeOfTransaction String
   */
  public void setDateTimeOfTransaction(String dateTimeOfTransaction) {
    this.dateTimeOfTransaction = dateTimeOfTransaction;
  }

  /**
   * Sets the enteredBy
   * @param enteredBy String
   */
  public void setEnteredBy(String enteredBy) {
    this.enteredBy = enteredBy;
  }

  /**
   * Sets the verifiedBy
   * @param verifiedBy String
   */
  public void setVerifiedBy(String verifiedBy) {
    this.verifiedBy = verifiedBy;
  }

  /**
   * Sets the orderingProvider
   * @param orderingProvider String
   */
  public void setOrderingProvider(String orderingProvider) {
    this.orderingProvider = orderingProvider;
  }

  /**
   * Sets the entererLocation
   * @param entererLocation String
   */
  public void setEntererLocation(String entererLocation) {
    this.entererLocation = entererLocation;
  }

  /**
   * Sets the callbackPhoneNumber
   * @param callbackPhoneNumber String
   */
  public void setCallbackPhoneNumber(String callbackPhoneNumber) {
    this.callbackPhoneNumber = callbackPhoneNumber;
  }

  /**
   * Sets the orderEffectiveDateTime
   * @param orderEffectiveDateTime String
   */
  public void setOrderEffectiveDateTime(String
                                        orderEffectiveDateTime) {
    this.orderEffectiveDateTime = orderEffectiveDateTime;
  }

  /**
   * Sets the orderControlCodeReason
   * @param orderControlCodeReason String
   */
  public void setOrderControlCodeReason(String orderControlCodeReason) {
    this.orderControlCodeReason = orderControlCodeReason;
  }

  /**
   * Sets the enteringOrganization
   * @param enteringOrganization String
   */
  public void setEnteringOrganization(String enteringOrganization) {
    this.enteringOrganization = enteringOrganization;
  }

  /**
   * Sets the enteringDevice
   * @param enteringDevice String
   */
  public void setEnteringDevice(String enteringDevice) {
    this.enteringDevice = enteringDevice;
  }

  /**
   * Sets the actionBy
   * @param actionBy String
   */
  public void setActionBy(String actionBy) {
    this.actionBy = actionBy;
  }
}

/**
 *
 * Copyright (c) 2005-2012. Centre for Research on Inner City Health, St. Michael's Hospital, Toronto. All Rights Reserved.
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
 * This software was written for
 * Centre for Research on Inner City Health, St. Michael's Hospital,
 * Toronto, Ontario, Canada
 */

package org.oscarehr.PMmodule.model;

/**
 * Enumeration of the reasons for a discharge
 */
public enum DischargeReason {
    UNKNOWN,
    REQUIRES_ACUTE_CARE,
    NOT_INTERESTED,
    DOES_NOT_FIT_CRITERIA,
    NO_SPACE_AVAILABLE,
    OTHER,
    STUB_6, STUB_7, STUB_8, STUB_9,
    MEDICAL_NEEDS_EXCEED_PROVISION,
    SOCIAL_BEHAVIOUR_NEEDS_EXCEED_PROVISION,
    WITHDRAWAL_NEEDS_EXCEED_PROVISION,
    MENTAL_HEALTH_NEEDS_EXCEED_PROVISION,
    OTHER_NEEDS_EXCEED_PROVISION,
    ADMITTED_TO_LTC_FACILITY,
    COMPLETION_WITHOUT_REFERRAL,
    COMPLETION_WITH_REFERRAL,
    DEATH,
    RELOCATION,
    SERVICE_PLAN_COMPLETED,
    SUICIDE,
    WITHDRAWL_CLIENT_PREFERENCE

    /**
     * See MessageResources_program.properties for string constant explanations for discharge reasons
     */
}

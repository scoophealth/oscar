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
    OTHER_NEEDS_EXCEED_PROVISION

    /**
     * See MessageResources_program.properties for string constant explanations for discharge reasons
     */
}

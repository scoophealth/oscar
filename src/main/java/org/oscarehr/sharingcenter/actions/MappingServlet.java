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
package org.oscarehr.sharingcenter.actions;

import java.util.Set;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.struts.action.Action;
import org.apache.struts.action.ActionForm;
import org.apache.struts.action.ActionForward;
import org.apache.struts.action.ActionMapping;

import org.marc.shic.core.MappingCodeType;
import org.oscarehr.sharingcenter.dao.AffinityDomainDao;
import org.oscarehr.sharingcenter.dao.EDocMappingDao;
import org.oscarehr.sharingcenter.dao.EFormMappingDao;
import org.oscarehr.sharingcenter.dao.MiscMappingDao;
import org.oscarehr.sharingcenter.dao.SiteMappingDao;
import org.oscarehr.sharingcenter.model.AffinityDomainDataObject;
import org.oscarehr.sharingcenter.model.CodeValueDataObject;
import org.oscarehr.sharingcenter.model.EDocMapping;
import org.oscarehr.sharingcenter.model.EFormMapping;
import org.oscarehr.sharingcenter.model.MiscMapping;
import org.oscarehr.sharingcenter.model.SiteMapping;
import org.oscarehr.util.SpringUtils;

public class MappingServlet extends Action {

    public ActionForward execute(ActionMapping actionMapping, ActionForm form, HttpServletRequest request, HttpServletResponse response) throws Exception {

        // domain
        int networkId = Integer.parseInt(request.getParameter("network_id"));
        AffinityDomainDao affDao = SpringUtils.getBean(AffinityDomainDao.class);
        AffinityDomainDataObject network = null;
        network = affDao.getAffinityDomain(networkId);

        // expected parameters
        String source = request.getParameter("source");
        String siteFacilityType = request.getParameter("site_facilityType");
        String sitePracticeSetting = request.getParameter("site_practiceSetting");

        // edocs
        String[] edocTypes = request.getParameterValues("edoc_type");
        String[] edocClassCodes = request.getParameterValues("edoc_classCode");
        String[] edocTypeCodes = request.getParameterValues("edoc_typeCode");
        String[] edocFormatCodes = request.getParameterValues("edoc_formatCode");
        String[] edocContentTypeCodes = request.getParameterValues("edoc_contentTypeCode");
        String[] edocEventCodeList = request.getParameterValues("edoc_eventCodeList");
        String[] edocFolderCodeList = request.getParameterValues("edoc_folderCodeList");

        // eforms
        String[] eformIds = request.getParameterValues("eform_id");
        String[] eformClassCodes = request.getParameterValues("eform_classCode");
        String[] eformTypeCodes = request.getParameterValues("eform_typeCode");
        String[] eformFormatCodes = request.getParameterValues("eform_formatCode");
        String[] eformContentTypeCodes = request.getParameterValues("eform_contentTypeCode");
        String[] eformEventCodeList = request.getParameterValues("eform_eventCodeList");
        String[] eformFolderCodeList = request.getParameterValues("eform_folderCodeList");

        // misc (other) documents
        String[] miscTypes = request.getParameterValues("misc_type");
        String[] miscClassCodes = request.getParameterValues("misc_classCode");
        String[] miscTypeCodes = request.getParameterValues("misc_typeCode");
        String[] miscFormatCodes = request.getParameterValues("misc_formatCode");
        String[] miscContentTypeCodes = request.getParameterValues("misc_contentTypeCode");
        String[] miscEventCodeList = request.getParameterValues("misc_eventCodeList");
        String[] miscFolderCodeList = request.getParameterValues("misc_folderCodeList");

        // get all Dao's
        SiteMappingDao siteMappingDao = SpringUtils.getBean(SiteMappingDao.class);
        EDocMappingDao eDocMappingDao = SpringUtils.getBean(EDocMappingDao.class);
        EFormMappingDao eFormMappingDao = SpringUtils.getBean(EFormMappingDao.class);
        MiscMappingDao miscMappingDao = SpringUtils.getBean(MiscMappingDao.class);

        // and affinity domain and a source are required parameters
        if (source != null && network != null) {

            // Site Mappings
            SiteMapping currentSiteMapping = siteMappingDao.findSiteMapping(network.getId());

            if (currentSiteMapping == null) {
                // add new mapping
                SiteMapping mapping = new SiteMapping();
                mapping.setAffinityDomain(networkId);
                mapping.setSource(source);

                if (siteFacilityType != null) {
                    mapping.setFacilityTypeCode(createCode(MappingCodeType.HealthcareFacilityTypeCode.toString(), siteFacilityType, source, network));
                }

                if (sitePracticeSetting != null) {
                    mapping.setPracticeSettingCode(createCode(MappingCodeType.PracticeSettingCode.toString(), sitePracticeSetting, source, network));
                }

                siteMappingDao.persist(mapping);

            } else {
                // replace current mapping
                currentSiteMapping.setAffinityDomain(networkId);
                currentSiteMapping.setSource(source);
                if (siteFacilityType != null && !siteFacilityType.equals(currentSiteMapping.getFacilityTypeCode().getDisplayName())) {
                    currentSiteMapping.setFacilityTypeCode(createCode(MappingCodeType.HealthcareFacilityTypeCode.toString(), siteFacilityType, source, network));
                }
                if (sitePracticeSetting != null & !sitePracticeSetting.equals(currentSiteMapping.getPracticeSettingCode().getDisplayName())) {
                    currentSiteMapping.setPracticeSettingCode(createCode(MappingCodeType.PracticeSettingCode.toString(), sitePracticeSetting, source, network));
                }
                siteMappingDao.merge(currentSiteMapping);
            }

            // Edocs
            for (int i = 0; i < edocTypes.length; i++) {
                // get any current edoc mapping for this edoc type (ie. lab, etc...) in this domain
                EDocMapping current = eDocMappingDao.findEDocMapping(network.getId(), edocTypes[i]);

                if (current == null) {
                    // add new
                    EDocMapping mapping = new EDocMapping();
                    mapping.setAffinityDomain(networkId);
                    mapping.setSource(source);
                    mapping.setDocType(edocTypes[i]);

                    if (edocClassCodes != null) {
                        mapping.setClassCode(createCode(MappingCodeType.ClassCode.toString(), edocClassCodes[i], source, network));
                    }

                    if (edocTypeCodes != null) {
                        mapping.setTypeCode(createCode(MappingCodeType.TypeCode.toString(), edocTypeCodes[i], source, network));
                    }

                    if (edocFormatCodes != null) {
                        mapping.setFormatCode(createCode(MappingCodeType.FormatCode.toString(), edocFormatCodes[i], source, network));
                    }

                    if (edocContentTypeCodes != null) {
                        mapping.setContentTypeCode(createCode(MappingCodeType.ContentTypeCode.toString(), edocContentTypeCodes[i], source, network));
                    }

                    if (edocEventCodeList != null) {
                        mapping.setEventCodeList(createCode(MappingCodeType.EventCodeList.toString(), edocEventCodeList[i], source, network));
                    }

                    if (edocFolderCodeList != null) {
                        mapping.setFolderCodeList(createCode(MappingCodeType.FolderCodeList.toString(), edocFolderCodeList[i], source, network));
                    }

                    eDocMappingDao.persist(mapping);

                } else {
                    // update existing
                    if (edocClassCodes != null && !edocClassCodes[i].equals(current.getClassCode().getDisplayName())) {
                        current.setClassCode(createCode(MappingCodeType.ClassCode.toString(), edocClassCodes[i], source, network));
                    }
                    if (edocTypeCodes != null && !edocTypeCodes[i].equals(current.getTypeCode().getDisplayName())) {
                        current.setTypeCode(createCode(MappingCodeType.TypeCode.toString(), edocTypeCodes[i], source, network));
                    }
                    if (edocFormatCodes != null && !edocFormatCodes[i].equals(current.getFormatCode().getDisplayName())) {
                        current.setFormatCode(createCode(MappingCodeType.FormatCode.toString(), edocFormatCodes[i], source, network));
                    }
                    if (edocContentTypeCodes != null && !edocContentTypeCodes[i].equals(current.getContentTypeCode().getDisplayName())) {
                        current.setContentTypeCode(createCode(MappingCodeType.ContentTypeCode.toString(), edocContentTypeCodes[i], source, network));
                    }
                    if (edocEventCodeList != null && !edocEventCodeList[i].equals(current.getEventCodeList().getDisplayName())) {
                        current.setEventCodeList(createCode(MappingCodeType.EventCodeList.toString(), edocEventCodeList[i], source, network));
                    }
                    if (edocFolderCodeList != null && !edocFolderCodeList[i].equals(current.getFolderCodeList().getDisplayName())) {
                        current.setFolderCodeList(createCode(MappingCodeType.FolderCodeList.toString(), edocFolderCodeList[i], source, network));
                    }
                    eDocMappingDao.merge(current);
                }
            }

            // Eforms
            for (int i = 0; i < eformIds.length; i++) {
                // get any current eform mapping for this eform in this domain
                EFormMapping current = eFormMappingDao.findEFormMapping(network.getId(), Integer.valueOf(eformIds[i]));

                if (current == null) {
                    // add new
                    EFormMapping mapping = new EFormMapping();
                    mapping.setAffinityDomain(networkId);
                    mapping.setSource(source);
                    mapping.setEformId(Integer.valueOf(eformIds[i]));

                    if (eformClassCodes != null) {
                        mapping.setClassCode(createCode(MappingCodeType.ClassCode.toString(), eformClassCodes[i], source, network));
                    }

                    if (eformTypeCodes != null) {
                        mapping.setTypeCode(createCode(MappingCodeType.TypeCode.toString(), eformTypeCodes[i], source, network));
                    }

                    if (eformFormatCodes != null) {
                        mapping.setFormatCode(createCode(MappingCodeType.FormatCode.toString(), eformFormatCodes[i], source, network));
                    }

                    if (eformContentTypeCodes != null) {
                        mapping.setContentTypeCode(createCode(MappingCodeType.ContentTypeCode.toString(), eformContentTypeCodes[i], source, network));
                    }

                    if (eformEventCodeList != null) {
                        mapping.setEventCodeList(createCode(MappingCodeType.EventCodeList.toString(), eformEventCodeList[i], source, network));
                    }

                    if (eformFolderCodeList != null) {
                        mapping.setFolderCodeList(createCode(MappingCodeType.FolderCodeList.toString(), eformFolderCodeList[i], source, network));
                    }

                    eFormMappingDao.persist(mapping);

                } else {
                    // update existing
                    if (eformClassCodes != null && !eformClassCodes[i].equals(current.getClassCode().getDisplayName())) {
                        current.setClassCode(createCode(MappingCodeType.ClassCode.toString(), eformClassCodes[i], source, network));
                    }
                    if (eformTypeCodes != null && !eformTypeCodes[i].equals(current.getTypeCode().getDisplayName())) {
                        current.setTypeCode(createCode(MappingCodeType.TypeCode.toString(), eformTypeCodes[i], source, network));
                    }
                    if (eformFormatCodes != null && !eformFormatCodes[i].equals(current.getFormatCode().getDisplayName())) {
                        current.setFormatCode(createCode(MappingCodeType.FormatCode.toString(), eformFormatCodes[i], source, network));
                    }
                    if (eformContentTypeCodes != null && !eformContentTypeCodes[i].equals(current.getContentTypeCode().getDisplayName())) {
                        current.setContentTypeCode(createCode(MappingCodeType.ContentTypeCode.toString(), eformContentTypeCodes[i], source, network));
                    }
                    if (eformEventCodeList != null && !eformEventCodeList[i].equals(current.getEventCodeList().getDisplayName())) {
                        current.setEventCodeList(createCode(MappingCodeType.EventCodeList.toString(), eformEventCodeList[i], source, network));
                    }
                    if (eformFolderCodeList != null && !eformFolderCodeList[i].equals(current.getFolderCodeList().getDisplayName())) {
                        current.setFolderCodeList(createCode(MappingCodeType.FolderCodeList.toString(), eformFolderCodeList[i], source, network));
                    }
                    eFormMappingDao.merge(current);
                }
            }

            // Misc documents
            for (int i = 0; i < miscTypes.length; i++) {
                // get any current misc (other document) mapping for this document in this domain
                MiscMapping current = miscMappingDao.findMiscMapping(network.getId(), miscTypes[i]);

                if (current == null) {
                    // add new
                    MiscMapping mapping = new MiscMapping();
                    mapping.setAffinityDomain(networkId);
                    mapping.setSource(source);
                    mapping.setType(miscTypes[i]);

                    if (miscClassCodes != null) {
                        mapping.setClassCode(createCode(MappingCodeType.ClassCode.toString(), miscClassCodes[i], source, network));
                    }

                    if (miscTypeCodes != null) {
                        mapping.setTypeCode(createCode(MappingCodeType.TypeCode.toString(), miscTypeCodes[i], source, network));
                    }

                    if (miscFormatCodes != null) {
                        mapping.setFormatCode(createCode(MappingCodeType.FormatCode.toString(), miscFormatCodes[i], source, network));
                    }

                    if (miscContentTypeCodes != null) {
                        mapping.setContentTypeCode(createCode(MappingCodeType.ContentTypeCode.toString(), miscContentTypeCodes[i], source, network));
                    }

                    if (miscEventCodeList != null) {
                        mapping.setEventCodeList(createCode(MappingCodeType.EventCodeList.toString(), miscEventCodeList[i], source, network));
                    }

                    if (miscFolderCodeList != null) {
                        mapping.setFolderCodeList(createCode(MappingCodeType.FolderCodeList.toString(), miscFolderCodeList[i], source, network));
                    }

                    miscMappingDao.persist(mapping);

                } else {
                    // update existing
                    if (miscClassCodes != null && !miscClassCodes[i].equals(current.getClassCode().getDisplayName())) {
                        current.setClassCode(createCode(MappingCodeType.ClassCode.toString(), miscClassCodes[i], source, network));
                    }
                    if (miscTypeCodes != null && !miscTypeCodes[i].equals(current.getTypeCode().getDisplayName())) {
                        current.setTypeCode(createCode(MappingCodeType.TypeCode.toString(), miscTypeCodes[i], source, network));
                    }
                    if (miscFormatCodes != null && !miscFormatCodes[i].equals(current.getFormatCode().getDisplayName())) {
                        current.setFormatCode(createCode(MappingCodeType.FormatCode.toString(), miscFormatCodes[i], source, network));
                    }
                    if (miscContentTypeCodes != null && !miscContentTypeCodes[i].equals(current.getContentTypeCode().getDisplayName())) {
                        current.setContentTypeCode(createCode(MappingCodeType.ContentTypeCode.toString(), miscContentTypeCodes[i], source, network));
                    }
                    if (miscEventCodeList != null && !miscEventCodeList[i].equals(current.getEventCodeList().getDisplayName())) {
                        current.setEventCodeList(createCode(MappingCodeType.EventCodeList.toString(), miscEventCodeList[i], source, network));
                    }
                    if (miscFolderCodeList != null && !miscFolderCodeList[i].equals(current.getFolderCodeList().getDisplayName())) {
                        current.setFolderCodeList(createCode(MappingCodeType.FolderCodeList.toString(), miscFolderCodeList[i], source, network));
                    }
                    miscMappingDao.merge(current);
                }
            }

        }

        
        return new ActionForward("/administration/index.jsp?show=sharingCenterMenu&load=scManageAffinityDomainLink");

    }

    private org.oscarehr.sharingcenter.model.MappingCode createCode(String attribute, String displayName, String source, AffinityDomainDataObject network) {
        org.oscarehr.sharingcenter.model.MappingCode retVal = null;

        if (source.equals("codes")) {
            // a code selected
            Set<CodeValueDataObject> codes = network.getCodeMapping(attribute).getCodeValues();
            CodeValueDataObject selectedCode = findCodeValueDataObject(displayName, codes);

            if (selectedCode != null) {
                // code found, create a MappingCode
                retVal = new org.oscarehr.sharingcenter.model.MappingCode();
                retVal.setAttribute(attribute);
                retVal.setCode(selectedCode.getCode());
                retVal.setCodeSystem(selectedCode.getCodeSystem());
                retVal.setCodeSystemName(selectedCode.getCodeSystemName());
                retVal.setDisplayName(selectedCode.getDisplayName());
            }

        } else if (source.equals("svs")) {
            // a value set was selected

        }

        return retVal;
    }

    private CodeValueDataObject findCodeValueDataObject(String displayName, Set<CodeValueDataObject> codes) {
        CodeValueDataObject retVal = null;
        for (CodeValueDataObject code : codes) {
            if (displayName.equals(code.getDisplayName())) {
                retVal = code;
                break;
            }
        }
        return retVal;
    }

}

/*
 *  Copyright (c) 2001-2002. Department of Family Medicine, McMaster University. All Rights Reserved.
 *  This software is published under the GPL GNU General Public License.
 *  This program is free software; you can redistribute it and/or
 *  modify it under the terms of the GNU General Public License
 *  as published by the Free Software Foundation; either version 2
 *  of the License, or (at your option) any later version.
 *  This program is distributed in the hope that it will be useful,
 *  but WITHOUT ANY WARRANTY; without even the implied warranty of
 *  MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 *  GNU General Public License for more details.
 * You should have received a copy of the GNU General Public License
 *  along with this program; if not, write to the Free Software
 *  Foundation, Inc., 59 Temple Place - Suite 330, Boston, MA 02111-1307, USA.
 *
 *
 *  This software was written for the
 *  Department of Family Medicine
 *  McMaster University
 *  Hamilton
 *  Ontario, Canada
 *
 *
 */
package org.oscarehr.clinicalconnect.ws;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlElementDecl;
import javax.xml.bind.annotation.XmlRegistry;
import javax.xml.namespace.QName;


/**
 * This object contains factory methods for each 
 * Java content interface and Java element interface 
 * generated in the org.oscarehr.clinicalconnect.ws package. 
 * <p>An ObjectFactory allows you to programatically 
 * construct new instances of the Java representation 
 * for XML content. The Java representation of XML 
 * content can consist of schema derived interfaces 
 * and classes representing the binding of schema 
 * type definitions, element declarations and model 
 * groups.  Factory methods for each of these are 
 * provided in this class.
 * 
 */
@XmlRegistry
public class ObjectFactory {

    private final static QName _DeleteFileResponse_QNAME = new QName("http://service.emr.medseek.com/", "deleteFileResponse");
    private final static QName _DownloadFileResponse_QNAME = new QName("http://service.emr.medseek.com/", "downloadFileResponse");
    private final static QName _ListFiles_QNAME = new QName("http://service.emr.medseek.com/", "listFiles");
    private final static QName _GenerateFileResponse_QNAME = new QName("http://service.emr.medseek.com/", "generateFileResponse");
    private final static QName _ListFilesResponse_QNAME = new QName("http://service.emr.medseek.com/", "listFilesResponse");
    private final static QName _DownloadFile_QNAME = new QName("http://service.emr.medseek.com/", "downloadFile");
    private final static QName _GenerateFile_QNAME = new QName("http://service.emr.medseek.com/", "generateFile");
    private final static QName _DeleteFile_QNAME = new QName("http://service.emr.medseek.com/", "deleteFile");

    /**
     * Create a new ObjectFactory that can be used to create new instances of schema derived classes for package: org.oscarehr.clinicalconnect.ws
     * 
     */
    public ObjectFactory() {
    }

    /**
     * Create an instance of {@link ListFilesResponse }
     * 
     */
    public ListFilesResponse createListFilesResponse() {
        return new ListFilesResponse();
    }

    /**
     * Create an instance of {@link DownloadFileResponse }
     * 
     */
    public DownloadFileResponse createDownloadFileResponse() {
        return new DownloadFileResponse();
    }

    /**
     * Create an instance of {@link DownloadFile }
     * 
     */
    public DownloadFile createDownloadFile() {
        return new DownloadFile();
    }

    /**
     * Create an instance of {@link GeneratedFileInfo }
     * 
     */
    public GeneratedFileInfo createGeneratedFileInfo() {
        return new GeneratedFileInfo();
    }

    /**
     * Create an instance of {@link GenerateFile }
     * 
     */
    public GenerateFile createGenerateFile() {
        return new GenerateFile();
    }

    /**
     * Create an instance of {@link ListFiles }
     * 
     */
    public ListFiles createListFiles() {
        return new ListFiles();
    }

    /**
     * Create an instance of {@link DeleteFileResponse }
     * 
     */
    public DeleteFileResponse createDeleteFileResponse() {
        return new DeleteFileResponse();
    }

    /**
     * Create an instance of {@link GenerateFileResponse }
     * 
     */
    public GenerateFileResponse createGenerateFileResponse() {
        return new GenerateFileResponse();
    }

    /**
     * Create an instance of {@link DeleteFile }
     * 
     */
    public DeleteFile createDeleteFile() {
        return new DeleteFile();
    }

    /**
     * Create an instance of {@link GeneratedFile }
     * 
     */
    public GeneratedFile createGeneratedFile() {
        return new GeneratedFile();
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "deleteFileResponse")
    public JAXBElement<DeleteFileResponse> createDeleteFileResponse(DeleteFileResponse value) {
        return new JAXBElement<DeleteFileResponse>(_DeleteFileResponse_QNAME, DeleteFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "downloadFileResponse")
    public JAXBElement<DownloadFileResponse> createDownloadFileResponse(DownloadFileResponse value) {
        return new JAXBElement<DownloadFileResponse>(_DownloadFileResponse_QNAME, DownloadFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListFiles }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "listFiles")
    public JAXBElement<ListFiles> createListFiles(ListFiles value) {
        return new JAXBElement<ListFiles>(_ListFiles_QNAME, ListFiles.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateFileResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "generateFileResponse")
    public JAXBElement<GenerateFileResponse> createGenerateFileResponse(GenerateFileResponse value) {
        return new JAXBElement<GenerateFileResponse>(_GenerateFileResponse_QNAME, GenerateFileResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link ListFilesResponse }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "listFilesResponse")
    public JAXBElement<ListFilesResponse> createListFilesResponse(ListFilesResponse value) {
        return new JAXBElement<ListFilesResponse>(_ListFilesResponse_QNAME, ListFilesResponse.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DownloadFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "downloadFile")
    public JAXBElement<DownloadFile> createDownloadFile(DownloadFile value) {
        return new JAXBElement<DownloadFile>(_DownloadFile_QNAME, DownloadFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link GenerateFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "generateFile")
    public JAXBElement<GenerateFile> createGenerateFile(GenerateFile value) {
        return new JAXBElement<GenerateFile>(_GenerateFile_QNAME, GenerateFile.class, null, value);
    }

    /**
     * Create an instance of {@link JAXBElement }{@code <}{@link DeleteFile }{@code >}}
     * 
     */
    @XmlElementDecl(namespace = "http://service.emr.medseek.com/", name = "deleteFile")
    public JAXBElement<DeleteFile> createDeleteFile(DeleteFile value) {
        return new JAXBElement<DeleteFile>(_DeleteFile_QNAME, DeleteFile.class, null, value);
    }

}

package org.oscarehr.common.dao;

import org.oscarehr.common.model.FileUploadCheck;
import org.springframework.stereotype.Repository;

@Repository
public class FileUploadCheckDao extends AbstractDao<FileUploadCheck>{

	public FileUploadCheckDao() {
		super(FileUploadCheck.class);
	}
}

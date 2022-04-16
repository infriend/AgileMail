package edu.agiledev.agilemail.service;

import java.io.File;
import java.io.InputStream;

/**
 * 本地文件管理服务类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/16
 */
public interface FileManageService {

    File getSavedFile(String filename);

    boolean saveAttachment(String filename, InputStream inputStream);

    boolean deleteAttachment(String filename);


}

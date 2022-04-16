package edu.agiledev.agilemail.service.impl;

import edu.agiledev.agilemail.exception.BaseException;
import edu.agiledev.agilemail.pojo.model.ReturnCode;
import edu.agiledev.agilemail.service.FileManageService;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;

/**
 * 本地文件管理服务类实现
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/16
 */
@Service
public class FileManageServiceImpl implements FileManageService {

    private final static String ATTACHMENT_DIR = "../resources/attachments/";

    @Override
    public File getSavedFile(String filename) {
        File file = newLocalFile(filename);

        if (file.exists()) {
            return file;
        } else {
            return null;
        }
    }

    @Override
    public boolean saveAttachment(String filename, InputStream inputStream) {
        File file = newLocalFile(filename);
        try {
            //FileUtils中已经包含创建文件及目录的代码
            FileUtils.copyInputStreamToFile(inputStream, file);

        } catch (IOException e) {
            throw new BaseException(ReturnCode.ERROR, "保存附件时出错");
        }

        return true;
    }

    @Override
    public boolean deleteAttachment(String filename) {

        File file = newLocalFile(filename);
        if (file.exists()) {
            return file.delete();
        }
        return true;
    }


    private File newLocalFile(String filename) {
        return new File(ATTACHMENT_DIR + filename);
    }


}

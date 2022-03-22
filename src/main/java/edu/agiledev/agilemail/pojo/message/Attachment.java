package edu.agiledev.agilemail.pojo.message;


import lombok.Data;
import lombok.NoArgsConstructor;

import java.io.Serializable;

/**
 * Message attachment
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/3/22
 */
@Data
public class Attachment implements Serializable {

    private static final long serialVersionUID = 4001902363078332347L;

    private String contentId;
    private String fileName;
    private String contentType;
    private Integer size;
    private byte[] content;

    public Attachment(String contentId, String fileName, String contentType, Integer size) {
        this.contentId = contentId;
        this.fileName = fileName;
        this.contentType = contentType;
        this.size = size;
    }


}

package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

/**
 * 包含是否是contentId的附件请求DTO
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class EmailAddressAttachDTO extends AddressBase {
    private Boolean contentId;
}

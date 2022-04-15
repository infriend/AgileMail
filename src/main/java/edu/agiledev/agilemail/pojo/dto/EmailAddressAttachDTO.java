package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class EmailAddressAttachDTO extends AddressBase {
    private Boolean isContentId;
}

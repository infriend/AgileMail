package edu.agiledev.agilemail.pojo.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.List;

/**
 * 保存传入的msgId的列表
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/15
 */
@EqualsAndHashCode(callSuper = true)
@Data
public class MsgIdsDTO extends AddressBase {
    private List<Long> msgIds;
}

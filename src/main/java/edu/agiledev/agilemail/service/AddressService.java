package edu.agiledev.agilemail.service;

/**
 * 通讯录服务接口
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/16
 */
public interface AddressService {

    boolean addressIsSaved(String userId, String mailId);

    boolean saveAddress(String userId, String mailId);
}

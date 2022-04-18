package edu.agiledev.agilemail.service.impl;

import edu.agiledev.agilemail.mappers.AddressbookMapper;
import edu.agiledev.agilemail.pojo.model.Addressbook;
import edu.agiledev.agilemail.service.AddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * 通讯录服务接口实现类
 *
 * @author Nosolution
 * @version 1.0
 * @since 2022/4/16
 */
@Service
public class AddressServiceImpl implements AddressService {

    private final AddressbookMapper addressbookMapper;

    @Autowired
    public AddressServiceImpl(AddressbookMapper addressbookMapper) {
        this.addressbookMapper = addressbookMapper;
    }

    @Override
    public boolean addressIsSaved(String userId, String mailId) {
        Addressbook addressbook = addressbookMapper.searchAddressByIdAndEmail(userId, mailId);
        return addressbook != null;
    }

    @Override
    public boolean saveAddress(String userId, String mailId) {
//        int res = addressbookMapper.insert(new Addressbook(userId, mailId));
//        return res > 0;
        //TODO: 需要适配
        return true;
    }
}

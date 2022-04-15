package edu.agiledev.agilemail.mappers;

import edu.agiledev.agilemail.pojo.model.Addressbook;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

@Mapper
public interface AddressbookMapper {
    int deleteByPrimaryKey(@Param("contactEmail") String contactEmail, @Param("id") String id);

    int insert(Addressbook record);

    int insertSelective(Addressbook record);

    Addressbook searchAddressByPrimarykey(Addressbook record);

    List<Addressbook> getContactByUserId(String userId);
}
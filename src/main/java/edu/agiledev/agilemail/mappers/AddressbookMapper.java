package edu.agiledev.agilemail.mappers;

import edu.agiledev.agilemail.pojo.model.Addressbook;
import org.apache.ibatis.annotations.Mapper;import org.apache.ibatis.annotations.Param;import java.util.List;

@Mapper
public interface AddressbookMapper {
    int deleteByPrimaryKey(String id);

    int insert(Addressbook record);

    int insertSelective(Addressbook record);

    Addressbook selectByPrimaryKey(String id);

    int updateByPrimaryKeySelective(Addressbook record);

    int updateByPrimaryKey(Addressbook record);

    int deleteByPrimaryKey(@Param("contactEmail") String contactEmail, @Param("id") String id);

    Addressbook searchAddressByIdAndEmail(@Param("userId") String userId,
                                          @Param("mailId") String mailId);

    List<Addressbook> getContactByUserId(String userId);
}
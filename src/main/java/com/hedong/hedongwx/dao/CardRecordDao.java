package com.hedong.hedongwx.dao;

import java.util.List;
import org.apache.ibatis.annotations.Param;
import com.hedong.hedongwx.entity.CardRecord;

public interface CardRecordDao {

	List<CardRecord> getRecordList();//卡操作查询
	
	List<CardRecord> cardRecordCardID(String cardID);
	
	int insertCardRecord(CardRecord crd);//插入卡操作表中记录

	List<CardRecord> selectInfo(CardRecord cardRecord);//查询用户提交的申请解卡(转移金额)记录
	
	CardRecord selectInfoOne(@Param("id")Integer id);
	
	CardRecord cardRecordByCardID(@Param("cardID")String cardID, @Param("status")Integer status, @Param("type")Integer type);//解挂时判断防止再次提交

	CardRecord selectByCardID(@Param("remark")String residue, @Param("status")Integer status, @Param("type")Integer type);

	int updatRecord(CardRecord cardRecord);

	List<CardRecord> submitApply(Integer uid);

	List<CardRecord> submitApply(@Param("uid")Integer uid, @Param("parameter")Integer parameter);
	
	
}

package com.hedong.hedongwx.dao;

import java.util.List;
import java.util.Map;

import org.apache.ibatis.annotations.Param;

import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Realchargerecord;

/**
 * @author  origin
 * 创建时间：   2019年4月16日 上午10:14:40  
 */
public interface RealchargerecordDao {
	

	/**根据实体类添加消费信息*/
	int insertRealRecord(Realchargerecord realrecord);

	/**根据条件查询用户消费信息 */
	List<Realchargerecord> realChargeRecordList( @Param("orderId")Integer orderId, @Param("code")String code,
			@Param("type")Integer type, @Param("startnum")Integer startnum, @Param("equnum")Integer equnum);

	/**
	 * @Description： 根据条件查询数据的函数情况  1：最大值   2：最小值     3：平均值
	 * @author： origin      
	 */
	Map<String, Object> functionRecord(Parameters pare);
	
	/**
	 * 根据订单id查询最大功率
	 * @param chargeid
	 * @return max power
	 */
	Integer selectMaxPowerByChargeid(Integer chargeid);
	
	/**
	 * 根据时间删除功率表
	 * @param createtime
	 * @return
	 */
	int delectRealChargeByTime(@Param("createtime")String createtime);

	
}

package com.hedong.hedongwx.service;

import com.hedong.hedongwx.entity.*;
import com.hedong.hedongwx.utils.PageUtils;
import org.springframework.ui.Model;

import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface ChargingTemplateService {

    /*修改计费模板信息*/
    Object updateChargingById(ChargingTemplate chargingTemplate);

    /*获取所有的计费模板信息*/
    List<ChargingTemplate> selectAllTemplate();

    /*新增计费模板*/
    Object insertCharging(ChargingTemplate chargingTemplate);

    /*删除计费模板信息*/
    Object delTemplateById(Integer id);

    /*获取所有的父类计费模板信息*/
    List<ChargingTemplate> selectAllTemplateParent();


}

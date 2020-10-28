package com.hedong.hedongwx.service.impl;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.dao.*;
import com.hedong.hedongwx.entity.*;
import com.hedong.hedongwx.service.*;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.*;
import com.hedong.hedongwx.utils.yinlian.SignUtils;
import net.sf.json.JSONObject;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

@Service
public class ChargingTemplateServiceImpl implements ChargingTemplateService {

    @Resource
    ChargingTemplateDao chargingTemplateDao;

    @Override
    public Object updateChargingById(ChargingTemplate chargingTemplate) {

        if (chargingTemplate.getParentId() != 0) {
            int time = chargingTemplate.getHour() * 60 + chargingTemplate.getMinute();
            List<ChargingTemplate> childTemplates = chargingTemplateDao.selectAllTemplate(chargingTemplate.getParentId());
            for (int i = 0; i < childTemplates.size(); i++) {
                if (chargingTemplate.getId() == childTemplates.get(i).getId() && i == 0) {//如果修改的是第一条判断是否大于第二条数据
                    int timeNext = childTemplates.get(i).getHour() * 60 + childTemplates.get(i).getMinute();
                    if (time > timeNext) {
                        return JSON.toJSON(CommUtil.responseBuild(400, "时间设置的不对", ""));
                    }
                } else if (chargingTemplate.getId() == childTemplates.get(i).getId() && i == childTemplates.size() - 1) {//如果修改的是最后一条判断是否大于倒数第二条
                    int timeOld = childTemplates.get(i).getHour() * 60 + childTemplates.get(i - 1).getMinute();
                    if (time < timeOld) {
                        return JSON.toJSON(CommUtil.responseBuild(400, "时间设置的不对", ""));
                    }
                } else {
                    int timeNext = childTemplates.get(i).getHour() * 60 + childTemplates.get(i + 1).getMinute();
                    int timeOld = childTemplates.get(i).getHour() * 60 + childTemplates.get(i - 1).getMinute();
                    if (timeOld > time || time > timeNext) {
                        return JSON.toJSON(CommUtil.responseBuild(400, "时间设置的不对", ""));
                    }
                }
            }
        }
        chargingTemplateDao.updateChargingById(chargingTemplate);
        return JSON.toJSON(CommUtil.responseBuild(200, "修改成功", ""));
    }

    @Override
    public List<ChargingTemplate> selectAllTemplate() {
        List<ChargingTemplate> parentTems = chargingTemplateDao.selectAllTemplate(0);
        for (ChargingTemplate chargingTemplate : parentTems) {
            List<ChargingTemplate> childTemplates = chargingTemplateDao.selectAllTemplate(chargingTemplate.getId());
            chargingTemplate.setChildTemplate(childTemplates);
        }
        return parentTems;
    }

    @Override
    public Object insertCharging(ChargingTemplate chargingTemplate) {
        if (chargingTemplate.getParentId() != 0) {
            List<ChargingTemplate> childTemplates = chargingTemplateDao.selectAllTemplate(chargingTemplate.getParentId());
            ChargingTemplate template = childTemplates.get(childTemplates.size() - 1);
            int time = chargingTemplate.getHour() * 60 + chargingTemplate.getMinute();
            int timeLast = template.getHour() * 60 + chargingTemplate.getMinute();
            if (time < timeLast) {
                return JSON.toJSON(CommUtil.responseBuild(400, "时间设置的不对", ""));
            }
        }
        chargingTemplateDao.insertCharging(chargingTemplate);
        return JSON.toJSON(CommUtil.responseBuild(200, "新增成功", ""));
    }

    @Override
    public Object delTemplateById(Integer id) {
        ChargingTemplate chargingTemplate = chargingTemplateDao.selectAllTemplateById(id);
        if (chargingTemplate.getParentId() != 0) {
            chargingTemplateDao.delTemplateById(id,null);
        }else {
            chargingTemplateDao.delTemplateById(id,id);
        }
        return JSON.toJSON(CommUtil.responseBuild(200, "删除成功", ""));
    }

    @Override
    public List<ChargingTemplate> selectAllTemplateParent() {
        return chargingTemplateDao.selectAllTemplate(0);
    }


}

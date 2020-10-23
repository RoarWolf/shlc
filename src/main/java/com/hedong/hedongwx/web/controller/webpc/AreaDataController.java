package com.hedong.hedongwx.web.controller.webpc;

import java.math.BigDecimal;
import java.util.*;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.service.AreaService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.utils.CommUtil;

/**
 * @author origin  创建时间：   2019年11月7日 上午11:29:22
 * @Description： 小区信息控制类
 */
@Controller
@RequestMapping(value = "/areaData")
public class AreaDataController {

    @Autowired
    private AreaService areaService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private BasicsService basicsService;
    @Autowired
    private HttpServletRequest request;

    /**
     * separate
     *
     * @Description：小区管理信息
     * @author： origin
     */
    @RequestMapping(value = "/areaManageData")
    @ResponseBody
    public Object areaManageData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
		/*if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{*/
        result = areaService.areaManageData(request);
        //}
        return JSON.toJSON(result);
    }

    /**
     * @param request
     * @param response
     * @Description：
     * @author： origin 2020年5月15日上午11:11:53
     */
    @RequestMapping(value = "/areaDetails")
    @ResponseBody
    public Object areaDetails(HttpServletRequest request, HttpServletResponse response) {
        if (CommonConfig.isExistSessionUser(request)) {
            return CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            Map<String, Object> datamap = new HashMap<String, Object>();
            try {
                Map<String, Object> maparam = CommUtil.getRequestParam(request);
                if (maparam.isEmpty()) {
                    return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                }
                Integer aid = CommUtil.toInteger(maparam.get("aid"));
                if (aid.equals(0)) {
                    return CommUtil.responseBuildInfo(102, "请指定某个小区", datamap);
                }
                //根据小区id查询单个小区相关信息
                Map<String, Object> resultarea = areaService.inquireSingleAreaInfo(aid);
                Integer merid = CommUtil.toInteger(resultarea.get("merid"));
                Integer tempcardid = CommUtil.toInteger(resultarea.get("tempid2"));
                Integer tempwallid = CommUtil.toInteger(resultarea.get("tempid"));
                Map<String, Object> DirectTempCard = basicsService.inquireDirectTempData(tempcardid, merid, null, "在线卡");
                Integer nowcartempid = CommUtil.toInteger(DirectTempCard.get("id"));
                List<TemplateSon> tempcardson = templateService.getSonTemplateLists(nowcartempid);
                tempcardson = tempcardson == null ? new ArrayList<>() : tempcardson;
                DirectTempCard.put("gather", tempcardson);
                Map<String, Object> DirectTempWall = basicsService.inquireDirectTempData(tempwallid, merid, null, "钱包");
                Integer nowwaltempid = CommUtil.toInteger(DirectTempWall.get("id"));
                List<TemplateSon> tempwallson = templateService.getSonTemplateLists(nowwaltempid);
                tempwallson = tempwallson == null ? new ArrayList<>() : tempwallson;
                DirectTempWall.put("gather", tempwallson);

                Map<String, Object> areaonline = areaService.inquireAreaOnlineCard(aid);
                Integer devicenum = areaService.inquireAreaDevicenum(aid);
                Map<String, Object> areauser = areaService.inquireAreaUser(aid);

                Integer onlinenum = CommUtil.toInteger(areaonline.get("count"));
                Double ctopupbalance = CommUtil.toDouble(areaonline.get("topupbalance"));
                Double csendmoney = CommUtil.toDouble(areaonline.get("sendmoney"));

                Integer areausernum = CommUtil.toInteger(areauser.get("count"));
                Double utopupbalance = CommUtil.toDouble(areauser.get("topupbalance"));
                Double usendmoney = CommUtil.toDouble(areauser.get("sendmoney"));
                datamap.put("ctopupbalance", ctopupbalance);
                datamap.put("csendmoney", csendmoney);
                datamap.put("utopupbalance", utopupbalance);
                datamap.put("usendmoney", usendmoney);
                resultarea.put("onlinenum", onlinenum);
                resultarea.put("devicenum", devicenum);
                resultarea.put("areausernum", areausernum);

                List<Map<String, Object>> devicenumlist = equipmentService.inquireAreaDeaviceInfo(aid);

                List<Map<String, Object>> touarealist = areaService.inquirePartnerInfo(aid, 2);
                datamap.put("resultarea", resultarea);
                datamap.put("tempWallet", DirectTempWall);
                datamap.put("tempOnCard", DirectTempCard);
                datamap.put("partnerInfo", touarealist);
                datamap.put("devicenumlist", devicenumlist);
                return JSON.toJSON(CommUtil.responseBuildInfo(200, "成功", datamap));
            } catch (Exception e) {
                e.printStackTrace();
                return CommUtil.responseBuildInfo(301, "异常错误", datamap);
            }
        }
    }


    @RequestMapping(value = "/editAreaInfo")
    @ResponseBody
    public Object editAreaInfo(Integer aid, String name, String address) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        if (CommUtil.toInteger(aid).equals(0)) {
            return CommUtil.responseBuild(102, "参数传递不正确或为空", "");
        }
        try {
            Object resultedit = areaService.editAreaInfo(aid, name, address);
//			Map<String, Object> resultedit =  (Map<String, Object>) areaService.editAreaInfo(aid, name, address);
            return JSON.toJSON(resultedit);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }


    /**
     * @Description：修改合伙人分成比
     * @author： origin 2020年5月15日上午11:11:53
     */
    @RequestMapping(value = "/editBindAreaPartner")
    @ResponseBody
//    public Object editBindAreaPartner(HttpServletRequest request, HttpServletResponse response) {
    public Object editBindAreaPartner(Integer id, Integer aid, Double percent) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        if (CommUtil.toInteger(id).equals(0) || percent == null) {
            return CommUtil.responseBuild(102, "参数传递不正确或为空", "");
        }
        try {
            //修改小区合伙人分成比
            Map<String, Object> resultedit = areaService.editBindAreaPartner(id, percent * 100);
            Integer coden = CommUtil.toInteger(resultedit.get("code"));
            if (coden == 200) {
//				AreaRelevance partner = (AreaRelevance) resultedit.get("result");
                List<Map<String, Object>> partnerInfo = areaService.inquirePartnerInfo(aid, 2);
                datamap.put("partnerInfo", partnerInfo);
                return JSON.toJSON(CommUtil.responseBuildInfo(200, "成功", datamap));
            } else {
                return JSON.toJSON(CommUtil.responseBuildInfo(201, "修改失败", datamap));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }


    /**
     * @Description：删除合伙人
     * @author： origin 2020年5月15日上午11:11:53
     */
    @RequestMapping(value = "/removeAreaPartner")
    @ResponseBody
    public Object removeAreaPartner(Integer id, Integer aid) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        if (CommUtil.toInteger(id).equals(0)) {
            return CommUtil.responseBuild(102, "参数传递不正确或为空", "");
        }
        try {
            Map<String, Object> resultedit = areaService.removeAreaPartner(id);
            Integer coden = CommUtil.toInteger(resultedit.get("code"));
            if (coden == 200) {
//				AreaRelevance partner = (AreaRelevance) resultedit.get("result");
                List<Map<String, Object>> partnerInfo = areaService.inquirePartnerInfo(aid, 2);
                datamap.put("partnerInfo", partnerInfo);
                return JSON.toJSON(CommUtil.responseBuildInfo(200, "成功", datamap));
            } else {
                return JSON.toJSON(resultedit);
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * @Description：绑定合伙人
     * @author： origin 2020年5月15日上午11:11:53
     */
    @RequestMapping(value = "/bindAreaPartner")
    @ResponseBody
    public Object bindAreaPartner(Integer aid, Integer type, String phone, Double percent) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        if (CommUtil.toInteger(aid).equals(0) || percent == null) {
            return CommUtil.responseBuild(102, "参数传递不正确或为空", "");
        }
        try {
            Map<String, Object> resultedit = areaService.bindAreaPartner(aid, type, phone, percent * 100);
            Integer coden = CommUtil.toInteger(resultedit.get("code"));
            if (coden == 200) {
                List<Map<String, Object>> partnerInfo = areaService.inquirePartnerInfo(aid, 2);
                datamap.put("partnerInfo", partnerInfo);
                return JSON.toJSON(CommUtil.responseBuildInfo(200, "成功", datamap));
            } else {
                return JSON.toJSON(CommUtil.responseBuildInfo(201, "修改失败", datamap));
            }
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    @RequestMapping(value = "/delAreaCode")
    @ResponseBody
    public Object delAreaCode(Integer aid, Integer merid, String code) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        if (code == null) {
            return CommUtil.responseBuild(102, "参数传递不正确或为空", "");
        }
        try {
            User user = (User) request.getSession().getAttribute("admin");
            Integer opeid = user == null ? 0 : user.getId();
            equipmentService.insertCodeoperatelog(code, 4, 2, merid, opeid, aid + "");
            Equipment equipment = new Equipment();
            equipment.setCode(code);
            equipment.setAid(0);
            equipmentService.updateEquipment(equipment);
            List<Map<String, Object>> devicenumlist = equipmentService.inquireAreaDeaviceInfo(aid);
            datamap.put("devicenum", devicenumlist.size());
            datamap.put("devicenumlist", devicenumlist);
            return JSON.toJSON(CommUtil.responseBuildInfo(200, "成功", datamap));
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    @RequestMapping("/addAreaCode")
    @ResponseBody
    public Map<String, Object> addAreaCode(String code, Integer aid) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        if (code == null || aid == null) {
            return CommUtil.responseBuild(102, "参数传递不正确或为空", datamap);
        }
        Area area = areaService.selectByIdArea(CommUtil.toInteger(aid));
        if (area == null) {
            return CommUtil.responseBuild(102, "小区不存在", datamap);
        }
        Integer merid = CommUtil.toInteger(area.getMerid());
        Map<String, Object> deviceinfo = CommUtil.isMapEmpty(equipmentService.selectEquipAreaInfo(code));
        Integer dealid = CommUtil.toInteger(deviceinfo.get("dealid"));
        Integer aidcode = CommUtil.toInteger(deviceinfo.get("aid"));
        if (!merid.equals(dealid)) {
            return CommUtil.responseBuild(102, "小区所属商户与设备所属商户不一致", datamap);
        }
        if (!aidcode.equals(0)) {
            return CommUtil.responseBuild(102, "该设备已被绑定到小区中", datamap);
        }
        User user = (User) request.getSession().getAttribute("user");
        Integer opeid = user == null ? 0 : user.getId();
        equipmentService.insertCodeoperatelog(code, 4, 1, dealid, opeid, aid + "");
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setAid(aid);
        equipmentService.updateEquipment(equipment);
        deviceinfo.put("aid", aid);
        List<Map<String, Object>> devicenumlist = equipmentService.inquireAreaDeaviceInfo(aid);
        datamap.put("deviceinfo", deviceinfo);
        datamap.put("devicenum", devicenumlist.size());
        datamap.put("devicenumlist", devicenumlist);
        return CommUtil.responseBuild(200, "成功", datamap);
    }

    //type: 1 钱包   2在线卡
    @SuppressWarnings("unchecked")
    @RequestMapping({"/dealAllTemp"})
    @ResponseBody
    public Object copyDirectTemp(Integer aid, Integer merid, Integer type) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Area area = areaService.selectByIdArea(CommUtil.toInteger(aid));
            if (area == null || type == null) {
                return CommUtil.responseBuild(102, "小区不存在或未指定对象", datamap);
            }
            List<Map<String, Object>> listmap = new ArrayList<Map<String, Object>>();
            List<Map<String, Object>> listinfo = new ArrayList<Map<String, Object>>();
            //3 钱包   4在线卡
            Integer tempid = 0;
            Integer tempstatus = 3;
            if (type.equals(1)) {
                tempstatus = 3;
                tempid = CommUtil.toInteger(area.getTempid());
            } else if (type.equals(2)) {
                tempstatus = 4;
                tempid = CommUtil.toInteger(area.getTempid2());
            }
            List<TemplateParent> tempinfo = templateService.inquireTempByStatus(CommUtil.toInteger(merid), tempstatus);
            Map<String, Object> tempchoose = new HashMap<String, Object>();
            for (TemplateParent item : tempinfo) {
                Integer nowtempid = CommUtil.toInteger(item.getId());
                Map<String, Object> mapinfo = JSON.parseObject(JSON.toJSONString(item), Map.class);
                List<TemplateSon> tempcardson = templateService.getSonTemplateLists(nowtempid);
                tempcardson = tempcardson == null ? new ArrayList<>() : tempcardson;
                mapinfo.put("gather", tempcardson);
                if (CommUtil.toInteger(tempid).equals(nowtempid)) {
                    mapinfo.put("pitchon", 1);
                    tempchoose = mapinfo;
                } else {
                    listmap.add(mapinfo);
                }
            }
            listinfo.add(tempchoose);
            listinfo.addAll(listmap);
            datamap.put("listdata", listinfo);
            datamap.put("tempchoose", tempchoose);
            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }


    //type: 1 钱包   2在线卡
    @RequestMapping({"/areaTempChoose"})
    @ResponseBody
    public Object areaTempChoose(Integer aid, Integer tempid, Integer type) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            //3 钱包   4在线卡
            aid = CommUtil.toInteger(aid);
            tempid = CommUtil.toInteger(tempid);
            Map<String, Object> result = areaService.areaTempDirectUpdate(aid, tempid, type);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * 通过小区id 删除小区信息、并同步删除小区名下合伙人信息
     *
     * @param request, response, model
     * @return
     */
    @RequestMapping(value = {"/deleteByArea"})
    @ResponseBody
    public Object deleteByArea(Integer id) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            areaService.deleteAreaByAid(id);
            map.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 403);
        }
        return map;
    }

    /**
     * 通过实体类 Parameter 修改站点信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = {"/insertByParame"})
    @ResponseBody
    public Object insertByParame(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Area area = new Area();
            area.setName(request.getParameter("name"));
            area.setAddress(request.getParameter("address"));
            area.setLat(new BigDecimal(request.getParameter("lat")));
            area.setLon(new BigDecimal(request.getParameter("lon")));
            area.setCreateTime(new Date());
            area.setAreaImg(request.getParameter("areaImg"));
            areaService.insertArea(area);
            map.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 403);
        }
        return map;
    }

    /**
     * 通过实体类 Parameter 修改站点信息
     *
     * @param request
     * @return
     */
    @RequestMapping(value = {"/updateByParame"})
    @ResponseBody
    public Object updateByParame(HttpServletRequest request) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            Area area = new Area();
            area.setId(StringUtil.getIntString(request.getParameter("id")));
            area.setName(request.getParameter("name"));
            area.setAddress(request.getParameter("address"));
            area.setLat(new BigDecimal(request.getParameter("lat")));
            area.setLon(new BigDecimal(request.getParameter("lon")));
            area.setAreaOnlyCode(request.getParameter("area_only_code"));
            area.setAreaImg(request.getParameter("areaImg"));
            area.setUpdateTime(new Date());
            areaService.updateByArea(area);
            map.put("code", 200);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("code", 403);
        }
        return map;
    }

    /**
     * 查询站点列表下拉框
     *
     * @return
     */
    @RequestMapping(value = {"/selectAllArea"})
    @ResponseBody
    public Object selectAllArea() {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            List<Map<String, Object>> areaList = areaService.selectAllArea();
            map.put("areaList", areaList);
            map = CommUtil.responseBuildInfo(200, "成功", map);
        } catch (Exception e) {
            e.printStackTrace();
            map = CommUtil.responseBuildInfo(301, "异常错误", map);
        }
        return JSON.toJSON(map);
    }
}

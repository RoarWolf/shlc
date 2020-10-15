package com.hedong.hedongwx.web.controller.webpc;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.hedong.hedongwx.entity.*;
import com.hedong.hedongwx.utils.JedisUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import com.alibaba.fastjson.JSON;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.service.AllPortRecordService;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.FeescaleService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.service.UserService;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.DisposeUtil;

/**
 * @author origin  创建时间：   2019年11月7日 上午11:27:48
 * @Description： 设备信息控制类
 */
@Controller
@RequestMapping(value = "/deviceData")
public class DeviceDataController {

    @Autowired
    private EquipmentService equipmentService;
    @Autowired
    private AllPortRecordService allPortRecordService;
    @Autowired
    private UserEquipmentService userEquipmentService;
    @Autowired
    private UserService userService;
    @Autowired
    private AllPortStatusService allPortStatusService;
    @Autowired
    private TemplateService templateService;
    @Autowired
    private FeescaleService feescaleService;
    @Autowired
    private BasicsService basicsService;

    /**
     * separate
     *
     * @Description：设备转移（从A名下转移到B名下）
     * @author： origin
     * @createTime：2020年4月3日下午4:09:00
     */
    @RequestMapping(value = "/deviceDataTransfer")
    @ResponseBody
    public Object deviceDataTransfer(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.deviceDataTransfer(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description：搜索商户名下设备信息（并按小区排序）
     * @author： origin
     * @createTime：2019年12月31日上午10:28:42
     */
    @RequestMapping(value = "/searchDealerDeviceData")
    @ResponseBody
    public Object searchDealerDeviceData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.searchDealerDeviceData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description：搜索商户名下同类型设备信息（并按小区排序）
     * @author： origin
     * @createTime：2019年12月31日上午10:28:42
     */
    @RequestMapping(value = "/searchDeviceData")
    @ResponseBody
    public Object searchDeviceData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.searchDeviceData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 查询商户名下同类型设备信息
     * @author： origin   2019年11月21日 下午3:37:04
     */
    @RequestMapping(value = "/inquireDeviceData")
    @ResponseBody
    public Object inquireDeviceData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.inquireDeviceData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 更改设备模板信息
     * @author： origin   2019年11月21日 下午3:45:12
     */
    @RequestMapping(value = "/updateDeviceTemplate")
    @ResponseBody
    public Object updateDeviceTemplate(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.updateDeviceTemplate(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 查询设备模板信息
     * @author： origin
     */
    @RequestMapping(value = "/inquireDeviceTemplateData")
    @ResponseBody
    public Object inquireDeviceTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.inquireDeviceTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 修改主模板信息
     * @author： origin
     */
    @RequestMapping(value = "/updateFirstTemplateData")
    @ResponseBody
    public Object updateFirstTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.updateFirstTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * ok
     * separate
     *
     * @Description： 删除所有模板信息（主模板、子模板）（如果使用，不能删除）
     * @author： origin
     */
    @RequestMapping(value = "/deleteTemplateData")
    @ResponseBody
    public Object deleteTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.deleteTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 选中模板信息
     * @author： origin
     */
    @RequestMapping(value = "/pitchOnTemplate")
    @ResponseBody
    public Object pitchOnTemplate(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.pitchOnTemplate(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 选中默认模板信息
     * @author： origin
     */
    @RequestMapping(value = "/defaultPitchOnTemplate")
    @ResponseBody
    public Object defaultPitchOnTemplate(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.defaultPitchOnTemplate(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 修改编辑子模板信息
     * @author： origin
     */
    @RequestMapping(value = "/updateSecondTemplateData")
    @ResponseBody
    public Object updateSecondTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.updateSecondTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 删除子模板信息
     * @author： origin
     */
    @RequestMapping(value = "/deleteSecondTemplateData")
    @ResponseBody
    public Object deleteSecondTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.deleteSecondTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 添加主模板
     * @author： origin
     */
    @RequestMapping(value = "/insertFirstTemplateData")
    @ResponseBody
    public Object insertFirstTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.insertFirstTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 添加子模板
     * @author： origin
     */
    @RequestMapping(value = "/insertSecondTemplateData")
    @ResponseBody
    public Object insertSecondTemplateData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = templateService.insertSecondTemplateData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * @Description：电脑端添加或修改v3设备模板（主模板含子模板）
     * @author： origin
     * @createTime：2020年4月11日下午2:50:36
     */
    @RequestMapping(value = "/insertOrModifyTemp")
    @ResponseBody
    public Object insertAmendTemp(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
            return result;
        } else {
            result = templateService.insertOrModifyTemp(request);
            return result;
        }
    }
    //===============================================================================================

    /**
     * separate
     *
     * @Description： 查询获取设备信息
     * @author： origin
     */
    @RequestMapping(value = "/getDeviceData")
    @ResponseBody
    public Object getDeviceData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
		/*if(JedisUtils.get("admin")==null){
			result = CommUtil.responseBuild(901, "session缓存失效", "");
		}else{*/
        result = equipmentService.getDeviceData(request, 1);
        //}
        return JSON.toJSON(result);
    }

    /**
     * 按条件分页导出数据
     *
     * @param request 参数
     * @return {@link Object}
     */
    @PostMapping("/exportDeviceData")
    @ResponseBody
    public Object exportDeviceData(HttpServletRequest request) {
        if (CommonConfig.isExistSessionUser(request)) {
            return CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            return equipmentService.exportDeviceData(request);
        }
    }


    /**
     * separate
     *
     * @Description： 查询获取蓝牙设备信息
     * @author： origin
     */
    @RequestMapping(value = "/getBluetoothDeviceData")
    @ResponseBody
    public Object getBluetoothDeviceData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.getDeviceData(request, 2);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 查询设备日志信息
     * @author： origin
     */
    @RequestMapping(value = "/inquireDeviceLogData")
    @ResponseBody
    public Object inquireDeviceLogData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = allPortRecordService.inquireDeviceLogData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 重置设备测试次数
     * @author： origin
     */
    @RequestMapping(value = "/resetDeviceTestTime")
    @ResponseBody
    public Object resetDeviceTestTime(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.resetDeviceTestTime(request);
        }
        return result;
    }

    /**
     * separate
     *
     * @Description： 设置设备版本号信息
     * @author： origin
     */
    @RequestMapping(value = "/editDeviceHardversion")
    @ResponseBody
    public Object editDeviceHardversion(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.editDeviceHardversion(request);
        }
        return result;
    }

    /**
     * separate
     *
     * @Description： 查询设备操作日志
     * @author： origin
     */
    @RequestMapping(value = "/inquireDeviceOperationData")
    @ResponseBody
    public Object inquireDeviceOperationData(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.inquireDeviceOperationData(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 设备详细信息查询
     * @author： origin
     */
    @RequestMapping(value = "/inquireDeviceDetail")
    @ResponseBody
    public Object inquireDeviceDetail(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            Map<String, Object> datamap = new HashMap<String, Object>();
            try {
                Map<String, Object> maparam = CommUtil.getRequestParam(request);
                if (maparam.isEmpty()) {
                    return CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                }
                String code = CommUtil.toString(maparam.get("code"));
                Equipment equipment = equipmentService.getEquipmentById(code);
                datamap.put("code", code);
                datamap.put("bindtype", CommUtil.toInteger(equipment.getBindtype()));
                String version = CommUtil.toString(equipment.getHardversion());
//				int neednum = 10;
//				if ("05".equals(version)) {
//					neednum = 16;
//				} else if ("06".equals(version)) {
//					neednum = 20;
//				} else if ("02".equals(version)) {
//					neednum = 2;
//				}
//				List<AllPortStatus> allPortStatusList = allPortStatusService.findPortStatusListByEquipmentnum(code, neednum);
                List<Map<String, String>> allPortStatusList = DisposeUtil.addPortStatus(code, version);
                datamap.put("allPortStatusList", allPortStatusList);
                datamap.put("equipment", equipment);
                UserEquipment userEquipment = userEquipmentService.getUserEquipmentByCode(code);
                Integer merid = 0;
                if (userEquipment != null) merid = CommUtil.toInteger(userEquipment.getUserId());
                datamap.put("merid", merid);
                Integer tempid = CommUtil.toInteger(equipment.getTempid());
                User dealeruser = new User();
                if (!merid.equals(0)) dealeruser = userService.selectUserById(merid);
                if (dealeruser == null) dealeruser = new User();
                String dealerphne = CommUtil.toString(dealeruser.getPhoneNum());
                String servephone = CommUtil.toString(dealeruser.getServephone());

                Map<String, Object> DirectTemp = basicsService.inquireDirectTempData(tempid, merid, code, version);
                String temphone = CommUtil.toString(DirectTemp.get("common1"));
                servephone = basicsService.getServephoneData(temphone, null, servephone, dealerphne);
                datamap.put("phonenum", servephone);
                Integer nowtempid = CommUtil.toInteger(DirectTemp.get("id"));
                Integer temporaryc = CommUtil.toInteger(DirectTemp.get("permit"));
                Integer grade = CommUtil.toInteger(DirectTemp.get("grade"));

                List<TemplateSon> tempson = templateService.getSonTemplateLists(nowtempid);
                tempson = tempson == null ? new ArrayList<>() : tempson;

                if (version.equals("08") || version.equals("09") || version.equals("10")) {
                    List<TemplateSon> tempower = new ArrayList<>();
                    List<TemplateSon> temtime = new ArrayList<>();
                    List<TemplateSon> temmoney = new ArrayList<>();
                    for (TemplateSon item : tempson) {
                        Integer type = CommUtil.toInteger(item.getType());
                        if (type.equals(1)) {
                            tempower.add(item);
                        } else if (type.equals(2)) {
                            temtime.add(item);
                        } else if (type.equals(3)) {
                            temmoney.add(item);
                        }
                    }
//					Map<String, Object> testMap = JSON.parseObject(JSON.toJSONString(tempdata), Map.class);
                    DirectTemp.put("gather1", tempower);
                    DirectTemp.put("gather2", temtime);
                    DirectTemp.put("gather3", temmoney);
                    DirectTemp.put("temporaryc", temporaryc);
                    datamap.put("templatev3", DirectTemp);
                } else {
                    if (grade == 1) {
                        List<TemplateParent> template = templateService.getTempDetails(templateService.getParentTemplateOne(nowtempid));
                        datamap.put("templist", template);
                        datamap.put("temp", null);
                    } else {
                        DirectTemp.put("gather", tempson);
                        datamap.put("temp", DirectTemp);
                        datamap.put("templist", null);
                    }
//					TemplateParent tempdata = templateService.inquireDirectTemp(tempid);
//					if(tempdata.getId()==null){
//						Integer status = 0;
//						if(version.equals("03")){
//							status = 2;
//						}else if(version.equals("04")){
//							status = 1;
//						}
//						tempdata = templateService.inquireTempData( merid, status);
//					}
//					if(grade==1){
//						List<TemplateParent> template = templateService.getTempDetails(tempdata);
//						datamap.put("templist", template);
//						datamap.put("temp", null);
//					}else{
//						tempdata.setGather(templateService.getSonTemplateLists(tempdata.getId()));
//						datamap.put("temp", tempdata);
//						datamap.put("templist", null);
//					}
                }
                if (userEquipment == null) {
                    datamap.put("username", "0");
                    datamap.put("message", "设备未绑定");
                    return datamap;
                }
                CodeSystemParam codeSystemParam = equipmentService.selectCodeSystemParamByCode(code);
                datamap.put("sysparam", codeSystemParam);
                User user = userService.selectUserById(userEquipment.getUserId());
                if (user != null) {
                    datamap.put("username", user.getUsername());
                    datamap.put("uid", user.getId());
                } else {
                    datamap.put("username", "0");
                    datamap.put("uid", 0);
                }
                result = CommUtil.responseBuildInfo(200, "成功", datamap);
            } catch (Exception e) {
                e.printStackTrace();
                result = CommUtil.responseBuildInfo(301, "异常错误", datamap);
            }
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 绑定设备
     * @author： origin
     */
    @RequestMapping(value = "/bindingDevice")
    @ResponseBody
    public Object bindingDevice(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.bindingDevice(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * separate
     *
     * @Description： 解绑设备
     * @author： origin
     */
    @RequestMapping(value = "/disbindingDevice")
    @ResponseBody
    public Object disbindingDevice(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.disbindingDevice(request);
        }
        return JSON.toJSON(result);
    }

    /**
     * 超级管理员设置设备的到期时间
     *
     * @param code   设备号
     * @param expire 到期时间
     * @return boolean
     */
    @RequestMapping("/setExpire")
    @ResponseBody
    public Object setExpire(String code, String expire, HttpServletRequest request) {
        Object result = null;
        User user = CommonConfig.getAdminReq(request);
        Integer rank = user.getLevel();
        //判断用户的级别
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
            return JSON.toJSON(result);
        } else if (rank != null && rank == 0) {
            if (feescaleService.setEquipmentExpire(code, expire)) {
                result = CommUtil.responseBuild(200, "修改成功", expire);
                return JSON.toJSON(result);
            } else {
                result = CommUtil.responseBuild(400, "参数错误", "");
                return JSON.toJSON(result);
            }
        } else {
            result = CommUtil.responseBuild(103, "商家无权修改", "");
            return JSON.toJSON(result);
        }
    }

    /**
     * employ 选择	ORIGIN
     *
     * @Description： 设置(修改)设备名字（备注）
     * @author： origin
     */
    @RequestMapping(value = "/editDeviceName")
    @ResponseBody
    public Object editDeviceName(HttpServletRequest request, HttpServletResponse response) {
        Object result = null;
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else {
            result = equipmentService.editDeviceName(request);
        }
        return result;
    }

    /**
     * 管理员互换两个设备的IEME号
     *
     * @param request
     * @return JSON
     */
    @RequestMapping("/transpositionImei")
    @ResponseBody
    public Object transpositionImei(HttpServletRequest request) {
        Object result = null;
        User user = CommonConfig.getAdminReq(request);
        if (JedisUtils.get("admin") == null) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else if (user != null) {
            result = equipmentService.transpositionImei(request);
        } else {
            result = CommUtil.responseBuild(103, "无权修改", "");
            return JSON.toJSON(result);
        }
        return result;
    }

    /**
     * 超级管理员设置设备号
     *
     * @param request
     * @return JSON
     */
    @RequestMapping("/customizationCode")
    @ResponseBody
    public Object customizationCode(HttpServletRequest request) {
        Object result = null;
        User user = CommonConfig.getAdminReq(request);
        if (CommonConfig.isExistSessionUser(request)) {
            result = CommUtil.responseBuild(901, "session缓存失效", "");
        } else if (user != null && user.getLevel() == 0) {
            result = equipmentService.customizationCode(request);
        } else {
            result = CommUtil.responseBuild(103, "无权修改", "");
            return JSON.toJSON(result);
        }
        return result;
    }

    /**
     * 添加桩
     *
     * @param code           充电桩编号
     * @param hardversion    硬件版本
     * @param softversion    软件版本
     * @param subHardversion 次单元硬件版本
     * @param subSoftversion 次单元软件版本
     * @param dcModeltype    直流模块类型
     * @param dcModelnum     直流模块总数
     * @param dcModelpower   直流模块单模块功
     * @param location       地址详情
     * @param lon            经度
     * @param lat            纬度
     * @param remark         充电桩昵称
     * @return
     */
    @RequestMapping(value = "/insetDevice", method = RequestMethod.GET)
    @ResponseBody
    public Object insetDevice( EquipmentNew equ) {
        return equipmentService.insertEquipmentNew(equ);
    }

    /**
     * 修改充电桩信息
     *
     * @param code           充电桩编号
     * @param hardversion    硬件版本
     * @param softversion    软件版本
     * @param subHardversion 次单元硬件版本
     * @param subSoftversion 次单元软件版本
     * @param dcModeltype    直流模块类型
     * @param dcModelnum     直流模块总数
     * @param dcModelpower   直流模块单模块功
     * @param location       地址详情
     * @param lon            经度
     * @param lat            纬度
     * @param remark         充电桩昵称
     * @return
     */
    @RequestMapping(value = "/updateDevice", method = RequestMethod.GET)
    @ResponseBody
    public Object updateDevice( EquipmentNew equ) {
        return equipmentService.updateEquipmentNew(equ);
    }

    /**
     * @Author
     * @Description删除设备信息
     * @Date 2020/10/13 14:34
     * @Param [id]
     **/
    @RequestMapping(value = "/delEquipmentNewById")
    @ResponseBody
    public Object delEquipmentNewById(String code) {
        return equipmentService.delEquipmentNewById(code);
    }


    /**
     * @Author
     * @Description判断设备编号是否存在
     * @Date 2020/10/13 14:34
     * @Param [id]
     **/
    @RequestMapping(value = "/selectDeviceExsit")
    @ResponseBody
    public Object selectDeviceExsit(String code) {
        Object result = null;
        Map<String, Object> map = new HashMap<>();
        try {
            boolean isTrue = equipmentService.selectDeviceExsit(code);
            map.put("isTrue", isTrue);
           result = CommUtil.responseBuildInfo(200, "查询成功", map);
           return JSON.toJSON(result);
        } catch (Exception e) {
            e.printStackTrace();
            map.put("isTrue", "查询失败");
            return CommUtil.responseBuildInfo(201, "查询失败", map);
        }

    }

}

package com.hedong.hedongwx.service.impl;


import java.math.BigDecimal;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

import javax.servlet.http.HttpServletRequest;

import com.hedong.hedongwx.dao.*;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.hedong.hedongwx.config.CommonConfig;
import com.hedong.hedongwx.entity.AllPortStatus;
import com.hedong.hedongwx.entity.Area;
import com.hedong.hedongwx.entity.CodeSystemParam;
import com.hedong.hedongwx.entity.Codeoperatelog;
import com.hedong.hedongwx.entity.Codestatistics;
import com.hedong.hedongwx.entity.Equipment;
import com.hedong.hedongwx.entity.EquipmentNew;
import com.hedong.hedongwx.entity.PageBean;
import com.hedong.hedongwx.entity.Parameters;
import com.hedong.hedongwx.entity.Realchargerecord;
import com.hedong.hedongwx.entity.TemplateParent;
import com.hedong.hedongwx.entity.TemplateSon;
import com.hedong.hedongwx.entity.User;
import com.hedong.hedongwx.entity.UserEquipment;
import com.hedong.hedongwx.service.AllPortStatusService;
import com.hedong.hedongwx.service.BasicsService;
import com.hedong.hedongwx.service.EquipmentService;
import com.hedong.hedongwx.service.TemplateService;
import com.hedong.hedongwx.service.UserEquipmentService;
import com.hedong.hedongwx.thread.Server;
import com.hedong.hedongwx.utils.CommUtil;
import com.hedong.hedongwx.utils.HttpRequest;
import com.hedong.hedongwx.utils.JedisUtils;
import com.hedong.hedongwx.utils.PageUtils;
import com.hedong.hedongwx.utils.StringUtil;
import com.hedong.hedongwx.utils.TempMsgUtil;
import com.hedong.hedongwx.utils.WeiXinConfigParam;
import com.hedong.hedongwx.utils.yinlian.SignUtils;

import net.sf.json.JSONObject;

@Service
public class EquipmentServiceImpl implements EquipmentService {
    private static final Logger looger = LoggerFactory.getLogger(EquipmentServiceImpl.class);
    @Autowired
    private EquipmentDao equipmentDao;
    @Autowired
    private UserEquipmentDao userEquipmentDao;
    @Autowired
    private CodestatisticsDao codestatisticsDao;
    @Autowired
    private UserDao userDao;
    @Autowired
    private TemplateDao templateDao;
    @Autowired
    private TemplateSonDao templateSonDao;
    @Autowired
    private AreaDao areaDao;
    @Autowired
    private TradeRecordDao tradeRecordDao;
    @Autowired
    private CodeoperatelogDao codeoperatelogDao;
    @Autowired
    private InCoinsDao inCoinsDao;
    @Autowired
    private RealchargerecordDao realchargerecordDao;
    @Autowired
    private CodeSystemParamDao codeSystemParamDao;
    @Autowired
    private ChargeRecordDao chargeRecordDao;
    @Autowired
    private OfflineCardDao offlineCardDao;
    @Autowired
    private AllPortStatusService allPortStatusService;
    @Autowired
    private MerchantDetailDao merchantDetailDao;
    @Autowired
    private OperateRecordDao operateRecordDao;
    @Autowired
    private HttpServletRequest request;
    @Autowired
    private BasicsService basicsService;
    @Autowired
    private TemplateService TemplateService;
    @Autowired
    private UserEquipmentService userEquipmentService;
    @Autowired
    private Server server;
    @Autowired
    private EquipmentNewDao equipmentNewDao;

    @Autowired
    private AllPortStatusDao allPortStatusDao;


    @Transactional
    @Override
    public int addEquipment(Equipment equipment) {
        if (equipment.getCode() == null || "".equals(equipment.getCode())) {
            return 0;
        }
        return equipmentDao.addEquipment(equipment);
    }

    @Override
    public int insertCodeoperatelog(String code, Integer sort, Integer type, Integer merid, Integer opeid
            , String remark) {
        Codeoperatelog codelog = new Codeoperatelog();
        codelog.setCode(code);
        codelog.setSort(sort);
        codelog.setType(type);
        codelog.setMerid(merid);
        codelog.setOpeid(opeid);
        codelog.setRemark(remark);
        codelog.setOperateTime(new Date());
        return codeoperatelogDao.insertCodeoperatelog(codelog);
    }

    @Override
    public int addBluetoothDevice(Equipment equipment) {
        return equipmentDao.addBluetoothDevice(equipment);
    }

    @Override
    public String selectEndDeviceNum(Integer deviceType) {
        return equipmentDao.selectEndDeviceNum(deviceType);
    }

    @Override
    public List<Codestatistics> selectAllByCode(String code, Integer merid) {
        return codestatisticsDao.selectAllByCode(code, merid);
    }

    @Override
    public List<Codestatistics> selectAllByCodeAndLimit(String code, Integer merid, Integer beginnum, Integer endnum) {
        return codestatisticsDao.selectAllByCodeAndLimit(code, merid, beginnum, endnum);
    }

    @Override
    public List<Equipment> getEquipmentList() {
        List<Equipment> result = equipmentDao.getEquipmentList();
        return result != null ? result : new ArrayList<Equipment>();
    }

    @Override
    public Equipment getEquipmentById(String code) {
        Equipment result = equipmentDao.getEquipmentById(code);
        return result != null ? result : new Equipment();
    }

    public Equipment getEquipmentAndAreaById(String code) {
        return equipmentDao.getEquipmentAndAreaById(code);
    }

    @Transactional
    @Override
    public int updateEquipment(Equipment equipment) {
        return equipmentDao.updateEquipment(equipment);
    }

    @Transactional
    @Override
    public int deleteEquipmentById(Integer id) {
        return equipmentDao.deleteEquipmentById(id);
    }

    @Override
    public List<Equipment> selectEquListByUidAndBindtype(Integer uid, Byte state, String source, String parameter, Integer startnum, Integer equnum) {
        return equipmentDao.selectEquListByUidAndBindtype(uid, state, source, parameter, startnum, equnum);
    }

    @Override
    public Integer selectEquListByUidAndBindtypeNum(Integer uid, Byte state) {
        return equipmentDao.selectEquListByUidAndBindtypeNum(uid, state);
    }

    @Override
    public int updateTempidByEquipmentCode(String code, Integer tempid) {
        return equipmentDao.updateTempidByEquipmentCode(code, tempid);
    }

    @Override
    public PageBean<Equipment> findAllEquipmentPage(HttpServletRequest request) {
        Equipment equipment = new Equipment();
        String state = request.getParameter("state");
        String line = request.getParameter("line");// -1 请选择 、0 未绑定、1 已绑定
        if (null != state && !state.equals("-1"))
            equipment.setState((byte) Integer.parseInt(state));
        if (null != line && !line.equals("-1"))
            equipment.setBindtype((byte) Integer.parseInt(line));
        equipment.setCode(request.getParameter("code"));
        equipment.setImei(request.getParameter("imei"));
        equipment.setCcid(request.getParameter("ccid"));
        equipment.setUsername(request.getParameter("username"));
        List<Equipment> equi = equipmentDao.selectEquipment(equipment);
        int totalRecord = equi.size();
        int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));// 条数
        int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));// 当前页码
        if (numPerPage == 0)
            numPerPage = 10;
        if (currentPage < 1)
            currentPage = 1;
        PageBean<Equipment> equpb = new PageBean<>(currentPage, numPerPage, totalRecord);

        equipment.setNumPerPage(equpb.getPageSize());
        equipment.setStartIndex(equpb.getStartIndex());
        equpb.setList(equipmentDao.selectEquipment(equipment));
        return equpb;
    }

    @Override
    public int updataBindtypeByEquipmentCode(Byte bindtype, String code) {
        return equipmentDao.updataBindtypeByEquipmentCode(bindtype, code);
    }

    @Override
    public PageUtils<Parameters> selectEquipmentParameter(HttpServletRequest request) {
        int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
        int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
        PageUtils<Parameters> page = new PageUtils<>(numPerPage, currentPage);
        Parameters parameter = new Parameters();
        parameter.setCode(request.getParameter("code"));
        parameter.setNickname(request.getParameter("username"));
        parameter.setPhone(request.getParameter("phone"));
        parameter.setParamete(request.getParameter("imei"));
        parameter.setStatement(request.getParameter("ccid"));
        parameter.setType(request.getParameter("line"));// -1 请选择 、0 未绑定、1 已绑定
        parameter.setSource(request.getParameter("hardversion"));
        parameter.setState(request.getParameter("softversionnum"));
        parameter.setStatus(request.getParameter("state"));
        parameter.setSort(request.getParameter("csq"));

        List<Map<String, Object>> equiMap = equipmentDao.selectEquipmentParameter(parameter);
        page.setTotalRows(equiMap.size());
        page.setTotalPages();
        page.setStart();
        page.setEnd();
        parameter.setPages(page.getNumPerPage());
        parameter.setStartnumber(page.getStartIndex());
        page.setListMap(equipmentDao.selectEquipmentParameter(parameter));
        return page;
    }

    @Override
    public PageUtils<Equipment> selectEquipment(HttpServletRequest request) {
        int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));// 条数
        int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));// 页码

        PageUtils<Equipment> page = new PageUtils<>(numPerPage, currentPage);

        Equipment equipment = new Equipment();
        String state = request.getParameter("state");
        String line = request.getParameter("line");// -1 请选择 、0 未绑定、1 已绑定
        if (null != state && !state.equals("-1"))
            equipment.setState((byte) Integer.parseInt(state));
        if (null != line && !line.equals("-1"))
            equipment.setBindtype((byte) Integer.parseInt(line));
        equipment.setCode(request.getParameter("code"));
        equipment.setImei(request.getParameter("imei"));
        equipment.setCcid(request.getParameter("ccid"));
        equipment.setHardversion(request.getParameter("hardversion"));
        equipment.setSoftversionnum(request.getParameter("softversionnum"));
        equipment.setCsq(request.getParameter("csq"));
        equipment.setUsername(request.getParameter("username"));
        equipment.setPhoneNum(request.getParameter("phoneNum"));
        equipment.setAid(StringUtil.getIntString(request.getParameter("aid")));
        List<Equipment> equi = equipmentDao.selectEquipment(equipment);
        page.setTotalRows(equi.size());
        page.setTotalPages();
        page.setStart();
        page.setEnd();
        equipment.setNumPerPage(page.getNumPerPage());
        equipment.setStartIndex(page.getStartIndex());
        page.setList(equipmentDao.selectEquipment(equipment));
        return page;
    }

    @Override
    public String selectdisequ() {
        return equipmentDao.selectdisequ();
    }

    @Override
    public int updateEquHardversionByCode(String code, String hardversion) {
        return equipmentDao.updateEquHardversionByCode(code, hardversion);
    }

    @Override
    public List<Equipment> selectEqulistByAid(Integer aid) {
        return equipmentDao.selectEqulistByAid(aid);
    }

    @Override
    public List<Equipment> selectUnbindEqulist(Integer uid) {
        return equipmentDao.selectUnbindEqulist(uid);
    }

    @Override
    public List<Equipment> selectEquDisRelAid(Integer uid) {
        return equipmentDao.selectEquDisRelAid(uid);
    }

    @Override
    public int updateEquAidByAid(Integer aid) {
        return equipmentDao.updateEquAidByAid(aid);
    }

    @Override
    public List<String> selectCodeByAreaManNotNull(Integer merid, Integer manid) {
        return equipmentDao.selectCodeByAreaManNotNull(merid, manid);
    }

    @Override
    public List<String> selectCodeByAreaManIsNull(Integer merid, Integer manid) {
        return equipmentDao.selectCodeByAreaManIsNull(merid, manid);
    }

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/
    //查询设备列表 (跳转设备列表)
    @Override
    public PageUtils<Parameters> selectEquList(HttpServletRequest request) {
        int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
        int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
        PageUtils<Parameters> page = new PageUtils<>(numPerPage, currentPage);
        Parameters parameters = new Parameters();
        User user = CommonConfig.getAdminReq(request);
        Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
        if (rank != 0) parameters.setUid(user.getId());//获取用户
        parameters.setDeviceType(1);
        List<Map<String, Object>> equiMaps = equipmentDao.selectEquList(parameters);
        Map<String, Object> param = new HashMap<>();
        int totalRows = equiMaps.size();
        int online = 0;
        int binding = 0;
        for (Map<String, Object> iteam : equiMaps) {
            if (iteam.get("state").toString().equals("1")) {//在线
                online += 1;
            }
            if (iteam.get("bindtype").toString().equals("1")) {//绑定
                binding += 1;
            }
        }
        param.put("totalRows", totalRows);
        param.put("online", online);
        param.put("disonline", totalRows - online);
        param.put("binding", binding);
        param.put("disbinding", totalRows - binding);
        page.setMap(param);
        parameters.setCode(request.getParameter("code"));
        parameters.setNickname(request.getParameter("username"));
        parameters.setPhone(request.getParameter("phoneNum"));
        parameters.setSort(request.getParameter("imei"));
        parameters.setState(request.getParameter("ccid"));
        parameters.setStatement(request.getParameter("hardversion"));
        parameters.setNumber(request.getParameter("softversionnum"));
        parameters.setOrder(request.getParameter("hardversionnum"));
        parameters.setType(request.getParameter("line"));// -1 请选择 、0 未绑定、1 已绑定
        parameters.setStatus(request.getParameter("state"));
//		parameters.setRemark(request.getParameter("csq"));
        parameters.setSource(request.getParameter("aid"));
        parameters.setParamete(request.getParameter("areastate"));

        String testnum = request.getParameter("testnum");
        if ("1".equals(testnum)) {
            parameters.setType("1");
        } else if ("2".equals(testnum)) {
            parameters.setLevel("0,1,2,3,4,5,6,7,8,9");
            parameters.setType("0");
        } else if ("3".equals(testnum)) {
            parameters.setLevel("10");
        }
        List<Map<String, Object>> equiMap = equipmentDao.selectEquList(parameters);
        page.setTotalRows(equiMap.size());
        page.setTotalPages();
        page.setStart();
        page.setEnd();
        parameters.setPages(page.getNumPerPage());
        parameters.setStartnumber(page.getStartIndex());
        page.setListMap(equipmentDao.selectEquList(parameters));
        return page;
    }

    //查询设备列表 (跳转设备列表)
    @Override
    public PageUtils<Parameters> selectBluetoothEquList(HttpServletRequest request) {
        int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
        int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码
        PageUtils<Parameters> page = new PageUtils<>(numPerPage, currentPage);
        Parameters parameters = new Parameters();
        User user = CommonConfig.getAdminReq(request);
        Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
        if (rank != 0) parameters.setUid(user.getId());//获取用户
        parameters.setDeviceType(2);
        List<Map<String, Object>> equiMaps = equipmentDao.selectEquList(parameters);
        Map<String, Object> param = new HashMap<>();
        int totalRows = equiMaps.size();
        int online = 0;
        int binding = 0;
        for (Map<String, Object> iteam : equiMaps) {
            if (iteam.get("bindtype").toString().equals("1")) {//绑定
                binding += 1;
            }
        }
        param.put("totalRows", totalRows);
        param.put("online", online);
        param.put("disonline", totalRows - online);
        param.put("binding", binding);
        param.put("disbinding", totalRows - binding);
        page.setMap(param);
        parameters.setCode(request.getParameter("code"));
        parameters.setNickname(request.getParameter("username"));
        parameters.setPhone(request.getParameter("phoneNum"));
        parameters.setStatement(request.getParameter("hardversion"));
        parameters.setNumber(request.getParameter("softversionnum"));
        parameters.setType(request.getParameter("line"));// -1 请选择 、0 未绑定、1 已绑定
        parameters.setSource(request.getParameter("aid"));
        parameters.setParamete(request.getParameter("areastate"));

        String testnum = request.getParameter("testnum");
        if ("1".equals(testnum)) {
            parameters.setType("1");
        } else if ("2".equals(testnum)) {
            parameters.setLevel("0,1,2,3,4,5,6,7,8,9");
            parameters.setType("0");
        } else if ("3".equals(testnum)) {
            parameters.setLevel("10");
        }
        List<Map<String, Object>> equiMap = equipmentDao.selectEquList(parameters);
        page.setTotalRows(equiMap.size());
        page.setTotalPages();
        page.setStart();
        page.setEnd();
        parameters.setPages(page.getNumPerPage());
        parameters.setStartnumber(page.getStartIndex());
        page.setListMap(equipmentDao.selectEquList(parameters));
        return page;
    }

    //根据设备号查询测试的次数
    @Override
    public String selectEquiTestSeveral(String code) {
        return equipmentDao.selectEquiTestSeveral(code);
    }

    //根据设备号更新测试的次数
    @Override
    public int updateEquiTestSeveral(String code, Integer several) {
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setSeveral(several);
        return equipmentDao.updateEquipment(equipment);
    }

    //查询设备列表(根据uid查询对应商户下的设备；管理员为所有)
    @Override
    public List<Map<String, Object>> selectEquListByParam(Parameters parameters) {
        List<Map<String, Object>> equiMaps = equipmentDao.selectEquList(parameters);
        return equiMaps;
    }

    /**
     * 强制解绑时统计计算该设备的今日收益
     */
    @Override
    public void codeCollectmoney(String code, Integer dealid, Integer areaid, String hardversion) {
        try {
            String startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
            String endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
            Integer type = 1;
            String devicenum = CommUtil.toString(code);
            hardversion = CommUtil.toString(hardversion);
            Parameters parameters = new Parameters();
            parameters.setStartTime(startTime);
            parameters.setEndTime(endTime);
            parameters.setDealer(CommUtil.toString(dealid));
            parameters.setCode(devicenum);

            Map<String, Object> collectmoney = CommUtil.isMapEmpty(tradeRecordDao.timingCollectMoney(parameters));
            int incoins = 0;
            double incoinsmoney = 0.00;
            if (!hardversion.equals("04")) {
                Map<String, Object> coinscollect = CommUtil.isMapEmpty(inCoinsDao.selectcoinsup(parameters));
                incoins = CommUtil.toInteger(coinscollect.get("incoins"));
                incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
            }
            collectmoney.put("incoins", incoins);
            collectmoney.put("incoinsmoney", incoinsmoney);
            Map<String, Object> consumebytimes = CommUtil.isMapEmpty(chargeRecordDao.selectCodeConsumeQuantity(parameters));
            collectmoney.put("consumequantity", CommUtil.toInteger(consumebytimes.get("consumeQuantity")));
            collectmoney.put("consumetime", CommUtil.toInteger(consumebytimes.get("consumetime")));
            insertCollectData(code, CommUtil.toInteger(dealid), CommUtil.toInteger(areaid), type, collectmoney, startTime);

            List<UserEquipment> codesize = userEquipmentDao.getUserEquipmentById(dealid);
            if (codesize.size() == 0) {
                Parameters parame = new Parameters();
                parame.setStartTime(startTime);
                parame.setEndTime(endTime);
                parame.setDealer(CommUtil.toString(dealid));
                String time = StringUtil.toDateTime("yyyy-MM-dd");
                //查询作为商户时的在线收益
                Map<String, Object> result = CommUtil.isMapEmpty(merchantDetailDao.dealerIncomeCount(parame));
                //脉冲投币收益
                Map<String, Object> coinsincome = CommUtil.isMapEmpty(inCoinsDao.selectcoinsup(parame));
                //商户收益中在线卡、串口投币、离线卡统计
                Map<String, Object> cardandpulsecollect = CommUtil.isMapEmpty(chargeRecordDao.seleCardAndPulseCollect(parame));
                //查询商家的耗电量和耗电时间
                Map<String, Object> codeStatisticMer = CommUtil.isMapEmpty(chargeRecordDao.codeStatisticMer(dealid, startTime, endTime));
                Integer merincoins = CommUtil.toInteger(coinsincome.get("incoins"));
                Double merincoinsmoney = CommUtil.toDouble(coinsincome.get("incoinsmoney"));
                Integer consumequantity = CommUtil.toInteger(codeStatisticMer.get("consumequantity"));
                Integer consumetime = CommUtil.toInteger(codeStatisticMer.get("consumetime"));
                Double windowpulsemoney = CommUtil.toDouble(cardandpulsecollect.get("windowpulsemoney"));
                Double offcardmoney = CommUtil.toDouble(cardandpulsecollect.get("offcardmoney"));
                Double oncardmoney = CommUtil.toDouble(cardandpulsecollect.get("oncardmoney"));
                result.put("windowpulsemoney", windowpulsemoney);
                result.put("offcardmoney", offcardmoney);
                result.put("oncardmoney", oncardmoney);
                result.put("consumeQuantity", consumequantity);
                result.put("consumetime", consumetime);
                result.put("incoins", merincoins);
                result.put("incoinsmoney", merincoinsmoney);
                insertCollectData(code, dealid, CommUtil.toInteger(areaid), type, result, time);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void deviceIncomeDataGain(String code, Integer dealid, Integer areaid, String hardversion) {
        try {
            String startTime = StringUtil.toDateTime("yyyy-MM-dd 00:00:00");
            String endTime = StringUtil.toDateTime("yyyy-MM-dd 23:59:59");
            Integer type = 1;
            String devicenum = CommUtil.toString(code);
            hardversion = CommUtil.toString(hardversion);
            Parameters parameters = new Parameters();
            parameters.setStartTime(startTime);
            parameters.setEndTime(endTime);
            parameters.setDealer(CommUtil.toString(dealid));
            parameters.setCode(devicenum);

            Map<String, Object> collectmoney = CommUtil.isMapEmpty(tradeRecordDao.timingCollectMoney(parameters));
            int incoins = 0;
            double incoinsmoney = 0.00;
            if (!hardversion.equals("04")) {
                Map<String, Object> coinscollect = CommUtil.isMapEmpty(inCoinsDao.selectcoinsup(parameters));
                incoins = CommUtil.toInteger(coinscollect.get("incoins"));
                incoinsmoney = CommUtil.toDouble(coinscollect.get("incoinsmoney"));
            }
            collectmoney.put("incoins", incoins);
            collectmoney.put("incoinsmoney", incoinsmoney);
            Map<String, Object> consumebytimes = CommUtil.isMapEmpty(chargeRecordDao.selectCodeConsumeQuantity(parameters));
            collectmoney.put("consumequantity", CommUtil.toInteger(consumebytimes.get("consumeQuantity")));
            collectmoney.put("consumetime", CommUtil.toInteger(consumebytimes.get("consumetime")));
            insertCollectData(code, CommUtil.toInteger(dealid), CommUtil.toInteger(areaid), type, collectmoney, startTime);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * employ
     *
     * @Description：直接插入汇总数据
     * @author： origin
     */
    public void insertCollectData(String code, Integer merid, Integer areaid, Integer type, Map<String, Object> result, String time) {
        try {
            code = CommUtil.toString(code);
            merid = CommUtil.toInteger(merid);
            areaid = CommUtil.toInteger(areaid);
            type = CommUtil.toInteger(type);

            Integer wechatnum = CommUtil.toInteger(result.get("wechatnum"));
            Integer alipaynum = CommUtil.toInteger(result.get("alipaynum"));
            Integer refwechatnum = CommUtil.toInteger(result.get("refwechatnum"));
            Integer refalipaynum = CommUtil.toInteger(result.get("refalipaynum"));

            Double wechatmoney = CommUtil.toDouble(result.get("wechatmoney"));
            Double alipaymoney = CommUtil.toDouble(result.get("alipaymoney"));
            Double refwechatmoney = CommUtil.toDouble(result.get("refwechatmoney"));
            Double refalipaymoney = CommUtil.toDouble(result.get("refalipaymoney"));

            /*Double paymentmoney = CommUtil.toDouble(result.get("paymentmoney"));*/

            Double incomepaymentmoney = CommUtil.toDouble(result.get("incomepaymentmoney"));
            Double wechatpaymentmoney = CommUtil.toDouble(result.get("wechatpaymentmoney"));
            Double paymentmoney = CommUtil.addBig(wechatpaymentmoney, incomepaymentmoney);


            Integer incoins = CommUtil.toInteger(result.get("incoins"));
            Double incoinsmoney = CommUtil.toDouble(result.get("incoinsmoney"));

            Integer consumetime = CommUtil.toInteger(result.get("consumetime"));
            Integer consumequantity = CommUtil.toInteger(result.get("consumequantity"));

            Double offcardmoney = CommUtil.toDouble(result.get("offcardmoney"));
            Double oncardmoney = CommUtil.toDouble(result.get("oncardmoney"));
            Double windowpulsemoney = CommUtil.toDouble(result.get("windowpulsemoney"));

//			Integer offconsumetime = CommUtil.toInteger(result.get("offconsumetime"));
//			Integer offconsumequantity = CommUtil.toInteger(result.get("offconsumequantity"));
//			Integer onconsumetime = CommUtil.toInteger(result.get("onconsumetime"));
//			Integer onconsumequantity = CommUtil.toInteger(result.get("onconsumequantity"));
//			Integer pulseconsumetime = CommUtil.toInteger(result.get("pulseconsumetime"));
//			Integer pulseconsumequantity = CommUtil.toInteger(result.get("pulseconsumequantity"));

            Double totalmoney = CommUtil.addBig(wechatmoney, alipaymoney);
            Double onlinereturnmoney = CommUtil.addBig(refwechatmoney, refalipaymoney);
            Double onlinemoney = CommUtil.subBig(totalmoney, onlinereturnmoney);
            Double incomemoney = CommUtil.subBig(onlinemoney, incomepaymentmoney);

            Integer totalorder = CommUtil.toInteger(wechatnum + alipaynum);
            Integer onreturnorder = CommUtil.toInteger(refwechatnum + refalipaynum);
            Integer onlineorder = CommUtil.toInteger(totalorder - onreturnorder);
//			  `count_time` date DEFAULT NULL COMMENT '结算时间',
            Codestatistics collectData = new Codestatistics();

            collectData.setCode(code);
            collectData.setMerid(merid);
            collectData.setAreaid(areaid);
            collectData.setType(type);

            collectData.setOrdertotal(totalorder);
            collectData.setMoneytotal(totalmoney);

            collectData.setWechatorder(wechatnum);
            collectData.setAlipayorder(alipaynum);
            collectData.setWechatretord(refwechatnum);
            collectData.setAlipayretord(refalipaynum);

            collectData.setWechatmoney(wechatmoney);
            collectData.setAlipaymoney(alipaymoney);
            collectData.setWechatretmoney(refwechatmoney);
            collectData.setAlipayretmoney(refalipaymoney);

            collectData.setPaymentmoney(paymentmoney);

            collectData.setIncoinsorder(incoins);
            collectData.setIncoinsmoney(incoinsmoney);

            collectData.setConsumequantity(consumequantity);
            collectData.setConsumetime(consumetime);

            collectData.setOffcardmoney(offcardmoney);
//			collectData.setOffconsumetime(offconsumetime);;
//			collectData.setOffconsumequantity(offconsumequantity);
            collectData.setWindowpulsemoney(windowpulsemoney);
//			collectData.setPulseconsumetime(pulseconsumetime);
//			collectData.setPulseconsumequantity(pulseconsumequantity);
            collectData.setOncardmoney(oncardmoney);
//			collectData.setOnconsumetime(onconsumetime);
//			collectData.setOnconsumequantity(onconsumequantity);
            Date codetime = CommUtil.DateTime(time, "yyyy-MM-dd");
            collectData.setCountTime(codetime);
            codestatisticsDao.insertCodestatis(collectData);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //查询设备操作日志
    @Override
    public PageUtils<Parameters> selectCodeOperateLog(HttpServletRequest request) {
        int numPerPage = StringUtil.getIntString(request.getParameter("numPerPage"));//条数
        int currentPage = StringUtil.getIntString(request.getParameter("currentPage"));//页码

        String startTime = request.getParameter("startTime");
        String endTime = request.getParameter("endTime");
        if (null == startTime || startTime.equals(""))
            startTime = StringUtil.getnumterday("yyyy-MM-dd HH:mm:ss", StringUtil.toDateTime(), 3);
        if (null == endTime || endTime.equals("")) endTime = StringUtil.getCurrentDateTime();

        PageUtils<Parameters> page = new PageUtils<>(numPerPage, currentPage);
        Parameters parameters = new Parameters();
        User user = CommonConfig.getAdminReq(request);
        Integer rank = user.getLevel();// 0:管理员、 1:普通用户、 2:商户、  3:代理商、4：小区管理
        if (rank != 0) parameters.setUid(user.getId());//获取商户
        parameters.setCode(request.getParameter("code"));
        parameters.setDealer(request.getParameter("dealer"));//商户昵称
        parameters.setRealname(request.getParameter("realname"));//商户姓名
        parameters.setPhone(request.getParameter("phone"));//商户电话
        parameters.setNickname(request.getParameter("nickname"));//操作人昵称
        parameters.setUsername(request.getParameter("username"));//操作人名字

        int sort = StringUtil.getIntString(request.getParameter("sort"));
        if (sort == 1) {
            parameters.setSort("1");
            parameters.setType("1");
        } else if (sort == 2) {
            parameters.setSort("1");
            parameters.setType("2");
        } else if (sort == 3) {
            parameters.setSort("2");
            parameters.setType("1");
        } else if (sort == 4) {
            parameters.setSort("2");
            parameters.setType("2");
        } else if (sort == 5) {
            parameters.setSort("3");
            //parameters.setType("1");
        } else if (sort == 6) {
            parameters.setSort("4");
        }
        parameters.setStartTime(startTime);
        parameters.setEndTime(endTime);

        List<Map<String, Object>> operate = codeoperatelogDao.selectoperatelog(parameters);
        page.setTotalRows(operate.size());
        page.setTotalPages();
        page.setStart();
        page.setEnd();
        parameters.setPages(page.getNumPerPage());
        parameters.setStartnumber(page.getStartIndex());
        List<Map<String, Object>> tradepage = codeoperatelogDao.selectoperatelog(parameters);
        page.setListMap(tradepage);
        return page;
    }

    /**
     * @Description： 根据用户id查询合伙人绑定的设备
     * @author： origin   
     */
    @Override
    public List<Equipment> selectpartnercode(Integer id) {
        return equipmentDao.selectpartnercode(id);
    }

    /**
     * @Description： 根据条件查询用户绑定的设备与作为合伙人的设备
     * @author： origin   
     */
    @Override
    public List<Map<String, Object>> selectrelatedcode(Integer uid, Byte state, String source, String parameter, Integer startnum,
                                                       Integer equnum) {
        List<Map<String, Object>> devicenum = equipmentDao.selectrelatedcode(uid, state, source, parameter, startnum, equnum);
        return CommUtil.isListMapEmpty(devicenum);
    }

    /** ============================================================================================================= */

    /**
     * @return hd_realchargerecord
     * @Description： 根据条件查询用户消费信息
     * @author： origin 
     */
    @Override
    public List<Realchargerecord> realChargeRecordList(Integer orderId) {
        orderId = CommUtil.toInteger(orderId);
        List<Realchargerecord> realrecord = realchargerecordDao.realChargeRecordList(orderId, null, null, null, null);
        return realrecord == null ? new ArrayList<Realchargerecord>() : realrecord;
    }

    /**
     * @Description： 根据条件查询数据的函数情况  1：根据充电id(chargeid)
     * @author： origin 
     * @return maxeRecord hd_realchargerecord
     */
    @Override
    public Map<String, Object> functionRecord(Integer orderId) {
        Parameters pare = new Parameters();
        pare.setOrder(orderId.toString());
        Map<String, Object> func = realchargerecordDao.functionRecord(pare);

        return func;
    }

    /**
     * @return hd_realchargerecord
     * @Description： 根据实体类添加消费信息
     * @author： origin 
     */
    @Override
    public int insertRealRecord(Integer chargeid, Integer uid, Integer merid, String code, Integer port, Integer type,
                                Integer chargetime, Integer surpluselec, Integer power, double money) {
        Realchargerecord realrecord = new Realchargerecord();
        realrecord.setChargeid(chargeid);
        realrecord.setUid(uid);
        realrecord.setMerid(merid);
        realrecord.setCode(code);
        realrecord.setPort(port);
        realrecord.setType(type);
        realrecord.setChargetime(chargetime);
        realrecord.setSurpluselec(surpluselec);
        realrecord.setPower(power);
        realrecord.setMoney(money);
        //realrecord.setCreatetime(createtime);
        int realcha = realchargerecordDao.insertRealRecord(realrecord);
        return realcha;
    }

    /*** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** *** ***/

    @Override
    public Equipment selectBluetoothMac(String code) {
        return equipmentDao.selectBluetoothMac(code);
    }

    @Override
    public Equipment selectBluetoothExist(String deviceId) {
        return equipmentDao.selectBluetoothExist(deviceId);
    }

    @Override
    public int updateEquLivenumByCode(String code) {
        Equipment equipmentById = equipmentDao.getEquipmentById(code);
        Equipment equipment = new Equipment();
        equipment.setCode(equipmentById.getCode());
        equipment.setLiveTime(new Date());
        return equipmentDao.updateEquLiveTimeByCode(equipment);
    }

    @Override
    public Integer selectLast24Hours() {
        return equipmentDao.selectLast24Hours();
    }

    @Override
    public Integer selectLastMonth() {
        return equipmentDao.selectLastMonth();
    }

    @Override
    public String queryCodeBindPhone(String code) {
        String phoneNum = "4006315515";
        Equipment equipment = equipmentDao.getEquipmentById(code);
        if (equipment != null) {
            TemplateParent templateParent = templateDao.getParentTemplateOne(equipment.getTempid());
            if (templateParent != null && templateParent.getCommon1() != null && !"".equals(templateParent.getCommon1())) {
                phoneNum = templateParent.getCommon1();
            } else {
                UserEquipment userEquipment = userEquipmentDao.getUserEquipmentByCode(code);
                if (userEquipment != null) {
                    User user = userDao.selectUserById(userEquipment.getUserId());
                    if (user != null) {
                        phoneNum = user.getPhoneNum();
                    }
                }
            }
        }
        return phoneNum;
    }

    @Override
    public int insertCodeSystemParam(CodeSystemParam codeSystemParam) {
        return codeSystemParamDao.insertCodeSystemParam(codeSystemParam);
    }

    @Override
    public int updateCodeSystemParam(CodeSystemParam codeSystemParam) {
        return codeSystemParamDao.updateCodeSystemParam(codeSystemParam);
    }

    @Override
    public CodeSystemParam selectCodeSystemParamByCode(String code) {
        return codeSystemParamDao.selectCodeSystemParamByCode(code);
    }

    @Override
    public Integer updateEquipmentRemark(String remark, String code) {
        return equipmentDao.updateEquipmentRemark(remark, code);
    }

    @Override
    public List<Codestatistics> selectOneByCodeAndTime(String code, String countTime) {
        return codestatisticsDao.selectOneByCodeAndTime(code, countTime);
    }

    @Override
    public void forEquCollet() {
        List<Equipment> equipmentList = equipmentDao.getEquipmentList();
        for (Equipment equipment : equipmentList) {
            String code = equipment.getCode();
            if (equipment.getBindtype() == 1) {
                UserEquipment userEquipment = userEquipmentDao.getUserEquipmentByCode(code);
                if (userEquipment != null) {
                    Integer merid = userEquipment.getUserId();
                    String hardversion = equipment.getHardversion();
                    Equipment equipment2 = new Equipment();
                    equipment2.setCode(code);
                    Double onlineMoney = 0.0;
                    Double incoinsMoney = 0.0;
                    if ("04".equals(hardversion)) {
                        onlineMoney = offlineCardDao.selectTotalMoneyByMeridAndEqunum(code, merid);
                        System.out.println("onlineMoney===" + onlineMoney);
                        if (onlineMoney != null && onlineMoney != 0) {
                            equipment2.setTotalOnlineEarn(onlineMoney);
                        }
                    } else if ("03".equals(hardversion)) {
                        onlineMoney = inCoinsDao.selectTotalMoneyByEqunumAndMerid(merid, code, 1);
                        incoinsMoney = inCoinsDao.selectTotalMoneyByEqunumAndMerid(merid, code, 2);
                        System.out.println(code + "---");
                        System.out.println("onlineMoney===" + onlineMoney);
                        System.out.println("incoinsMoney===" + incoinsMoney);
                        if (onlineMoney != null && onlineMoney != 0) {
                            equipment2.setTotalOnlineEarn(onlineMoney);
                        }
                        if (incoinsMoney != null && incoinsMoney != 0) {
                            equipment2.setTotalCoinsEarn(doubleToInt(incoinsMoney));
                        }
                    } else {
                        onlineMoney = chargeRecordDao.selectEquipmentTotalMoneyByMerid(merid, code);
                        incoinsMoney = inCoinsDao.selectTotalMoneyByEqunumAndMerid(merid, code, 2);
                        System.out.println("onlineMoney===" + onlineMoney);
                        System.out.println("incoinsMoney===" + incoinsMoney);
                        if (onlineMoney != null && onlineMoney != 0) {
                            equipment2.setTotalOnlineEarn(onlineMoney);
                        }
                        if (incoinsMoney != null && incoinsMoney != 0) {
                            equipment2.setTotalCoinsEarn(doubleToInt(incoinsMoney));
                        }
                    }
                    System.out.println(equipment2);
                    if ((onlineMoney != null && onlineMoney != 0.0) ||
                            (incoinsMoney != null && incoinsMoney != 0)) {
                        equipmentDao.updateEquipment(equipment2);
                    }
                }
            }
        }
    }

    int doubleToInt(Double d) {
        String money = String.valueOf(d);
        int idx = money.lastIndexOf(".");
        String doubleStr = money.substring(0, idx);
        int int1 = Integer.parseInt(doubleStr);
        return int1;
    }

    @Override
    public int updateEquEarn(String code, Double money, Integer type) {
        return equipmentDao.updateEquEarn(code, money, type);
    }

    /**
     * @Description： 指定记录对象id
     * @author： origin 创建时间：   2019年7月30日 下午5:05:02
     */
    @Override
    public List<Map<String, Object>> specifiedUsePortInfo(Integer merid, String code, Integer port) {
        Parameters param = new Parameters();
        param.setCode(code);
        param.setDealer(merid.toString());
        param.setSort(port.toString());
        List<Map<String, Object>> portInfo = equipmentDao.slelectPortInfoList(param);
        if (portInfo == null) portInfo = new ArrayList<Map<String, Object>>();
        return portInfo;
    }

    /**
     * @Description： 查询十路设备指定端口信息的情况
     * @author： origin 创建时间：   2019年7月30日 下午3:04:20
     */
    @Override
    public List<Map<String, Object>> specifiedUsePort(Integer merid, String code) {
        Parameters param = new Parameters();
        param.setCode(code);
        param.setDealer(CommUtil.toString(merid));
        List<Map<String, Object>> portInfo = equipmentDao.slelectPortInfoList(param);
        portInfo = portInfo.size() == 0 ? new ArrayList<Map<String, Object>>() : portInfo;
        return portInfo;
    }

    /**
     * @Description： 添加指定端口使用对象  equip:设备号     port:端口号   member:会员号(id)
     * @author： origin 创建时间：   2019年7月30日 下午5:04:58
     */
    @Override
    public Map<String, Object> addSpecifiedPort(User user, String equip, Integer port, String member) {
        Map<String, Object> map = new HashMap<String, Object>();
        try {
            User tourist = userDao.selectUserById(CommUtil.toInteger(member));
            if (tourist == null) {
                map = CommonConfig.messg(401);
            } else if (tourist.getMerid() != user.getId()) {
                map = CommonConfig.messg(407);
            } else {
                Parameters param = new Parameters();
                param.setCode(equip);
                param.setDealer(CommUtil.toString(user.getId()));
                param.setSort(CommUtil.toString(port));
                List<Map<String, Object>> portInfo = equipmentDao.slelectPortInfoList(param);
                if (portInfo != null & portInfo.size() > 10) {
                    map = CommonConfig.messg(408);
                } else {
                    equipmentDao.insertPortdate(equip, user.getId(), port, CommUtil.toInteger(member));
                    param.setUid(Integer.parseInt(member));
                    Map<String, Object> mapinfo = equipmentDao.slelectPortInfo(param);
                    map = CommonConfig.messg(200);
                    map.put("data", mapinfo);
                }
            }
            return map;
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConfig.messg(403);
        }

    }

    /**
     * @Description： 指定记录对象id
     * @author： origin 创建时间：   2019年7月30日 下午5:05:02
     */
    @Override
    public Map<String, Object> deleteSpecifiedPort(String memberList) {
        try {
            CommUtil.jsonList(memberList);
            JSONArray listObject = JSONArray.parseArray(memberList);
            for (Object item : listObject) {
                equipmentDao.deletePortdate(CommUtil.toInteger(item));
            }
//			equipmentDao.deletePortdate( id);
            return CommonConfig.messg(200);
        } catch (Exception e) {
            e.printStackTrace();
            return CommonConfig.messg(403);
        }
    }

    @Override
    public List<Map<String, Object>> assignportinfo(Integer uid, String code, Integer merid) {
        Parameters param = new Parameters();
        param.setUid(uid);
        param.setCode(code);
        param.setDealer(merid.toString());
        //Map<String, Object>  portinfo = equipmentDao.slelectPortInfo(param);
        List<Map<String, Object>> portInfo = equipmentDao.slelectPortInfoList(param);
        return portInfo;

    }

    @Override
    public List<String> slelecToPortINfo(Integer merid, String code) {
        List<String> portinfo = equipmentDao.slelecToPortINfo(code, merid);
        return portinfo;
    }

    @Override
    public int everydayResetEquEarn() {
        return equipmentDao.everydayResetEquEarn();
    }

    /**
     * @Description： 根据小区id获取对应在该小区下的设备信息
     * @author： origin 创建时间：   2019年8月10日 下午5:25:31
     */
    @Override
    public PageUtils<List<Map<String, Object>>> selfAreaEquipInfo(Integer aid, Integer page, Integer numPerPage) {
        Integer getpage = CommUtil.toInteger(page);//页码
        Integer PerPage = CommUtil.toInteger(numPerPage);//条数

        PageUtils<List<Map<String, Object>>> pagedata = new PageUtils<>(getpage, PerPage);

        Parameters parame = new Parameters();
        parame.setSort(CommUtil.toString(aid));
        parame.setParamete("01");
        List<Map<String, Object>> areaequip = equipmentDao.selfAreaEquipInfo(parame);
        if (null == areaequip) areaequip = new ArrayList<Map<String, Object>>();


        pagedata.setTotalRows(areaequip.size());
        pagedata.setTotalPages();
        pagedata.setStart();
        pagedata.setEnd();
        parame.setStartnumber((page - 1) * numPerPage);
        parame.setPages(PerPage);
        List<Map<String, Object>> areaequiplim = equipmentDao.selfAreaEquipInfo(parame);
        if (null == areaequiplim) areaequiplim = new ArrayList<Map<String, Object>>();
        for (Map<String, Object> item : areaequiplim) {
            String areaname = CommUtil.trimToEmpty(item.get("areaname"));
            String remark = CommUtil.trimToEmpty(item.get("remark"));
            item.put("areaname", areaname);
            item.put("remark", remark);
        }
        pagedata.setListMap(areaequiplim);

        return pagedata;
    }

    @Override
    public PageUtils<List<Map<String, Object>>> selfAreaEquipMerInfo(Integer merid, Integer page, Integer numPerPage) {
        Integer getpage = CommUtil.toInteger(page);//页码
        Integer PerPage = CommUtil.toInteger(numPerPage);//条数

        PageUtils<List<Map<String, Object>>> pagedata = new PageUtils<>(getpage, PerPage);
        try {
            Parameters parame = new Parameters();
            parame.setDealer(CommUtil.toString(merid));
            parame.setParamete("01");
            List<Map<String, Object>> areaequip = equipmentDao.selfAreaEquipInfo(parame);
            if (null == areaequip) areaequip = new ArrayList<Map<String, Object>>();
            pagedata.setTotalRows(areaequip.size());
            pagedata.setTotalPages();
            pagedata.setStart();
            pagedata.setEnd();
            parame.setStartnumber((page - 1) * numPerPage);
            parame.setPages(PerPage);
            List<Map<String, Object>> areaequiplim = equipmentDao.selfAreaEquipInfo(parame);
            if (null == areaequiplim) areaequiplim = new ArrayList<Map<String, Object>>();
            for (Map<String, Object> item : areaequiplim) {
                String areaname = CommUtil.trimToEmpty(item.get("areaname"));
                String remark = CommUtil.trimToEmpty(item.get("remark"));
                item.put("areaname", areaname);
                item.put("remark", remark);
            }
            pagedata.setListMap(areaequiplim);
            return pagedata;
        } catch (Exception e) {
            return pagedata;
        }
    }

    /**
     * @return
     * @Description： 根据设备号查询设备信息与小区信息
     * @author： origin 创建时间：   2019年8月10日 上午11:23:15
     */
    @Override
    public Map<String, Object> selectEquipAreaInfo(String code) {
        Parameters parame = new Parameters();
        parame.setCode(code);
        Map<String, Object> areaequip = equipmentDao.selectEquipAreaInfo(parame);
        return areaequip;
    }

    /**
     * @Description： 搜索指定类型的设备信息
     * @author： origin 创建时间：   2019年8月23日 下午3:18:01
     */
    @Override
    public List<Map<String, Object>> selectAssignTypeEqui(Integer merid, String parame) {
        Parameters param = new Parameters();
        param.setDealer(CommUtil.toString(merid));
        String versiontype = CommUtil.gainVersionsType(parame);
        param.setStatement(versiontype);
        //param.setParamete(parame);
        List<Map<String, Object>> equip = equipmentDao.selfAreaEquipInfo(param);
        List<Map<String, Object>> listmap = new ArrayList<>();
        for (Map<String, Object> item : equip) {
            Map<String, Object> map = new HashMap<>();
            String code = CommUtil.toString(item.get("code"));
            String devicename = CommUtil.toString(item.get("remark"));
            String areaname = CommUtil.toString(item.get("areaname"));
            Integer codetempid = CommUtil.toInteger(item.get("tempid"));
            Integer areaidid = CommUtil.toInteger(item.get("aid"));
            map.put("code", code);
            map.put("devicename", devicename);
            map.put("areaname", areaname);
            map.put("tempid", codetempid);
            map.put("areaidid", areaidid);
            listmap.add(map);
        }
        return listmap;
    }

    /**
     * @Description： 解绑设备并记录解绑信息
     * @author： origin   2019年10月10日 下午3:40:45
     */
    @Override
    public Map<String, Object> unbindDevice(Integer operid, String devicenum) {
        UserEquipment userequi = userEquipmentDao.getUserEquipmentByCode(devicenum);
        Equipment equipment = equipmentDao.getEquipmentById(devicenum);
        if (userequi != null) {
            operid = CommUtil.toInteger(operid);
            Integer sort = 2;
            Integer type = 2;
            insertCodeoperatelog(devicenum, sort, type, userequi.getUserId(), operid, CommUtil.toString(userequi.getUserId()));
            userEquipmentDao.deleteUserEquipmentByEquipmentCode(devicenum);
//			codeCollectmoney(devicenum, userequi.getUserId(), equipment.getAid());//强制解绑时统计计算该设备的今日收益
            deviceIncomeDataGain(devicenum, userequi.getUserId(), equipment.getAid(), equipment.getHardversion());
        }

        equipment.setBindtype((byte) 0);
        equipment.setSeveral(0);
        equipment.setAid(0);
//		Integer temid = getDefaultPartemp( 0, equipment.getHardversion());
//		equipment.setTempid(temid);
        equipment.setTotalOnlineEarn(0.0);
        equipment.setNowOnlineEarn(0.0);
        equipment.setTotalCoinsEarn(0);
        equipment.setNowCoinsEarn(0);
        equipment.setRemark("");
        equipmentDao.updateEquipment(equipment);
        equipmentDao.updateEquipmentRemark(null, devicenum);
        return null;
    }

    /**
     * separate
     *
     * @Description： 查询获取设备信息
     * @author： origin
     */
    @Override
    public Object getDeviceData(HttpServletRequest request, Integer type) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
//			User user = CommonConfig.getAdminReq(request);
            Parameters parameters = new Parameters();
            int numPerPage = CommUtil.toInteger(maparam.get("numPerPage"));
            int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
            PageUtils<Parameters> page = new PageUtils<>(numPerPage, currentPage);
            parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
            parameters.setNickname(CommUtil.toString(maparam.get("nick")));
            parameters.setUsername(CommUtil.toString(maparam.get("realname")));
            parameters.setPhone(CommUtil.toString(maparam.get("phone")));
            parameters.setSort(CommUtil.toString(maparam.get("imei")));
            parameters.setState(CommUtil.toString(maparam.get("ccid")));
            parameters.setStatement(CommUtil.toString(maparam.get("hardversion")));
            parameters.setNumber(CommUtil.toString(maparam.get("softversion")));
            parameters.setOrder(CommUtil.toString(maparam.get("hardversionnum")));
            parameters.setType(CommUtil.toString(maparam.get("binding")));// -1 请选择 、0 未绑定、1 已绑定
            parameters.setStatus(CommUtil.toString(maparam.get("line")));//在线与不在线
            parameters.setSource(CommUtil.toString(maparam.get("aid")));
            parameters.setParamete(CommUtil.toString(maparam.get("areaname")));
//			parameters.setRemark(CommUtil.toString(maparam.get("signalsort")));

            String signalsort = CommUtil.toString(maparam.get("signalsort"));//信号强度
            String expiretime = CommUtil.toString(maparam.get("expiretime"));//到期时间
            StringBuffer remark = new StringBuffer();
            if ("1".equals(signalsort)) {
                remark.append(" e.csq  DESC ");
            } else if ("2".equals(signalsort)) {
                remark.append(" e.csq ASC ");
            }
            if (remark.length() == 0) {
                if ("1".equals(expiretime)) {
                    remark.append(" e.expiration_time DESC ");
                } else if ("2".equals(expiretime)) {
                    remark.append(" e.expiration_time ASC ");
                }
            } else {
                if ("1".equals(expiretime)) {
                    remark.append(", e.expiration_time DESC ");
                } else if ("2".equals(expiretime)) {
                    remark.append(", e.expiration_time ASC ");
                }
            }
            String remarks = null;
            if (remark.length() > 0) remarks = remark.toString();
            parameters.setRemark(remarks);
            Integer teststatus = CommUtil.toInteger(maparam.get("teststatus"));
            if (teststatus.equals(1)) {
                parameters.setType("1");
            } else if (teststatus.equals(2)) {
                parameters.setLevel("0,1,2,3,4,5,6,7,8,9");
                parameters.setType("0");
            } else if (teststatus.equals(3)) {
                parameters.setLevel("10");
            }
            List<Map<String, Object>> deviceData = CommUtil.isListMapEmpty(equipmentNewDao.selectEquList(parameters));

            Integer export = CommUtil.toInteger(maparam.get("export"));
            if (export.equals(1)) {
                datamap.put("deviceData", deviceData);
            } else {
                int totaldevice = deviceData.size();
                int online = 0;
                int binding = 0;
                for (Map<String, Object> iteam : deviceData) {
                    if (CommUtil.toInteger(iteam.get("state")).equals(1)) online += 1;
                    if (CommUtil.toInteger(iteam.get("bindtype")).equals(1)) binding += 1;
                }
                datamap.put("totaldevice", totaldevice);
                datamap.put("online", online);
                datamap.put("disonline", CommUtil.toInteger(totaldevice - online));
                datamap.put("binding", binding);
                datamap.put("disbinding", CommUtil.toInteger(totaldevice - binding));

                page.setTotalRows(deviceData.size());
                page.setTotalPages();
                page.setStart();
                page.setEnd();
                parameters.setPages(page.getNumPerPage());
                parameters.setStartnumber(page.getStartIndex());
                List<Map<String, Object>> deviceInfo = CommUtil.isListMapEmpty(equipmentNewDao.selectEquList(parameters));
                datamap.put("listdata", deviceInfo);
                datamap.put("totalRows", page.getTotalRows());
                datamap.put("totalPages", page.getTotalPages());
                datamap.put("currentPage", page.getCurrentPage());
            }
            CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
        return datamap;
    }

    @Override
    public Object exportDeviceData(HttpServletRequest request) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        // 获取参数
        Map<String, Object> mapParam = CommUtil.getRequestParam(request);
        if (StringUtils.isEmpty(mapParam.get("typeIndex"))) {
            dataMap.put("result", "异常错误");
            CommUtil.responseBuildInfo(301, "异常错误", dataMap);
        }
        User user = CommonConfig.getAdminReq(request);
        // 超级管理员才能导出数据
        if (user != null && user.getLevel() != 0) {
            dataMap.put("result", "无权操作");
            CommUtil.responseBuildInfo(301, "异常错误", dataMap);
        }
        if (user == null) {
            dataMap.put("result", "无权操作");
            CommUtil.responseBuildInfo(301, "异常错误", dataMap);
        }
        // 根据条件查询
        // Integer number = CommUtil.toInteger(mapParam.get("type"));
        // 参数类
        Parameters parameters = new Parameters();
        // 设备号
        parameters.setCode(CommUtil.toString(mapParam.get("devicenum")));
        // 所属人
        parameters.setNickname(CommUtil.toString(mapParam.get("nick")));
        // 手机号
        parameters.setPhone(CommUtil.toString(mapParam.get("phone")));
        // IMEI号
        parameters.setSort(CommUtil.toString(mapParam.get("imei")));
        // CCID
        parameters.setState(CommUtil.toString(mapParam.get("ccid")));
        // 小区名称
        parameters.setParamete(CommUtil.toString(mapParam.get("areaname")));
        // 硬件版本
        parameters.setStatement(CommUtil.toString(mapParam.get("hardversion")));
        // 软件版本
        parameters.setNumber(CommUtil.toString(mapParam.get("softversion")));
        // 模块版本
        parameters.setOrder(CommUtil.toString(mapParam.get("hardversionnum")));
        // 在线状态
        parameters.setStatus(CommUtil.toString(mapParam.get("line")));
        // 条数
        int numPerPage = 2000;
        // 页数
        int currentPage = CommUtil.toInteger(mapParam.get("typeIndex"));
        // 下标索引
        int startIndex = (currentPage * numPerPage);
        parameters.setPages(numPerPage);
        parameters.setStartnumber(startIndex);
        looger.info("条数===" + numPerPage);
        looger.info("索引===" + startIndex);
        // 分页查询符合条件的数据
        List<Map<String, Object>> deviceData = equipmentDao.exportDeviceData(parameters);
        return deviceData;
    }

    /**
     * separate
     *
     * @Description： 查询设备操作日志
     * @author： origin
     */
    @Override
    public Object inquireDeviceOperationData(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            int numPerPage = CommUtil.toInteger(maparam.get("numPerPage"));
            int currentPage = CommUtil.toInteger(maparam.get("currentPage"));
            PageUtils<Parameters> page = new PageUtils<>(numPerPage, currentPage);
            User user = CommonConfig.getAdminReq(request);
            Parameters parameters = new Parameters();
            Integer rank = CommUtil.toInteger(user.getLevel());
            //===========================================
            //前端传递代理商名下某一个商家的id
            Integer agentSelectmerid = CommUtil.toInteger(maparam.get("agentSelectmerid"));
            if (agentSelectmerid != null && !agentSelectmerid.equals(0)) {
                user = new User();
                user.setLevel(2);
                user.setId(agentSelectmerid);
            }
            //====================================================
            if (!rank.equals(0)) parameters.setUid(user.getId());//绑定id
            parameters.setCode(CommUtil.toString(maparam.get("devicenum")));
            parameters.setDealer(CommUtil.toString(maparam.get("dealernick")));//商户昵称
            parameters.setRealname(CommUtil.toString(maparam.get("realname")));//商户姓名
            parameters.setPhone(CommUtil.toString(maparam.get("phone")));//商户电话
            parameters.setNickname(CommUtil.toString(maparam.get("nickname")));//操作人昵称
            parameters.setUsername(CommUtil.toString(maparam.get("username")));//操作人名字

            Integer sort = CommUtil.toInteger(maparam.get("sort"));
            if (sort.equals(1)) {
                parameters.setSort("1");
                parameters.setType("1");
            } else if (sort.equals(2)) {
                parameters.setSort("1");
                parameters.setType("2");
            } else if (sort.equals(3)) {
                parameters.setSort("2");
                parameters.setType("1");
            } else if (sort.equals(4)) {
                parameters.setSort("2");
                parameters.setType("2");
            } else if (sort.equals(5)) {
                parameters.setSort("3");
                //parameters.setType("1");
            } else if (sort.equals(6)) {
                parameters.setSort("4");
            }
            parameters.setStartTime(CommUtil.toString(maparam.get("startTime")));
            parameters.setEndTime(CommUtil.toString(maparam.get("endTime")));

            List<Map<String, Object>> operate = codeoperatelogDao.selectoperatelog(parameters);
            page.setTotalRows(operate.size());
            page.setTotalPages();
            page.setStart();
            page.setEnd();
            parameters.setPages(page.getNumPerPage());
            parameters.setStartnumber(page.getStartIndex());
            List<Map<String, Object>> operationInfo = codeoperatelogDao.selectoperatelog(parameters);

            datamap.put("listdata", operationInfo);
            datamap.put("totalRows", page.getTotalRows());
            datamap.put("totalPages", page.getTotalPages());
            datamap.put("currentPage", page.getCurrentPage());
            CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
        return datamap;
    }

    /**
     * separate
     *
     * @Description： 重置设备测试次数
     * @author： origin
     */
    @Override
    public Object resetDeviceTestTime(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String code = CommUtil.toString(maparam.get("code"));
            Integer testTime = CommUtil.toInteger(maparam.get("testTime"));
            Equipment equipment = new Equipment();
            equipment.setCode(code);
            equipment.setSeveral(testTime);
            equipmentDao.updateEquipment(equipment);
            CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
        return datamap;
    }

    /**
     * separate
     *
     * @Description： 设置设备版本号信息
     * @author： origin
     */
    @Override
    public Object editDeviceHardversion(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String code = CommUtil.toString(maparam.get("code"));
            String hardversion = CommUtil.toString(maparam.get("hardversion"));

            Equipment equipment = new Equipment();
            equipment.setCode(code);
            equipment.setHardversion(hardversion);
            equipmentDao.updateEquipment(equipment);

            List<AllPortStatus> portStatusList = allPortStatusService.findPortStatusListByEquipmentnum(code, 20);
            System.out.println("portStatusList===" + portStatusList);
            if (portStatusList != null) {
                int size = portStatusList.size();
                if ("02".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 3, 2);
                } else if ("01".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 11, 10);
                } else if ("05".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 17, 16);
                } else if ("06".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 21, 20);
                } else {
                    editHardverAddPortStatus(size, code, 11, 10);
                }
            } else {
                int size = 0;
                if ("02".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 3, 2);
                } else if ("01".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 11, 10);
                } else if ("05".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 17, 16);
                } else if ("06".equals(hardversion)) {
                    editHardverAddPortStatus(size, code, 21, 20);
                } else {
                    editHardverAddPortStatus(size, code, 11, 10);
                }
            }
            if ("03".equals(hardversion)) {
                List<TemplateParent> templateList = templateDao.getParentTemplateListByMerchantid(0, 2);
                equipment.setTempid(templateList.get(0).getId());
            } else if ("04".equals(hardversion)) {
                List<TemplateParent> templateList = templateDao.getParentTemplateListByMerchantid(0, 1);
                equipment.setTempid(templateList.get(0).getId());
            } else if ("02".equals(hardversion)) {
                List<TemplateParent> templateList = templateDao.getParentTemplateListByMerchantid(0, 0);
                equipment.setTempid(templateList.get(0).getId());
            } else if ("08".equals(hardversion) || "09".equals(hardversion) || "10".equals(hardversion)) {
                List<TemplateParent> templateList = templateDao.getParentTemplateListByMerchantid(0, 6);
                equipment.setTempid(templateList.get(0).getId());
            } else {
                equipment.setTempid(0);
            }
            Equipment equipmentById = equipmentDao.getEquipmentById(code);
            equipmentById = equipmentById == null ? new Equipment() : equipmentById;
            UserEquipment userEquipment = userEquipmentDao.getUserEquipmentByCode(code);
            Integer merid = userEquipment == null ? 0 : CommUtil.toInteger(userEquipment.getUserId());
            User user = (User) request.getSession().getAttribute("admin");
            if (user != null) {
                insertCodeoperatelog(code, 3, 1, merid, user.getId(), equipmentById.getHardversion());
            } else {
                insertCodeoperatelog(code, 3, 1, merid, 0, equipmentById.getHardversion());
            }
            equipmentDao.updateEquipment(equipment);
            CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
        return datamap;
    }

    public void editHardverAddPortStatus(int size, String code, int maxtemp, int needsize) {
        if (size < needsize) {
            for (int i = size + 1; i < maxtemp; i++) {
                allPortStatusService.insertPortStatus(code, i);
            }
        }
    }

    /**
     * separate ORIGIN device
     *
     * @Description： 解绑设备
     * @author： origin
     */
    @Override
    public Object disbindingDevice(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            User operuser = (User) request.getSession().getAttribute("admin");
            operuser = operuser == null ? new User() : operuser;
            Integer operid = CommUtil.toInteger(operuser.getId());
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String devicenum = CommUtil.toString(maparam.get("devicenum"));

            UserEquipment userequi = userEquipmentDao.getUserEquipmentByCode(devicenum);
            Integer dealid = CommUtil.toInteger(userequi.getUserId());
            Equipment equipment = equipmentDao.getEquipmentById(devicenum);
            equipment = equipment == null ? new Equipment() : equipment;
            Integer areaid = CommUtil.toInteger(equipment.getAid());
            String hardversion = CommUtil.toString(equipment.getHardversion());
            if (userequi != null) {
                User admin = (User) request.getSession().getAttribute("admin");
                Integer opeid = admin == null ? 0 : admin.getId();
                insertCodeoperatelog(devicenum, 2, 2, dealid, opeid, operid + "");
                userEquipmentDao.deleteUserEquipmentByEquipmentCode(devicenum);
                datamap.put("code", devicenum);
//				codeCollectmoney(devicenum, dealid, areaid);//强制解绑时统计计算该设备的今日收益
                deviceIncomeDataGain(devicenum, dealid, areaid, hardversion);//强制解绑时统计计算该设备的今日收益
            }
            equipment.setBindtype((byte) 0);
            equipment.setSeveral(0);
            equipment.setAid(0);
            Integer temid = getDefaultPartemp(0, equipment.getHardversion());
            equipment.setTempid(temid);
            equipment.setTotalOnlineEarn(0.0);
            equipment.setNowOnlineEarn(0.0);
            equipment.setTotalCoinsEarn(0);
            equipment.setNowCoinsEarn(0);
            equipmentDao.updateEquipment(equipment);
            equipmentDao.updateEquipmentRemark(null, devicenum);

            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * separate
     *
     * @Description： 根据商户id和设备类型获取默认模板
     * @author： origin         
     */
    public Integer getDefaultPartemp(Integer dealid, String version) {
        Integer temid = 0;
        TemplateParent tempdata = new TemplateParent();
        Integer tempstatus = 0;
        //模板类型 0充电模板  1离线卡模板     2模拟投币模板（脉冲模板） 3钱包模板   4在线卡模板  5包月模板  6:v3模板
        if ("03".equals(version)) {
            tempstatus = 2;
        } else if ("04".equals(version)) {
            tempstatus = 1;
        } else if ("08".equals(version) || "09".equals(version) || "10".equals(version)) {
            tempstatus = 6;
        } else if ("在线卡".equals(version)) {
            tempstatus = 4;
        } else if ("钱包".equals(version)) {
            tempstatus = 3;
        } else if ("包月".equals(version)) {
            tempstatus = 5;
        }
        TemplateParent temp = new TemplateParent();
        temp.setMerchantid(dealid);
        temp.setCommon3("1");
        temp.setStatus(tempstatus);
        List<TemplateParent> temlist = templateDao.selectpartemp(temp);
        if (temlist.size() == 0) {
            temlist = templateDao.getParentTemplateListByMerchantidwolf(0, tempstatus);
        }
        if (version.equals("02") || version.equals("07")) {//大功率电轿款
            for (TemplateParent item : temlist) {
                Integer teid = CommUtil.toInteger(item.getId());
                if (teid.equals(1)) {
                    tempdata = item;
                    break;
                }
            }
        } else {//其他
            tempdata = temlist.get(0);
        }
        temid = tempdata.getId();
        return temid;
    }

    /**
     * separate
     *
     * @Description： 绑定设备
     * @author： origin
     */
    @Override
    public Object bindingDevice(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            User user = CommonConfig.getAdminReq(request);
            Integer opeid = user == null ? 0 : CommUtil.toInteger(user.getId());
//			User user = (User) request.getSession().getAttribute("admin");
            Integer userId = CommUtil.toInteger(maparam.get("dealid"));
            String code = CommUtil.toString(maparam.get("devicenum"));
            insertCodeoperatelog(code, 2, 1, userId, opeid, "0");
            UserEquipment userEquipment = new UserEquipment();
            userEquipment.setUserId(userId);
            userEquipment.setEquipmentCode(code);
            userEquipmentDao.addUserEquipment(userEquipment);
            Equipment equipment = equipmentDao.getEquipmentById(code);
            //第一次绑定生成到期时间
            if (equipment.getExpirationTime() == null) {
                //获取当前时间加一年，添加设备的到期时间
                Calendar cal = Calendar.getInstance();
                cal.setTime(new Date());
                cal.add(Calendar.YEAR, 1);
                equipment.setExpirationTime(cal.getTime());
            }
            equipment.setBindtype((byte) 1);
            Integer temid = getDefaultPartemp(userId, equipment.getHardversion());
            equipment.setTempid(temid);
            equipmentDao.updateEquipment(equipment);
            datamap.put("code", code);
            datamap.put("username", userDao.selectUserById(userId).getUsername());

            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * separate
     *
     * @Description： 查询商户名下同类型设备信息
     * @author： origin   2019年11月21日 下午3:37:04
     */
    @Override
    public Object inquireDeviceData(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String merid = CommUtil.toString(maparam.get("merid"));
            String type = CommUtil.toString(maparam.get("hwVerson"));
            Parameters param = new Parameters();
            param.setDealer(merid);
            param.setParamete(type);
            List<Map<String, Object>> equip = equipmentDao.selfAreaEquipInfo(param);
            List<Map<String, Object>> listmap = new ArrayList<>();
            for (Map<String, Object> item : equip) {
                Map<String, Object> map = new HashMap<>();
                String code = CommUtil.toString(item.get("code"));
                String devicename = CommUtil.toString(item.get("remark"));
                String areaname = CommUtil.toString(item.get("areaname"));
                map.put("code", code);
                map.put("devicename", devicename);
                map.put("areaname", areaname);
                listmap.add(map);
            }
            datamap.put("devicelist", listmap);
            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * separate
     *
     * @Description： 更改设备模板信息
     * @author： origin   2019年11月21日 下午3:45:12
     */
    @Override
    public Object updateDeviceTemplate(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String deviceList = CommUtil.toString(maparam.get("deviceList"));
            Integer tempid = CommUtil.toInteger(maparam.get("tempid"));
            List<String> list = CommUtil.jsonList(deviceList);
            for (Object item : list) {

                String device = CommUtil.toString(item);
                equipmentDao.updateTempidByEquipmentCode(device, tempid);
            }
            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * employ 选择	ORIGIN
     *
     * @Description： 设置(修改)设备名字（备注）
     * @author： origin
     */
    @Override
    public Object editDeviceName(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String code = CommUtil.toString(maparam.get("code"));
            String devicename = CommUtil.toString(maparam.get("name"));
            Equipment equipment = new Equipment();
            equipment.setCode(code);
            equipment.setRemark(devicename);
            equipmentDao.updateEquipment(equipment);
//			Equipment devicedata = equipmentDao.getEquipmentById(code);
//			devicedata = devicedata == null ? new Equipment() : devicedata;
//			datamap.put("resultdata", devicedata)
            CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
        return datamap;
    }

    /**
     * separate
     *
     * @Description：搜索商户名下同类型设备信息（并按小区排序）
     * @author： origin
     * @createTime：2019年12月31日上午10:28:42
     */
    @SuppressWarnings({"unchecked", "rawtypes"})
    @Override
    public Object searchDeviceData(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            String merid = CommUtil.toString(maparam.get("merid"));
            String type = CommUtil.toString(maparam.get("hwVerson"));
            String code = CommUtil.toString(maparam.get("code"));
            Equipment devicedata = equipmentDao.getEquipmentById(code);
            devicedata = devicedata == null ? new Equipment() : devicedata;
            Integer aid = CommUtil.toInteger(devicedata.getAid());
            Parameters param = new Parameters();
            param.setDealer(merid);
            param.setParamete(type);
            param.setRemark(" e.aid ASC ");
            List<Map<String, Object>> equip = equipmentDao.selfAreaEquipInfo(param);
            List<Map<String, Object>> listmap = new ArrayList<>();
            List<Map<String, Object>> listdata = new ArrayList<>();
            List<Map<String, Object>> listinfo = new ArrayList<>();
            for (Map<String, Object> item : equip) {
                Map<String, Object> map = new HashMap<>();
                String devicenum = CommUtil.toString(item.get("code"));
                String devicename = CommUtil.toString(item.get("remark"));
                String areaname = CommUtil.toString(item.get("areaname"));
                Integer deviaid = CommUtil.toInteger(item.get("aid"));
                map.put("code", devicenum);
                map.put("devicename", devicename);
                map.put("areaname", areaname);
                map.put("aid", deviaid);
                if (code.equals(devicenum)) {
                    listinfo.add(map);
                } else if (aid.equals(deviaid)) {
                    listmap.add(map);
                } else {
                    listdata.add(map);
                }
            }
            List list = new ArrayList();
            list.addAll(listinfo);
            list.addAll(listmap);
            list.addAll(listdata);
            datamap.put("devicelist", list);
            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * 设备到期提醒
     */
    @Override
    public void equipmetExpireRemind() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        String sendurl = CommonConfig.ZIZHUCHARGE + "/wxpay/equipmentExpireRemindLink?id=";
        Date today = StringUtil.getPastTime(0);
        Date expirationTime = null;
        //用户设备表中的用户id
        Parameters parameter = new Parameters();
        List<Map<String, Object>> list = userDao.selectValidMerId();
        for (int i = 0; i < list.size(); i++) {
            int equipmentNum = 0;
            int days = 0;
            StringBuffer listCode = new StringBuffer();
            Integer merId = (Integer) list.get(i).get("uId");
            String openId = (String) list.get(i).get("openId");
            parameter.setUid(merId);
            //商家的设备
            List<Map<String, Object>> listEquipmet = equipmentDao.equipmetExpireRemind(parameter);
            if (!listEquipmet.isEmpty()) {
                for (int j = 0; j < listEquipmet.size(); j++) {
                    String expiration = dateFormat.format(listEquipmet.get(j).get("expiration_time"));
                    try {
                        expirationTime = dateFormat.parse(expiration);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //设备到期时间小于15天
                    long from = today.getTime();
                    long to = expirationTime.getTime();
                    int day = (int) ((to - from) / (1000 * 60 * 60 * 24));
                    if (day <= 15) {
                        days = days + day;
                        equipmentNum++;
                        listCode.append(listEquipmet.get(j).get("equipment_code")).append(",");
                    }
                }
            }
            //根据openid给商家推送
            if (equipmentNum != 0 && openId != null && !"".equals(openId)) {
                listCode = listCode.replace(listCode.length() - 1, listCode.length(), "。");
                System.out.println("设备数:" + equipmentNum + "---" + "天数" + days + "--" + "剩余天数" + days / equipmentNum + "设备号" + listCode + "链接信息" + sendurl + merId);
                TempMsgUtil.expireRemind("您有设备即将到期,缴费步骤:1-点击下方查看详情,2-选择按设备/小区,3-选择到期的设备,4-点击去结算。", openId, equipmentNum, days / equipmentNum, sendurl + merId, "设备编号:" + listCode);
                System.out.println("发送成功");
            }
        }
    }

    @Override
    public List<Equipment> selectAreaEqulistOrderyByExpire(Integer aid) {
        List<Equipment> areaList = equipmentDao.selectAreaEqulistOrderyByExpire(aid);
        if (areaList != null) {
            return areaList;
        } else {
            return new ArrayList<Equipment>();
        }
    }

    //更新设备到期时间增加一年
    @Override
    public void updateExpirationTime(String code) {
        if (code != null && !"".equals(code)) {
            equipmentDao.updateExpirationTime(code);
        }
    }

    /**
     * 商家根据设备的编号互换设备的IEMI号
     */
    @Override
    @Transactional
    public Map<String, Object> merTranspositionImei(HttpServletRequest request) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //获取参数
        String code1 = request.getParameter("code1");
        String code2 = request.getParameter("code2");
        User user = (User) request.getSession().getAttribute("user");
        Integer merId = user.getId();
        if (user.getLevel() == 6) {
            merId = user.getMerid();
        }

        //准备记录变化的设备号和IMEI号
        List<String> remark = new ArrayList<String>();

        //判断参数
        if (code1 == null || "".equals(code1) || code1.length() != 6) {
            dataMap.put("msg", "设备参数错误");
            return dataMap;
        }
        if (code2 == null || "".equals(code2) || code2.length() != 6) {
            dataMap.put("msg", "被扫描设备号错误");
            return dataMap;
        }
        if (code1.equals(code2)) {
            dataMap.put("msg", "被扫描设备号重复");
            return dataMap;
        }
        if (merId != null) {
            // 判断设备号是否存在
            Equipment codeA = equipmentDao.getEquipmentById(code1);
            // 扫描的设备是未绑定的
            Equipment codeB = equipmentDao.getEquipmentById(code2);
            // 查询用户设备
            UserEquipment codeB1 = userEquipmentDao.getUserEquipmentByCode(code2);
            if (codeA == null || codeA.getCode() == null || "".equals(codeA.getCode())) {
                dataMap.put("msg", "商家的设备异常");
                return dataMap;
            } else if (codeB == null || codeB.getCode() == null || "".equals(codeB.getCode())) {
                dataMap.put("msg", "被扫描的设备异常");
                return dataMap;
            } else if (codeA.getImei() == null || "".equals(codeA.getImei())) {
                dataMap.put("msg", "商家设备的IMEI号错误");
                return dataMap;
            } else if (codeB.getImei() == null || "".equals(codeB.getImei())) {
                dataMap.put("msg", "被扫描设备的IMEI号错误");
                return dataMap;
                //设备属于本人或未绑定都可以互换IMEI
            } else if (codeB1 == null || codeB1.getUserId().equals(merId) || codeB.getBindtype() == 0) {
                //设备属于本人或未绑定都可以互换IMEI
                try {
                    //删除A和B设备的IMEI
                    System.out.println(codeA.getCode());
                    System.out.println(codeB.getCode());
                    equipmentDao.deleteEquipmentIMEIByCode(codeA.getCode());
                    equipmentDao.deleteEquipmentIMEIByCode(codeB.getCode());
                    //互换IMEI
                    Equipment equipment1 = new Equipment();
                    equipment1.setCode(code1);
                    equipment1.setImei(codeB.getImei());
                    if (codeA.getState() == 1) {
                        equipment1.setState((byte) 0);
                        equipment1.setCsq("0");
                    }
                    equipmentDao.updateEquipment(equipment1);
                    equipment1.setCode(code2);
                    equipment1.setImei(codeA.getImei());
                    if (codeB.getState() == 1) {
                        equipment1.setState((byte) 0);
                        equipment1.setCsq("0");
                    }
                    equipmentDao.updateEquipment(equipment1);
                    //插入操作日志
                    remark.add(code1);
                    remark.add(codeA.getImei());
                    remark.add(code2);
                    remark.add(codeB.getImei());
                    insertCodeoperatelog(code1, 5, 1, merId, 0, JSON.toJSONString(remark));
                    dataMap.put("msg", "设备IMEI互换成功");
                    if (codeA.getState() == 1) {
//						server.removeClient(code1);
                    }
                    if (codeB.getState() == 1) {
//						server.removeClient(code2);
                    }
                    return dataMap;
                } catch (Exception e) {
                    e.printStackTrace();
                    dataMap.put("msg", "设备IMEI互换失败");
                    return dataMap;
                }
            } else {
                dataMap.put("msg", "设备已被他人绑定");
                return dataMap;
            }
        } else {
            dataMap.put("msg", "商家信息错误");
            return dataMap;
        }
    }

    /**
     * 管理员互换设备的IMEI号
     */
    @Override
    @Transactional
    public Object transpositionImei(HttpServletRequest request) {
        Map<String, Object> dataMap = new HashMap<String, Object>();
        //获取参数
        String code1 = request.getParameter("code1");
        String code2 = request.getParameter("code2");
        //判断参数
        if (code1 == null || "".equals(code1) || code1.length() != 6) {
            dataMap.put("msg", "设备参数错误");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
        if (code2 == null || "".equals(code2) || code2.length() != 6) {
            dataMap.put("msg", "输入设备号错误");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
        if (code1.equals(code2)) {
            dataMap.put("msg", "输入设备号重复");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
        User user = CommonConfig.getAdminReq(request);
        System.out.println("开始更换IMEI号:" + user.getLevel());
        //超级管理员互换设备的IMEI号
        if (user != null && user.getLevel().equals(0)) {
            // 判断设备号是否存在
            Equipment codeA = equipmentDao.getEquipmentById(code1);
            // 扫描的设备是未绑定的
            Equipment codeB = equipmentDao.getEquipmentById(code2);
            // 查询用户设备
            UserEquipment codeA1 = userEquipmentDao.getUserEquipmentByCode(code1);
            //准备记录变化的设备号和IMEI号
            List<String> remark = new ArrayList<String>();
            if (codeA == null || codeA.getCode() == null || "".equals(codeA.getCode())) {
                dataMap.put("msg", "商家的设备异常");
                return CommUtil.responseBuildInfo(201, "失败", dataMap);
            } else if (codeB == null || codeB.getCode() == null || "".equals(codeB.getCode())) {
                dataMap.put("msg", "输入设备异常");
                return CommUtil.responseBuildInfo(201, "失败", dataMap);
            } else if (codeA.getImei() == null || "".equals(codeA.getImei())) {
                dataMap.put("msg", "商家设备的IMEI号错误");
                return CommUtil.responseBuildInfo(201, "失败", dataMap);
            } else if (codeB.getImei() == null || "".equals(codeB.getImei())) {
                dataMap.put("msg", "输入设备的IMEI号错误");
                return CommUtil.responseBuildInfo(201, "失败", dataMap);
            } else {
                try {
                    //删除A和B设备的IMEI
                    equipmentDao.deleteEquipmentIMEIByCode(codeA.getCode());
                    equipmentDao.deleteEquipmentIMEIByCode(codeB.getCode());
                    //互换IMEI
                    Equipment equipment1 = new Equipment();
                    equipment1.setCode(code1);
                    equipment1.setImei(codeB.getImei());
                    if (codeA.getState() == 1) {
                        equipment1.setState((byte) 0);
                        equipment1.setCsq("0");
                    }
                    equipmentDao.updateEquipment(equipment1);
                    equipment1.setCode(code2);
                    equipment1.setImei(codeA.getImei());
                    if (codeB.getState() == 1) {
                        equipment1.setState((byte) 0);
                        equipment1.setCsq("0");
                    }
                    equipmentDao.updateEquipment(equipment1);
                    //记录操作日志
                    remark.add(code1);
                    remark.add(codeA.getImei());
                    remark.add(code2);
                    remark.add(codeB.getImei());
                    if (codeA1 == null) {
                        insertCodeoperatelog(code1, 5, 1, 0, 0, JSON.toJSONString(remark));
                    } else {
                        insertCodeoperatelog(code1, 5, 1, codeA1.getUserId(), 0, JSON.toJSONString(remark));
                    }
                    dataMap.put("msg", "设备IMEI互换成功");
                    if (codeA.getState() == 1) {
//						server.removeClient(code1);
                    }
                    if (codeB.getState() == 1) {
//						server.removeClient(code2);
                    }
                    return CommUtil.responseBuildInfo(200, "成功", dataMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    dataMap.put("msg", "设备IMEI互换失败");
                    return CommUtil.responseBuildInfo(201, "失败", dataMap);
                }
            }
            // 普通管理员PC端互换设备的IMEI号
        } else if (user != null && user.getLevel().equals(2)) {
            // 判断设备号是否存在
            Equipment codeA = equipmentDao.getEquipmentById(code1);
            // 扫描的设备是未绑定的
            Equipment codeB = equipmentDao.getEquipmentById(code2);
            // 查询用户设备
            UserEquipment codeB1 = userEquipmentDao.getUserEquipmentByCode(code2);
            //准备记录变化的设备号和IMEI号
            List<String> remark = new ArrayList<String>();
            if (codeA == null || codeA.getCode() == null || "".equals(codeA.getCode())) {
                dataMap.put("msg", "商家的设备异常");
                return dataMap;
            } else if (codeB == null || codeB.getCode() == null || "".equals(codeB.getCode())) {
                dataMap.put("msg", "被扫描的设备异常");
                return dataMap;
            } else if (codeA.getImei() == null || "".equals(codeA.getImei())) {
                dataMap.put("msg", "商家设备的IMEI号错误");
                return dataMap;
            } else if (codeB.getImei() == null || "".equals(codeB.getImei())) {
                dataMap.put("msg", "被扫描设备的IMEI号错误");
                return dataMap;
                //设备属于本人或未绑定都可以互换IMEI
            } else if (codeB1 == null || codeB1.getUserId().equals(user.getId()) || codeB.getBindtype() == 0) {
                //设备属于本人或未绑定都可以互换IMEI
                try {
                    //删除A和B设备的IMEI
                    System.out.println(codeA.getCode());
                    System.out.println(codeB.getCode());
                    equipmentDao.deleteEquipmentIMEIByCode(codeA.getCode());
                    equipmentDao.deleteEquipmentIMEIByCode(codeB.getCode());
                    //互换IMEI
                    Equipment equipment1 = new Equipment();
                    equipment1.setCode(code1);
                    equipment1.setImei(codeB.getImei());
                    equipmentDao.updateEquipment(equipment1);
                    equipment1.setCode(code2);
                    equipment1.setImei(codeA.getImei());
                    equipmentDao.updateEquipment(equipment1);
                    //插入操作日志
                    remark.add(code1);
                    remark.add(codeA.getImei());
                    remark.add(code2);
                    remark.add(codeB.getImei());
                    insertCodeoperatelog(code1, 5, 1, user.getId(), 0, JSON.toJSONString(remark));
                    dataMap.put("msg", "设备IMEI互换成功");
                    return dataMap;
                } catch (Exception e) {
                    e.printStackTrace();
                    dataMap.put("msg", "设备IMEI互换失败");
                    return dataMap;
                }
            } else {
                dataMap.put("msg", "设备已被他人绑定");
                return dataMap;
            }
        } else {
            dataMap.put("msg", "无权限修改");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
    }

    /**
     * 超级管理员定制设备号
     */
    @Override
    public Object customizationCode(HttpServletRequest request) {
        //判断参数是否存在
        Map<String, Object> dataMap = new HashMap<String, Object>();
        String oldCode = request.getParameter("code1");
        String newCode = request.getParameter("code2");
        if (oldCode == null || "".equals(oldCode) || oldCode.length() != 6) {
            dataMap.put("msg", "设备号异常");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
        if (newCode == null || "".equals(newCode) || newCode.length() != 6) {
            dataMap.put("msg", "输入设备号异常");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
        //判断输入的设备号是否被占用
        UserEquipment codeA = userEquipmentDao.getUserEquipmentByCode(oldCode);
        UserEquipment codeB = userEquipmentDao.getUserEquipmentByCode(newCode);
        if (codeA != null || codeB != null) {
            dataMap.put("msg", "设备号正在被使用");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
        Equipment codeA1 = equipmentDao.getEquipmentById(oldCode);
        Equipment codeB1 = equipmentDao.getEquipmentById(newCode);
        //被定制的设备必须为新设备
        //存在设备表中不存在用户设备表中
        //未绑定的状态,到期时间为空,收益必须为0
        if (codeA1 != null && codeA1.getBindtype() == 0 && codeA1.getExpirationTime() == null && codeA1.getTotalOnlineEarn() == 0) {
            //新的设备号不能重复
            if (codeB1 == null) {
                //开始更换设备号
                List<String> remark = new ArrayList<String>();
                try {
                    equipmentDao.customizationCode(oldCode, newCode);
                    //插入记录
                    remark.add(newCode);
                    remark.add(oldCode);
                    insertCodeoperatelog(oldCode, 6, 1, 0, 0, JSON.toJSONString(remark));
                    dataMap.put("msg", "设备号修改成功");
                    return CommUtil.responseBuildInfo(200, "成功", dataMap);
                } catch (Exception e) {
                    e.printStackTrace();
                    dataMap.put("msg", "设备号修改失败");
                    return CommUtil.responseBuildInfo(201, "失败", dataMap);
                }
            } else {
                dataMap.put("msg", "设备号已经存在");
                return CommUtil.responseBuildInfo(201, "失败", dataMap);
            }
        } else {
            dataMap.put("msg", "该设备不属于新设备");
            return CommUtil.responseBuildInfo(201, "失败", dataMap);
        }
    }

    /**
     * @param request
     * @return
     * @Description：搜索商户名下设备信息（并按小区排序）
     * @author： origin
     * @createTime：2020年4月3日上午11:31:10
     */
    @Override
    public Object searchDealerDeviceData(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            Integer dealid = CommUtil.toInteger(maparam.get("merid"));

            Area area = new Area();
            area.setMerid(dealid);
            List<Area> areadata = areaDao.selectByArea(area);
            areadata = areadata == null ? new ArrayList<Area>() : areadata;

            Parameters param = new Parameters();
            param.setDealer(dealid.toString());
            param.setRemark(" e.aid ASC ");
            List<Map<String, Object>> equip = equipmentDao.selfAreaEquipInfo(param);

            List<Map<String, Object>> listdata = new ArrayList<>();
            for (Area item : areadata) {
                Integer areaid = CommUtil.toInteger(item.getId());
                String areaname = CommUtil.toString(item.getName());
                Map<String, Object> itemdata = new HashMap<>();

                String devquantity = equipmentDao.selectEquCountByAid(areaid);

                Parameters parame = new Parameters();
                parame.setDealer(dealid.toString());
                parame.setNumber(areaid.toString());
                Map<String, Object> clients = userDao.inquireClientsNum(parame);
                Integer clientsnum = CommUtil.toInteger(clients.get("clientsnum"));
                itemdata.put("areaid", areaid);
                itemdata.put("areaname", areaname);
                itemdata.put("quantity", devquantity);
                itemdata.put("clientsnum", clientsnum);
                listdata.add(itemdata);
            }

            Map<String, Object> itemdevice = new HashMap<>();
            itemdevice.put("areaid", 0);
            itemdevice.put("areaname", "未绑定小区");
            Integer numqu = 0;
            for (Map<String, Object> item : equip) {
                Integer devaid = CommUtil.toInteger(item.get("aid"));
                if (devaid.equals(0)) numqu += 1;
            }
            Parameters parame = new Parameters();
            parame.setDealer(dealid.toString());
            parame.setNumber("0");
            Map<String, Object> clients = userDao.inquireClientsNum(parame);
            Integer clientsnum = CommUtil.toInteger(clients.get("clientsnum"));
            itemdevice.put("quantity", numqu);
            itemdevice.put("clientsnum", clientsnum);
            listdata.add(itemdevice);
            // 查询微信子商户的配置信息
            User user = new User();
            user.setId(dealid);
            List<Map<String, Object>> subMerData = userDao.selectSubMerByMchid(user);
            datamap.put("subMerData", subMerData);
            datamap.put("listdata", listdata);
            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * separate
     *
     * @Description：设备转移（从A名下转移到B名下）
     * @author： origin
     * @createTime：2020年4月3日下午4:09:00
     */
    @Override
    public Object deviceDataTransfer(HttpServletRequest request) {
        Map<String, Object> datamap = new HashMap<String, Object>();
        try {
            Map<String, Object> maparam = CommUtil.getRequestParam(request);
            if (maparam.isEmpty()) {
                CommUtil.responseBuildInfo(102, "参数传递不正确或为空", datamap);
                return datamap;
            }
            User usersesson = (User) request.getSession().getAttribute("admin");
            Integer operid = CommUtil.toInteger(usersesson.getId());
            String jsondata = CommUtil.toString(maparam.get("param"));
            JSONObject json = JSONObject.fromObject(jsondata);

            Integer oridealid = CommUtil.toInteger(json.get("fromId"));//设备原来商户的id
            Integer nowdealid = CommUtil.toInteger(json.get("ToId"));//设备要转移到的商户的id
//			String arealist = CommUtil.toString(json.get("areaList"));//小区id的集合
            List<?> list = (List<?>) json.get("areaList");//小区id的集合

            for (int i = 0; i < list.size(); i++) {
                Integer areaid = CommUtil.toInteger(list.get(i));
                Parameters param = new Parameters();
                param.setDealer(oridealid.toString());
                param.setSort(areaid.toString());
                List<Map<String, Object>> equipmap = CommUtil.isListMapEmpty(equipmentDao.selectEquipmentParameter(param));
                for (Map<String, Object> item : equipmap) {
                    String devicenum = CommUtil.toString(item.get("code"));
                    Integer devuserid = CommUtil.toInteger(item.get("uid"));
                    if (!devuserid.equals(nowdealid)) {
                        Equipment devicedata = new Equipment();
                        devicedata.setCode(devicenum);
                        devicedata.setTempid(0);
                        devicedata.setAid(0);
                        devicedata.setRemark("");
//						devicedata.setRegistTime(new Date());
//						devicedata.set
                        equipmentDao.updateEquipment(devicedata);
                        userEquipmentDao.updateUserEquipment(nowdealid, devicenum);
                        String remark = "转移设备到： " + nowdealid;
                        insertCodeoperatelog(devicenum, 6, 1, oridealid, operid, remark);
                    }
                }
                Parameters parame = new Parameters();
                parame.setUid(oridealid);
                parame.setType(areaid.toString());
                List<Map<String, Object>> userlist = CommUtil.isListMapEmpty(userDao.selectmemberinfo(parame));
                for (Map<String, Object> item : userlist) {
//					Integer aid = CommUtil.toInteger(item.get("aid"));
                    Integer merid = CommUtil.toInteger(item.get("merid"));
                    Integer id = CommUtil.toInteger(item.get("id"));
                    User user = new User();
                    user.setId(id);
                    user.setMerid(nowdealid);
                    user.setAid(0);
                    userDao.updateUserById(user);
                    //userDao.bindUserBelongMerid(id, nowdealid);
                    operateRecordDao.insertoperate("转移用户", operid, id, 1, 0, "用户转移" + merid, null);
                }
            }
            return CommUtil.responseBuildInfo(200, "成功", datamap);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(301, "异常错误", datamap);
        }
    }

    /**
     * @param aid
     * @Description：查询小区下的设备
     * @author： origin2020年5月15日下午4:54:09
     */
    @Override
    public List<Map<String, Object>> inquireAreaDeaviceInfo(Integer aid) {
        try {
            Parameters param = new Parameters();
            param.setSort(aid.toString());
            List<Map<String, Object>> devicelist = CommUtil.isListMapEmpty(equipmentDao.selectEquipmentParameter(param));
            return devicelist;
        } catch (Exception e) {
            e.printStackTrace();
            return new ArrayList<Map<String, Object>>();
        }


    }

    @Override
    public int resetEquEarn(String code) {
        Equipment equipment = new Equipment();
        equipment.setCode(code);
        equipment.setTotalOnlineEarn(0.0);
        equipment.setNowOnlineEarn(0.0);
        equipment.setTotalCoinsEarn(0);
        equipment.setNowCoinsEarn(0);
        return equipmentDao.updateEquipment(equipment);
    }

    @Override
    public String unionScan(String equCode, String codeAndPort, Model model) {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
        // 银联应答码
        String respCode = request.getParameter("respCode");
        if (!"00".equals(respCode)) return "erroruser";
        // 获取设备参数
        String code = null;
        Integer port = null;
        // 扫描设备号
        if (equCode != null && equCode.length() == 6 && codeAndPort == null) {
            code = equCode;
            // 扫描设备端口号
        } else if (equCode == null && codeAndPort != null) {
            int length = codeAndPort.length();
            if (length == 7 || length == 8) {
                String val = codeAndPort.substring(6);
                int port1 = Integer.parseInt(val);
                if (length == 8 && port1 > 20) {
                    model.addAttribute("errorinfo", "A二维码有误");
                    return "chargeporterror";
                }
            } else {
                model.addAttribute("errorinfo", "B二维码有误");
                return "chargeporterror";
            }
            code = new StringBuffer(codeAndPort).substring(0, 6);
            port = Integer.parseInt(codeAndPort.substring(6));
        } else {
            model.addAttribute("errorinfo", "B二维码有误");
            return "chargeporterror";
        }
        // 获取用户授权码
        SortedMap<String, String> params = new TreeMap<>();
        params.put("service", "pay.unionpay.userid");
        params.put("sign_type", "MD5");
        params.put("mch_id", WeiXinConfigParam.UNIONPAYMERCHID);
        params.put("nonce_str", HttpRequest.getRandomStringByLength(30));
        params.put("user_auth_code", request.getParameter("userAuthCode"));
        params.put("app_up_identifier", "UnionPay/1.0 CloudPay");
        ;
        // 发送请求
        Map<String, String> data = SignUtils.sendPostToUnionpay(params);
        if ("0".equals(data.get("status")) && "0".equals(data.get("result_code"))) {
            String userId = data.get("user_id");
            if (userId == null) return "erroruser";
            //Equipment equipment = equipmentDao.getEquipmentById(code);
            //if(equipment == null) return "erroruser";
            // 直接查询商家和设备的信息
            // 设备绑定,模板,型号,小区ID,到期时间,设备名称
            // 用户手机号,ID,服务号
            Map<String, Object> map = userDao.selectUserByCode(code);
            if (map == null) return "erroruser";
            // 判断设备绑定状态
            Integer bindType = CommUtil.toInteger(map.get("bindtype"));
            model.addAttribute("code", code);
            looger.info("设备未绑定状态===" + (bindType == 0));
            if (bindType == 0) {
                model.addAttribute("status", 2001);
                return "/unionpay/errorpage";
            }
            // 获取设备小区信息
            Integer aId = CommUtil.toInteger(map.get("aid"));
            if (aId != 0) {
                Area area = areaDao.selectByIdArea(aId);
                model.addAttribute("areaname", area.getName());
            }
            String phone = CommUtil.toString(map.get("phonenum"));
            String servePhone = CommUtil.toString(map.get("servephone"));
            Integer merId = CommUtil.toInteger(map.get("id"));
            Integer tempId = CommUtil.toInteger(map.get("tempid"));
            String hardVersion = CommUtil.toString(map.get("hardversion"));
            Integer state = CommUtil.toInteger(map.get("state"));
            String remark = CommUtil.toString(map.get("remark"));
            Integer subMer = CommUtil.toInteger(map.get("submer"));
            // 获取主模板信息和子模板
            Map<String, Object> tempData = basicsService.inquireDirectTempData(tempId, merId, equCode, hardVersion);
            String temPhone = CommUtil.toString(tempData.get("common1"));
            phone = basicsService.getServephoneData(temPhone, null, servePhone, phone);
            String brandName = CommUtil.toString(tempData.get("remark"));
            model.addAttribute("phonenum", phone);
            model.addAttribute("brandname", brandName);
            // 判断设备是否在线
            looger.info("设备已离线===" + (state == 0));
            if (state == 0) {
                model.addAttribute("remark", remark);
                model.addAttribute("status", 2002);
                return "/unionpay/errorpage";
            }
            // 判断设备是否到期
            Date expirationTime = (Date) map.get("expirationtime");
            String format = dateFormat.format(expirationTime);
            looger.info("设备已到期===" + (expirationTime != null));
            looger.info("设备已到期===" + (new Date().after(expirationTime)));
            if (expirationTime != null && new Date().after(expirationTime)) {
                model.addAttribute("status", 2003);
                model.addAttribute("time", format);
                return "/unionpay/errorpage";
            }
            // 判断是否属于特约商户
            looger.info("是特约商户的设备=====" + (subMer == 1));
            if (subMer == 1) {
                model.addAttribute("status", 2004);
                return "/unionpay/errorpage";
            }
            // 判断设备是否支持支付宝(支持支付宝支付的设备也支持银联)
            Integer aliPay = CommUtil.toInteger(tempData.get("ifalipay"));
            looger.info("设备支持支付宝===" + (aliPay != 1));
            if (aliPay != 1) {
                model.addAttribute("status", 2005);
                return "/unionpay/errorpage";
            }
            // 选择设备的模板
            Integer nowTempId = CommUtil.toInteger(tempData.get("id"));
            List<TemplateSon> templateSons = templateSonDao.getSonTemplateLists(nowTempId);
            templateSons = templateSons == null ? new ArrayList<>() : templateSons;
            Map<String, Object> tempMap = TemplateService.tempDefaultObje(templateSons);
            model.addAttribute("defaultchoose", tempMap.get("defaultchoose"));
            model.addAttribute("defaultindex", tempMap.get("defaultindex"));
            List<Map<String, String>> portStatus = null;
            // 脉冲不获取端口信息
            if (!"03".equals(hardVersion)) {
                portStatus = new ArrayList<>();
                // 获取设备端口状态放进list
                Map<String, String> codeRedisMap = JedisUtils.hgetAll(code);
                if (codeRedisMap == null) {
                    looger.info("获取端口数据失败");
                    model.addAttribute("status", 2008);
                    return "/unionpay/errorpage";
                }
                if ("02".equals(hardVersion)) {
                    StringUtil.addPortStatus(2, codeRedisMap, portStatus);
                } else if ("01".equals(hardVersion)) {
                    StringUtil.addPortStatus(10, codeRedisMap, portStatus);
                } else if ("05".equals(hardVersion)) {
                    StringUtil.addPortStatus(16, codeRedisMap, portStatus);
                } else if ("06".equals(hardVersion)) {
                    StringUtil.addPortStatus(20, codeRedisMap, portStatus);
                } else {
                    StringUtil.addPortStatus(10, codeRedisMap, portStatus);
                }
                // 刷新端口状态
                Map<String, String> portMap = portStatus.get(0);
                String updateTimeStr = portMap.get("updateTime");
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                long time = 0;
                try {
                    time = sdf.parse(updateTimeStr).getTime();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                long currentTime = System.currentTimeMillis();
                if ((currentTime - time) > 300000) {
                    model.addAttribute("flag", true);
                } else {
                    model.addAttribute("flag", false);
                }
            }
            String chargeInfo = CommUtil.toString(tempData.get("chargeInfo"));
            model.addAttribute("nowtime", new Date().getTime());
            model.addAttribute("templatelist", templateSons);
            model.addAttribute("chargeInfo", chargeInfo);
            model.addAttribute("userid", userId);
            model.addAttribute("merUserId", merId);
            model.addAttribute("equname", remark);
            // 十路智慧款(扫描设备)
            if ("01".equals(hardVersion) || "00".equals(hardVersion)) {
                // 扫描端口
                if (port != null) {
                    model.addAttribute("portStatus", portStatus.get(port - 1).get("portStatus"));
                    model.addAttribute("port", port);
                    return "unionpay/chargeport1";
                }
                model.addAttribute("portStatus", portStatus);
                return "unionpay/chargeallport1";
                // 脉冲投币
            } else if ("03".equals(hardVersion)) {
//				SendMsgUtil.send_0x82(code);
                if (templateSons.size() >= 2) {
                    model.addAttribute("defaultTemp", templateSons.get(1).getId());
                } else {
                    model.addAttribute("defaultTemp", templateSons.get(0).getId());
                }
                model.addAttribute("phonenum", phone);
                return "unionpay/inCoins";
            }
        } else {
            return "erroruser";
        }
        return "/unionpay/errorpage";
    }

    @Override
    public String getLocationData() {
        String locationData = JedisUtils.get("locationData");
        if (!StringUtils.isEmpty(locationData)) return locationData;
        List<Map<String, Object>> data = equipmentDao.seleLocationData();
        List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
        for (int i = 0; i < data.size(); i++) {
            Map<String, Object> map = data.get(i);
            String name = (String) map.get("code");
            List<BigDecimal> list2 = new ArrayList<BigDecimal>();
            BigDecimal lon = (BigDecimal) map.get("lon");
            BigDecimal lat = (BigDecimal) map.get("lat");
            list2.add(lon);
            list2.add(lat);
            Map<String, Object> map2 = new HashMap<String, Object>();
            map2.put("name", name);
            map2.put("geoCoord", list2);
            list.add(map2);
        }
        String string = JSON.toJSONString(list);
        JedisUtils.setnum("locationData", string, 604800, 0);
        return string;
    }

    /**
     * @param merid:商户id[查询所有时，商户id为null]
     * @return
     * @method_name: inquireDeviceCount
     * @Description: 查询设备计数信息【 查询所有设备数量 的在线数量、领先数量、绑定数量、未绑定数量】
     * @Author: origin  创建时间:2020年9月3日 上午10:43:58
     * @common:
     */
    @Override
    public Map<String, Object> inquireDeviceCount(Integer merid) {
        try {
            Map<String, Object> result = CommUtil.isMapEmpty(equipmentDao.inquireDeviceCount(merid));
            Integer onbinding = CommUtil.toInteger(result.get("onbinding"));
            Integer disbinding = CommUtil.toInteger(result.get("disbinding"));
            Integer devisenum = CommUtil.toInteger(onbinding + disbinding);
            result.put("devicetotal", devisenum);
            return result;
        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    }

    /*-- *** 请暂时不要在*号范围内添加   thanks ***********************************************************   --*/

    /**
     * ORIGGIN  ANEW AFRESH
     *
     * @param devicenum:设备号
     * @Description：根据设备号查询设备信息与设备关联信息(如:设备绑定人员信息、设备所属小区信息)
     * @author： origin 2020年6月20日下午5:09:54
     * @comment:
     */
    @Override
    public Map<String, Object> getDeviceRelevanceInfo(String devicenum) {
        try {
            Map<String, Object> deviceReleInfo = equipmentDao.getDeviceRelevanceInfo(devicenum);
            deviceReleInfo = deviceReleInfo == null ? new HashMap<>() : deviceReleInfo;
            return deviceReleInfo;
//			//设备信息
//			String devicenum = CommUtil.toString(deviceinfo.get("code"));
//			String hardversion = CommUtil.toString(deviceinfo.get("hardversion"));
//			String devicename = CommUtil.toString(deviceinfo.get("remark"));
//			String devicecsq = CommUtil.toString(deviceinfo.get("csq"));
//			Integer bindtype = CommUtil.toInteger(deviceinfo.get("bindtype"));
//			Integer state = CommUtil.toInteger(deviceinfo.get("state"));
//			Integer several = CommUtil.toInteger(deviceinfo.get("several"));
//			Integer aid = CommUtil.toInteger(deviceinfo.get("aid"));
//			String expirationtime = CommUtil.toString(deviceinfo.get("expiration_time"));
//			Integer tempid = CommUtil.toInteger(deviceinfo.get("tempid"));
//			
//			//商户信息
//			Integer merid = CommUtil.toInteger(deviceinfo.get("merid"));
//			String dealnick = CommUtil.toString(deviceinfo.get("mernick"));
//			String realname = CommUtil.toString(deviceinfo.get("realname"));
//			String openid = CommUtil.toString(deviceinfo.get("openid"));
//			String merphone = CommUtil.toString(deviceinfo.get("merphone"));
//			String merservephone = CommUtil.toString(deviceinfo.get("merservephone"));
//			Integer payhint = CommUtil.toInteger(deviceinfo.get("payhint"));
//			Integer submer = CommUtil.toInteger(deviceinfo.get("submer"));
//			
//			//小区信息
//			String areaname = CommUtil.toString(deviceinfo.get("areaname"));
//			String areaaddress = CommUtil.toString(deviceinfo.get("areaaddress"));
//			Integer waltempid = CommUtil.toInteger(deviceinfo.get("waltempid"));
//			Integer onltempid = CommUtil.toInteger(deviceinfo.get("onltempid"));

        } catch (Exception e) {
            e.printStackTrace();
            return new HashMap<String, Object>();
        }
    }
    /*-- ****************************************************************************************  --*/

    /**
     * @param code
     * @param merid
     * @param opertype
     * @param type
     * @Description:
     * @Author: origin  创建时间:2020年9月12日 下午6:00:35
     * @common:
     */
    @Override
    public Map<String, Object> getWarnParamsInfo(String code, Integer merid, Integer opertype, Integer type) {
        Map<String, Object> resultdata = new HashMap<String, Object>();
        try {
//			if(type==null || type == 0){
//				equipmentDao.getWarnParamsInfo( code, merid, opertype);
//			} else {
//				 equipmentDao.getWarnParamsInfo( code, merid, opertype);
//			}

        } catch (Exception e) {
            // TODO: handle exception
        }
        return resultdata;

    }

    @Override
    public boolean selectDeviceExsit(String devicenum) {
        try {
            if (equipmentNewDao.selectDeviceExsit(devicenum) != null) {
                return true;
            }
            return false;
        } catch (Exception e) {
            return true;
        }
    }

    @Override
    public Map<String, Object> insertEquipmentNew(EquipmentNew equ) {
        try {
            equ.setCreateTime(new Date());
            equipmentNewDao.insertEquipmentNew(equ);
            List<AllPortStatus> allPortStatuses = allPortStatusDao.findPortStatusListByEquipmentnum(equ.getCode(), 10);
            if (allPortStatuses.size() > 0&&equ.getAid()!=null) {
            AllPortStatus allPortStatus = new AllPortStatus();
            allPortStatus.setAid(equ.getAid());
            allPortStatus.setEquipmentnum(equ.getCode());
            allPortStatusDao.updateAllPortStatus(allPortStatus);
            }
            return CommUtil.responseBuildInfo(200, "添加成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(201, "添加失败", null);
        }
    }

    @Override
    public Map<String, Object> updateEquipmentNew(EquipmentNew equ) {
        try {
            equipmentNewDao.updateEquipmentNew(equ);
            List<AllPortStatus> allPortStatuses = allPortStatusDao.findPortStatusListByEquipmentnum(equ.getCode(), 10);
            if (allPortStatuses.size() > 0&&equ.getAid()!=null) {
                AllPortStatus allPortStatus = new AllPortStatus();
                allPortStatus.setAid(equ.getAid());
                allPortStatus.setEquipmentnum(equ.getCode());
                allPortStatusDao.updateAllPortStatus(allPortStatus);
            }
            return CommUtil.responseBuildInfo(200, "修改成功", null);
        } catch (Exception e) {
            e.printStackTrace();
            return CommUtil.responseBuildInfo(201, "修改失败", null);
        }
    }

    @Override
    public Map<String, Object> delEquipmentNewById(String code) {
        try {
            equipmentNewDao.delEquipmentNewById(code);
            return CommUtil.responseBuildInfo(200, "删除成功", null);
        } catch (Exception e) {
            return CommUtil.responseBuildInfo(201, "删除失败", null);
        }
    }

    @Override
    public Map<String, Object> insertEquipmentNewData(String code, String hardversion, String softversion,
                                                      String subHardversion, String subSoftversion, Integer dcModeltype, Integer dcModelnum, Integer dcModelpower,
                                                      String location, BigDecimal lon, BigDecimal lat, String remark) {
        try {
            EquipmentNew equ = new EquipmentNew();
            equ.setCode(code);
            equ.setHardversion(hardversion);
            equ.setSoftversion(softversion);
            equ.setSubHardversion(subHardversion);
            equ.setSubSoftversion(subSoftversion);
            equ.setDcModeltype(dcModeltype);
            equ.setDcModelnum(dcModelnum);
            equ.setDcModelpower(dcModelpower);
            equ.setLocation(location);
            equ.setLon(lon);
            equ.setLat(lat);
            equ.setRemark(remark);
            equ.setCreateTime(new Date());
            equipmentNewDao.insertEquipmentNew(equ);
            return CommUtil.responseBuildInfo(200, "添加成功", null);
        } catch (Exception e) {
            return CommUtil.responseBuildInfo(201, "添加失败", null);
        }
    }

    @Override
    public Map<String, Object> updateEquipmentNewData(String code, String hardversion, String softversion,
                                                      String subHardversion, String subSoftversion, Integer dcModeltype, Integer dcModelnum, Integer dcModelpower,
                                                      String location, BigDecimal lon, BigDecimal lat, String remark) {
        try {
            EquipmentNew equ = new EquipmentNew();
            equ.setCode(code);
            equ.setHardversion(hardversion);
            equ.setSoftversion(softversion);
            equ.setSubHardversion(subHardversion);
            equ.setSubSoftversion(subSoftversion);
            equ.setDcModeltype(dcModeltype);
            equ.setDcModelnum(dcModelnum);
            equ.setDcModelpower(dcModelpower);
            equ.setLocation(location);
            equ.setLon(lon);
            equ.setLat(lat);
            equ.setRemark(remark);
            equ.setCreateTime(new Date());
            equipmentNewDao.insertEquipmentNew(equ);
            return CommUtil.responseBuildInfo(200, "修改成功", null);
        } catch (Exception e) {
            return CommUtil.responseBuildInfo(201, "修改失败", null);
        }
    }

}

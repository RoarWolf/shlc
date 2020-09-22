package com.hedong.hedongwx.utils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import org.influxdb.InfluxDB;
import org.influxdb.InfluxDBFactory;
import org.influxdb.dto.BatchPoints;
import org.influxdb.dto.Point;
import org.influxdb.dto.Query;
import org.influxdb.dto.QueryResult;

import com.hedong.hedongwx.entity.InfluxDbRow;

/**
 * 时序数据库 InfluxDB 连接
 * @author zz
 *
 */
public class InfluxDbUtil {

    // private String username;
    // private String password;
    // private String url;
    private String database = "db";
    private int retentionDay;
    private int replicationCount;
    private InfluxDB influxDB;
    private static String URL = "http://47.93.203.50:8086/";
    private static String USERNAME = "admin";
    private static String PASSWORD = "123456";
    // private static String dataBase = "real";
    public InfluxDbUtil() {
		super();
		// TODO Auto-generated constructor stub
	}

	public InfluxDbUtil(String username, String password, String url, String database, int retentionDay, int replicationCount) {
       // this.username = username;
       // this.password = password;
       // this.url = url;
        this.database = database;
        this.retentionDay = retentionDay;
        this.replicationCount = replicationCount;
    }

    /** 连接时序数据库；获得InfluxDB **/
   public void connection() {
        if (influxDB == null) {
            influxDB = InfluxDBFactory.connect(URL, USERNAME, PASSWORD);
        }
        
    }

    /**
     * 设置数据保存策略
     * defalut 策略名 /database 数据库名/ 30d 数据保存时限30天/ 1  副本个数为1/ 结尾DEFAULT 表示 设为默认的策略
     */
   public void createRetentionPolicy() {
        String command = String.format("CREATE RETENTION POLICY \"%s\" ON \"%s\" DURATION %s REPLICATION %s DEFAULT",
                "default", database, retentionDay + "d", replicationCount);
        this.query(command);
    }

    /**
     * 查询
     * @param command 查询语句
     * @return 查询结果
     */
   public QueryResult query(String command) {
        return influxDB.query(new Query(command, database));
    }

    /**
     * 插入
     */
    public void insert(InfluxDbRow influxDbRow) {
        if (influxDbRow == null) {
            return;
        }
        Point.Builder builder = Point.measurement(influxDbRow.getMeasurement());
        builder.tag(influxDbRow.getTags());
        builder.fields(influxDbRow.getFields());
        if (influxDbRow.getTimeSecond() != null) {
            builder.time(influxDbRow.getTimeSecond(), TimeUnit.SECONDS);
        }
        System.out.println("数据=="+builder.build());
        influxDB.write(database, "autogen", builder.build());
		influxDB.close();
    }
    
    /**
     * 插入设备的实时功率数据
     * @param chargeid 充电记录id
     * @param uid 用户的id
     * @param merid 商家的id
     * @param code 设备号
     * @param port 端口号
     * @param type 充电的类型
     * @param chargetime 创建的时间
     * @param surpluselec 剩余的电量
     * @param power 设备的功率
     * @param money 钱
     */
    public void insertRealData(Integer chargeid, Integer uid, Integer merid, String code, Integer port, Integer type,
			Integer chargetime, Integer surpluselec, Integer power, double money){
    	//InfluxDbUtil dbUtil = new InfluxDbUtil();
		//dbUtil.connection();
		Map<String, String> tags = new HashMap<String, String>();
		tags.put("surpluselec", surpluselec+"");//tags列创建索引
		tags.put("power", power+"");
		tags.put("type", type+"");
		tags.put("money", money+"");
		Map<String, Object> fields = new HashMap<String, Object>();
		fields.put("chargeid", chargeid);//fileds不创建索引
		fields.put("uid", uid);	//根据fileds条件查询
		fields.put("merid",merid);
		fields.put("code", code);
		fields.put("port", port);
		fields.put("chargetime", chargetime);
		InfluxDbRow dbRow = new InfluxDbRow();
		dbRow.setTimeSecond(System.currentTimeMillis()/1000);
		dbRow.setMeasurement("hd_realchargerecord");
		dbRow.setFields(fields);
		dbRow.setTags(tags);
		insert(dbRow);
    }
    /**
     * 删除
     * @param command 删除语句
     * @return 返回错误信息
     */
    public String deleteMeasurementData(String command) {
        QueryResult result = influxDB.query(new Query(command, database));
        return result.getError();
    }

    /**
     * 创建数据库
     * @param dbName 库名称
     */
    public void createDB(String dbName) {
        this.query("create database " + dbName);
    }

    /**
     * 删除数据库
     * @param dbName
     */
    public void deleteDB(String dbName) {
        this.query("drop database " + dbName);
    }

    public void close() {
        this.influxDB.close();
    }

    /**
     * 指导导入
     * @param influxDbRows 行记录
     */
    public void batchPointsImport(List<InfluxDbRow> influxDbRows) {
        if (influxDbRows == null || influxDbRows.size() == 0) {
            return;
        }
        BatchPoints batchPoints = BatchPoints.database(this.database).retentionPolicy("default").build();
        for (InfluxDbRow influxDbRow : influxDbRows) {
            if (influxDbRow.getTags().size() + influxDbRow.getFields().size() == 0) continue;
            Point.Builder builder = Point.measurement(influxDbRow.getMeasurement());
            builder.tag(influxDbRow.getTags());
            builder.fields(influxDbRow.getFields());
            if (influxDbRow.getTimeSecond() != null) {
                builder.time(influxDbRow.getTimeSecond(), TimeUnit.SECONDS);
            } else {
                builder.time(System.currentTimeMillis() / 1000, TimeUnit.SECONDS);
            }
            batchPoints.point(builder.build());
        }
        influxDB.write(batchPoints);
    }

}

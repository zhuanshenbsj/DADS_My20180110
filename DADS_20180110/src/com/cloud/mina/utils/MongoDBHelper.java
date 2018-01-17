package com.cloud.mina.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.UnknownHostException;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;

import net.sf.json.JSONObject;

import org.apache.commons.io.FileUtils;
import org.bson.types.ObjectId;

import com.mongodb.BasicDBObject;
import com.mongodb.DB;
import com.mongodb.DBCollection;
import com.mongodb.DBObject;
import com.mongodb.MongoClient;
import com.mongodb.MongoClientOptions;
import com.mongodb.MongoClientOptions.Builder;
import com.mongodb.ServerAddress;
import com.mongodb.WriteConcern;
import com.mongodb.WriteResult;
/**
 * 
 * 项目名称：DADS   
 * 类名称：MongoDBHelper   
 * 类描述：暂无 
 * 创建人：lw   
 * 创建时间：2014-5-13 下午03:43:08   
 * 修改人：lw   
 * 修改时间：2014-5-13 下午03:43:08   
 * 修改备注： 
 * @version
 */
public class MongoDBHelper {
	/*
	 * hold db object
	 */
	private static DB db;
	
	/*
	 * mongodb name
	 */
	private static String dbname = "cmcc";
	
	/**
	 * field:
	 * 
	 * phone,appType,dataType,collectDate,dataValue,sendFlag,receiveTime
	 * 
	 */
	private static DBCollection health_data = null;
	
	/**
	 * health_data's collection  name
	 */
	private static String collection_health_data = "C_HealthData";
	
	/**
	 * field:
	 * 
	 * phone,appType,sex,birth,email,name,deviceId,password,idcard
	 * 
	 */
	private static DBCollection patient_data = null;
	
	/**
	 * patient_data's collection  name
	 */
	private static String collection_patient_data = "C_PatientInfo";
	
	/*
	 * single instance
	 */
	private static MongoDBHelper uniqueInstance = null;
	
	/**
	 * mongodb's db config file path
	 */
	private static String propertiesPath = "Config/mongodb.properties";
	
	/**
	 * default constructer
	 */
	private MongoDBHelper(){
		
	}
	
	/**
	 * get single instance
	 * @return
	 */
	public static MongoDBHelper getInstance() {
		long start = System.currentTimeMillis();
		if (uniqueInstance == null) {
			Properties prop = new Properties();
			InputStream fis = null;
			try {
				fis =  MongoDBHelper.class.getClassLoader().getResourceAsStream(propertiesPath);
				prop.load(fis);
			} catch (FileNotFoundException e) {
				e.printStackTrace();
			} catch (IOException e) {
				e.printStackTrace();
			}
			uniqueInstance = new MongoDBHelper();
			MongoClient mongoClient;
			try {
				Builder bd = MongoClientOptions.builder();
				bd.connectionsPerHost(Integer.parseInt(prop.getProperty("mongo.connectionsPerHost")));
				bd.threadsAllowedToBlockForConnectionMultiplier(Integer.parseInt(prop.getProperty("mongo.threadsAllowedToBlockForConnectionMultiplier")));
				bd.connectTimeout(Integer.parseInt(prop.getProperty("mongo.connectTimeout")));
				bd.maxWaitTime(Integer.parseInt(prop.getProperty("mongo.maxWaitTime")));
				bd.socketKeepAlive(Boolean.parseBoolean(prop.getProperty("mongo.socketKeepAlive")));
				bd.socketTimeout(Integer.parseInt(prop.getProperty("mongo.socketTimeout")));
				MongoClientOptions mo = bd.build();
				mongoClient = new MongoClient(new ServerAddress(prop.getProperty("mongo.host"), Integer.parseInt(prop.getProperty("mongo.port"))),mo);
				mongoClient.setWriteConcern(WriteConcern.SAFE);
				db = mongoClient.getDB(dbname );
				init();
			} catch (UnknownHostException e) {
				e.printStackTrace();
			}
			System.out.println("getInstance cost :"+(System.currentTimeMillis()-start)+"ms");
		}
		return uniqueInstance;
	}
	
	/**
	 * initialize MongoDBHelper Instance
	 */
	private static void  init(){
		//config health data
		createHealthDataIndex();
		//config patient data
		createPatientDataIndex();
	}
	
	/**
	 * create healthdata collection index
	 */
	private static void createHealthDataIndex(){
		String[] index_and_order = {"phone,1","appType,1","dataType,1","measureTime,1"};
		health_data = db.getCollection(collection_health_data);
		createIndex(health_data, index_and_order);
	}
	
	/**
	 * create healthdata collection index
	 */
	private static void createPatientDataIndex(){
		String[] index_and_order = {"phone,1","appType,1"};
		patient_data = db.getCollection(collection_patient_data);
		createIndex(patient_data, index_and_order);
	}
	
	/**
	 * common create index method
	 * 
	 * @param collectionName
	 * 				the index to be created name
	 * 
	 * @param indexs 
	 * 				like {"phone,1","appType,1"} and 1 means asc, 0 means desc
	 * 
	 */
	private static void createIndex(DBCollection collection,String[] indexs){
		long start = System.currentTimeMillis();
		for (int i = 0; i < indexs.length; i++) {
			List<DBObject> index_list = collection.getIndexInfo();
			for (DBObject dbObject : index_list) {
				BasicDBObject bd = (BasicDBObject)dbObject;
				JSONObject jo = JSONObject.fromObject(bd.toString());
				JSONObject key = jo.getJSONObject("key");
				if(!key.containsKey(indexs[i])){
					 String[] ix = indexs[i].split(",");
					 collection.createIndex(new BasicDBObject(ix[0], Integer.parseInt(ix[1])));
				}
			}
		}
		System.out.println("create index cost :"+(System.currentTimeMillis()-start)+"ms");
	}
	/**
	 * save health data 
	 * 			
	 * @param bd
	 * 				you can save one or more BasicDBObject Object
	 * @return		
	 * 				effect lines in mongodb
	 * 				
	 */
	public int insertHealthData(BasicDBObject... bd){
		WriteResult wr = health_data.insert(bd);
		return wr.getN();
	}
	
	/**
	 * over load insertHealthData method
	 * 
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * @return
	 */
	public int insertHealthData(JSONObject... js){
		return insertDBCollectionData(health_data,js);
	}
	
	/**
	 * query needed send data by condition
	 * 
	 * @param appType
	 * 	
	 * @param sendFlag
	 * 
	 * @return List<DBObject>
	 * 
	 */
	public List<DBObject> queryHealthDataByApptypeAndSendFlag(String appType,String sendFlag){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("appType", appType);
		db_condition.put("sendFlag", sendFlag);
		DBObject db_filed = new BasicDBObject();
		db_filed.put("deviceId", false);
		db_filed.put("sendFlag", false);
		db_filed.put("receiveTime", false);
		return health_data.find(db_condition, db_filed).limit(1000).toArray();
	}
	
	/**
	 * update sendFlag 
	 * 
	 * @param ObjectId 
	 * 					object id like 53717e35efeac759455005a3
	 * 
	 * @param sendFlag
	 * 					update value
	 * 
	 * @return DBObject
	 * 
	 */
	public DBObject updateHealthDataSendFlagByObjectId(String ObjectId ,String sendFlag){
		DBObject query = new BasicDBObject();
		query.put("_id", new ObjectId(ObjectId));
		DBObject update = new BasicDBObject();
		update.put("sendFlag", sendFlag);
    	DBObject updateSetValue=new BasicDBObject("$set",update);
		return health_data.findAndModify(query, updateSetValue);
	}
	
	/**
	 * save health data 
	 * 			
	 * @param bd
	 * 				you can save one or more BasicDBObject Object
	 * @return		
	 * 				effect lines in mongodb
	 * 				
	 */
	public int insertPatientInfo(BasicDBObject... bd){
		WriteResult wr = patient_data.insert(bd);
		return wr.getN();
	}
	
	/**
	 * over load insertPatientData method
	 * 
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * @return
	 */
	public int insertPatientData(JSONObject... js){
		return insertDBCollectionData(patient_data,js);
	}
	
	/**
	 * query patient's appType and phone
	 * 
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * @return
	 */
	public DBObject queryPatientPhoneAndApptypeByDeviceId(String deviceId){
		DBObject db_condition = new BasicDBObject();
		db_condition.put("deviceId", deviceId);
		DBObject db_filed = new BasicDBObject();
//		phone,appType,sex,birth,email,name,deviceId,password,idcard
//		db_filed.put("deviceId", false);
//		db_filed.put("password", false);
//		db_filed.put("idcard", false);
//		db_filed.put("name", false);
//		db_filed.put("email", false);
//		db_filed.put("birth", false);
//		db_filed.put("sex", false);
		db_filed.put("appType", true);
		db_filed.put("phone", true);
		return patient_data.findOne(db_condition, db_filed);
	}
	
	/**
	 * common insert collection method
	 * 
	 * @param dbCollection
	 * 				
	 * 				DBCollection instance
	 * @param js
	 * 				support one or more JSONObject save in mongodb
	 * 
	 * @return
	 */
	@SuppressWarnings("unchecked")
	public int insertDBCollectionData(DBCollection dbCollection,JSONObject... js){
		BasicDBObject[] bds = new BasicDBObject[js.length];
		for (int i =0,n=js.length;i<n;i++) {
			BasicDBObject obj = new BasicDBObject();
			Iterator<String> joKeys = js[i].keys();  
			while(joKeys.hasNext()){  
				String key = joKeys.next();
				obj.put(key, js[i].get(key));  
			}
			bds[i]=obj;
		}
		WriteResult wr = dbCollection.insert(bds);
		return wr.getN();
	}
	
	/**
	 * save data in file with UTF-8 EnCodeing
	 * 
	 * @param fileName
	 * 					fileName and full path be required
	 * @param data
	 *  				JSON data 
	 * @return
	 */
	public static boolean backUpDataInfile(String fileName,String data){
		boolean re_bool = true;
		File file = new File(fileName);
		try {
			FileUtils.writeStringToFile(file, data, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			re_bool = false;
		}
		return re_bool;
	}
	
	/**
	 * read data from file 
	 * 
	 * @param fileName
	 * 					fileName and full path be required
	 * @param data
	 *  				JSON data with UTF-8 EnCodeing
	 * @return
	 */
	public static String readDataFromfile(String fileName){
		String re_str = "";
		File file = new File(fileName);
		try {
			FileUtils.readFileToString(file, "UTF-8");
		} catch (IOException e) {
			e.printStackTrace();
			re_str="error";
		}
		return re_str;
	}
}

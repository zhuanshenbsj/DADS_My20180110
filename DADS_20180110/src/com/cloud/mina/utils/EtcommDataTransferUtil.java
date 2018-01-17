/**
 * add by rencm on 2013-12-3下午06:15:45
 */
package com.cloud.mina.utils;

import java.util.Iterator;

import net.sf.json.JSONArray;
import net.sf.json.JSONObject;

/** 
 * 项目名称：EtcommGateWay   
 * 类名称：EtcommDataTransferUtil   
 * 类描述：暂无 
 * 创建人：rcm   
 * 创建时间：2013-12-3 下午06:15:45   
 * 修改人：rcm   
 * 修改时间：2013-12-3 下午06:15:45   
 * 修改备注： 
 * @version   
 */
public class EtcommDataTransferUtil {
	/**
	 * 将一分钟数据合并为五分钟，复杂json对象
	 * @param jo
	 * @return
	 * JSONObject
	 */
	@SuppressWarnings("unchecked")
	public static JSONObject oneToFive(JSONObject jo){
		JSONArray stepsArr = jo.getJSONObject("data").getJSONArray("datavalue");
		Iterator<String> iterator = null;
		for(int i=0;i<stepsArr.size();i++){
			iterator = stepsArr.getJSONObject(i).keySet().iterator();
			while(iterator.hasNext()){
				stepsArr.set(i, oneToFive(iterator.next(),stepsArr.getJSONObject(i)));
			}
		}
		return jo;
	}
	/**
	 * 将一分钟数据合并为五分钟 简单json对象
	 * @param key
	 * @param jo
	 * @return
	 * JSONObject
	 */
	public static JSONObject oneToFive(String key, JSONObject jo){
		String val = jo.getString(key);
		JSONArray ja = JSONArray.fromObject("["+val+"]");
		JSONArray newJa = new JSONArray();
		int sum = 0;
		for(int i=0;i<ja.size();i++){
			if((i+1)%5==0){//每5个数一组求和
				sum+=ja.getInt(i);
				newJa.add(sum);
				sum=0;
			}else{
				sum+=ja.getInt(i);
			}
		}
		String newVal = newJa.toString().replace("[", "").replace("]", "");
		jo.put(key, newVal);
		System.out.println("jo"+jo);
		return jo;
	}
	/**
	 * 将总步数转换为有效步数
	 * @param key
	 * @param jo
	 * @return
	 * JSONObject
	 */
	public static JSONObject toEffective(String key, JSONObject jo){
		JSONObject newJo = new JSONObject();
		String val = jo.getString(key);
		JSONArray ja = JSONArray.fromObject("["+val+"]");
		ja = CalculateEffectiveStep(ja);
		String newVal = ja.toString().replace("[", "").replace("]", "");
		key = key.replace("snp", "snyxp");
		newJo.put(key, newVal);
		System.out.println("newJo"+newJo);
		return newJo;
	}
	/**
	 * 有效步数计算算法
	 * @param jsonData
	 * @return
	 * JSONArray
	 */
	private static JSONArray CalculateEffectiveStep(JSONArray jsonData)
	{
		int tenmin_ef = 0;
		JSONArray efsteps=new JSONArray();
		JSONArray steps=jsonData;	

		for(int i=0;i<steps.size();i++)
		{
			int steperMin=steps.getInt(i);
			if(steperMin>60)
		    {//一分钟超过60步
				tenmin_ef+=1;
				if(tenmin_ef > 10)
			    {//超过10min，只加这一分钟的步数
					efsteps.add(steperMin);
				}
				else if(tenmin_ef==10)
				{//第10min有效
					if(i>=9)
					{
						efsteps.add(0);
						for(int j=9;j>=0;j--)
						{
							efsteps.set(i-j,steps.getInt(i-j));
						}
					}
					else
					{
						efsteps.add(0);
						for(int j=i;j>=0;j--)
						{
							efsteps.set(i-j,steps.getInt(i-j));
						}						
					}
				}
				else
				{//不足10min，什么都不用做
					efsteps.add(0);
				}
		     }
		     else
		      {//一分钟不到60步，所有标记清0
		        if(tenmin_ef>=10)
		        {
		        	efsteps.add(steperMin);
		        	tenmin_ef= 0;
		        }
		        else
		        {		        	
		        	efsteps.add(0); 
		        	tenmin_ef = 0;
		        }
		      }
		}
		return efsteps;
	}
	/**
	 * 测试
	 * @param args
	 * void
	 */
	public static void main(String[] args) {
		String detail = "{\"data\":{\"datatype\":\"STEPCOUNT2\",\"hour\":\"14\",\"datavalue\":[{\"snp5\":\"70,80,90,70,80,90,70,80,90,70,50,40,90,80,700,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,23808,5376,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\"},{\"knp5\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,768,768,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\"},{\"level2p5\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\"},{\"level3p5\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\"},{\"level4p5\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,10,4,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\"},{\"yuanp5\":\"0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,59136,43520,0,0,47650,27914,0,36352,0,42240,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0,0\"}]}}\"";
		JSONObject jo = JSONObject.fromObject(detail);
		jo.getJSONObject("data").put("datatype", "STEPCOUNT3");
//		JSONArray dvArr = jo.getJSONObject("data").getJSONArray("datavalue");
//		dvArr.set(0,toEffective("snp5",dvArr.getJSONObject(0)));
//		dvArr.set(0,oneToFive("snp5",dvArr.getJSONObject(0)));
//		System.out.println(dvArr.getJSONObject(0).keySet().iterator());
//		oneToFive(jo);
		System.out.println("jo="+jo);
	}
}

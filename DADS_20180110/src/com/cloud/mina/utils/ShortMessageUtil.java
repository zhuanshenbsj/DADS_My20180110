package com.cloud.mina.utils;
public class ShortMessageUtil {
	public static String sendMessage(String phoneCode,String shortMsg){
		if(phoneCode==null||phoneCode.equals("")) return "电话号码为空,未进行发送";
		if(shortMsg==null||shortMsg.equals("")) return "短信内容为空,未进行发送";
		if(shortMsg.length()>(70*30)) return "短信内容已经超过30条的量(2100个字符),未进行发送";
		try {
			/*Service service=new Service();				
			Call call= (Call)service.createCall();
			//特别注意，用Axis调用JAX开发的服务的时候，这里的参数不可带?wsdl 否则会报错；用Axis调用Axis开发的服务的时候可带可不带
			call.setTargetEndpointAddress(new java.net.URL("http://10.1.5.63:8080/CMRISMSWS/services/CMRISMSERVICE"));
			call.setOperationName(new QName("http://service.sms.ws.cmri","SendMessage"));
			Object result= (Object)call.invoke(new Object[]{phoneCode,shortMsg});*/
			return "短信正常发送,返回编号为：";//+result.toString() ;
            
        } catch (Exception e) {
            e.printStackTrace();   
            return "短信发送接口包异常,发送参数是"+phoneCode+" 内容是:"+shortMsg;
        }
	}
	
	public static void main(String[] args) {
		
		ShortMessageUtil.sendMessage("13701115948", "d4b2758da0205c1e0aa9512cd188002ad4b2758da0205c1e0aa9512cd188002ad4b2758da0205c1e0aa9512cd188002a");
		
		/*System.out.println("begin...");
		try {
				Service service=new Service();				
				Call call=null;
				call=(Call)service.createCall();
				
				call.setTargetEndpointAddress(new java.net.URL("http://10.1.5.63:8080/CMRISMSWS/services/CMRISMSERVICE"));
				call.setOperationName(new QName("http://service.sms.ws.cmri","SendMessage"));
				Object result= (Object)call.invoke(new Object[]{"13701115948","郭艺华测试http://www.baidu.com"});
				System.out.println(result);   
	            
	        } catch (Exception e) {   
	            e.printStackTrace();   
	        }   */
	}
	
	
	
	
	
}

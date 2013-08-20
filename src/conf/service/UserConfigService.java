package conf.service;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserConfigService {

	Properties configFile;
	
	private static UserConfigService instance = null;
	
	private UserConfigService(){
		configFile = new Properties();
	    try {
			configFile.load(new FileInputStream("etc/conf/user.properties"));
		} catch (IOException e) {
			e.printStackTrace();
		}
		init();
	}
	
	public static UserConfigService getInstance() {
		if (instance == null) {
			instance = new UserConfigService();
		}
		return instance;
	}
	
	private void init(){

	}
	
	public boolean getBool(String item){
		return Boolean.parseBoolean(configFile.getProperty(item));
	}
	
	public void setBool(String key, boolean value){
		configFile.setProperty(key, new Boolean(value).toString());
		try {
			configFile.store(new FileOutputStream("etc/conf/user.properties"), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getString(String item){
		return configFile.getProperty(item);
	}
	
	public void setString(String key, String value){
		configFile.setProperty(key, value);
		try {
			configFile.store(new FileOutputStream("etc/conf/user.properties"), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getInteger(String item){
		return Integer.parseInt(configFile.getProperty(item));
	}
	
	public void setInteger(String key, int value){
		configFile.setProperty(key, new Integer(value).toString());
		try {
			configFile.store(new FileOutputStream("etc/conf/user.properties"), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public float getFloat(String item){
		return Float.parseFloat(configFile.getProperty(item));
	}
	
	public void setFloat(String key, float value){
		configFile.setProperty(key, new Float(value).toString());
		try {
			configFile.store(new FileOutputStream("etc/conf/user.properties"), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

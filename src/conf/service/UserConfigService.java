package conf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserConfigService {

	Properties configuration;
	final String configPath = "etc/conf";
	final String configFileName = "user.properties";
	final String configFile = configPath + "/" + configFileName;
	
	private static UserConfigService instance = null;
	
	private UserConfigService(){
		configuration = new Properties();
		
	    try {	    	
	    	File folder = new File(configPath);
	    	File conf = new File(configFile);
	    	
	    	if (!folder.exists())
	    		folder.mkdir();
	    	
	    	if( !conf.exists() ) 
	    	    conf.createNewFile();
	    	
			FileOutputStream oFile = new FileOutputStream(conf, true);
	    	oFile.close();
	    	
			configuration.load(new FileInputStream(configFile));
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
		return Boolean.parseBoolean(configuration.getProperty(item));
	}
	
	public void setBool(String key, boolean value){
		configuration.setProperty(key, new Boolean(value).toString());
		try {
			configuration.store(new FileOutputStream(configFile), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public String getString(String item){
		return configuration.getProperty(item);
	}
	
	public void setString(String key, String value){
		configuration.setProperty(key, value);
		try {
			configuration.store(new FileOutputStream(configFile), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public int getInteger(String item){
		return Integer.parseInt(configuration.getProperty(item));
	}
	
	public void setInteger(String key, int value){
		configuration.setProperty(key, new Integer(value).toString());
		try {
			configuration.store(new FileOutputStream(configFile), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
	public float getFloat(String item){
		return Float.parseFloat(configuration.getProperty(item));
	}
	
	public void setFloat(String key, float value){
		configuration.setProperty(key, new Float(value).toString());
		try {
			configuration.store(new FileOutputStream(configFile), null);
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}
	
}

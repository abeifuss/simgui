package conf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserConfigService {

	private Properties _configuration;
	private final String _configPath = "etc/conf";
	private final String _configFileName = "user.properties";
	private final String _configFile = _configPath + "/" + _configFileName;
	
	private static UserConfigService instance = null;
	
	private UserConfigService(){
		_configuration = new Properties();
		
	    try {	    	
	    	File folder = new File(_configPath);
	    	File conf = new File(_configFile);
	    	
	    	if (!folder.exists())
	    		folder.mkdir();
	    	
	    	if( !conf.exists() ) 
	    	    conf.createNewFile();
	    	
			FileOutputStream oFile = new FileOutputStream(conf, true);
	    	oFile.close();
	    	
			_configuration.load(new FileInputStream(_configFile));
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
		return Boolean.parseBoolean(_configuration.getProperty(item));
	}
	
	public void setBool(String key, boolean value){
		_configuration.setProperty(key, new Boolean(value).toString());
		try {
			_configuration.store(new FileOutputStream(_configFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public String getString(String item){
		return _configuration.getProperty(item);
	}
	
	public void setString(String key, String value){
		_configuration.setProperty(key, value);
		try {
			_configuration.store(new FileOutputStream(_configFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public int getInteger(String item){
		return Integer.parseInt(_configuration.getProperty(item));
	}
	
	public void setInteger(String key, int value){
		_configuration.setProperty(key, new Integer(value).toString());
		try {
			_configuration.store(new FileOutputStream(_configFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	public float getFloat(String item){
		return Float.parseFloat(_configuration.getProperty(item));
	}
	
	public void setFloat(String key, float value){
		_configuration.setProperty(key, new Float(value).toString());
		try {
			_configuration.store(new FileOutputStream(_configFile), null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
}

package evaluation.simulator.conf.service;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Properties;

public class UserConfigService {

	private static UserConfigService instance = null;

	public static UserConfigService getInstance() {
		if (instance == null) {
			instance = new UserConfigService();
		}
		return instance;
	}

	private final String _configFile = this._configPath + "/"
			+ this._configFileName;
	private final String _configFileName = "user.properties";

	private final String _configPath = "etc/conf";

	private final Properties _configuration;

	private UserConfigService() {
		this._configuration = new Properties();

		try {
			File folder = new File(this._configPath);
			File conf = new File(this._configFile);

			if (!folder.exists()) {
				folder.mkdir();
			}

			if (!conf.exists()) {
				conf.createNewFile();
			}

			FileOutputStream oFile = new FileOutputStream(conf, true);
			oFile.close();

			this._configuration.load(new FileInputStream(this._configFile));
		} catch (IOException e) {
			e.printStackTrace();
		}
		this.init();
	}

	public boolean getBool(String item) {
		return Boolean.parseBoolean(this._configuration.getProperty(item));
	}

	public float getFloat(String item) {
		return Float.parseFloat(this._configuration.getProperty(item));
	}

	public int getInteger(String item) {
		return Integer.parseInt(this._configuration.getProperty(item));
	}

	public String getString(String item) {
		return this._configuration.getProperty(item);
	}

	private void init() {

	}

	public void setBool(String key, boolean value) {
		this._configuration.setProperty(key, new Boolean(value).toString());
		try {
			this._configuration.store(new FileOutputStream(this._configFile),
					null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setFloat(String key, float value) {
		this._configuration.setProperty(key, new Float(value).toString());
		try {
			this._configuration.store(new FileOutputStream(this._configFile),
					null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setInteger(String key, int value) {
		this._configuration.setProperty(key, new Integer(value).toString());
		try {
			this._configuration.store(new FileOutputStream(this._configFile),
					null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void setString(String key, String value) {
		this._configuration.setProperty(key, value);
		try {
			this._configuration.store(new FileOutputStream(this._configFile),
					null);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

}

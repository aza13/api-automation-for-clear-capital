package utils;

import org.apache.log4j.Logger;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * The ConfigManager class enables you to access
 * src/main/resources/ config.properties information.
 */
public class ConfigManager {

    private static Logger logger = Logger.getLogger(ConfigManager.class);
    private static ConfigManager manager;
    private static final Properties property = new Properties();
    private static InputStream inputStream;

    private ConfigManager() throws IOException{

        try {

            String propFileName = "config.properties";
            inputStream = ConfigManager.class.getClassLoader().getResourceAsStream(propFileName);
            if (inputStream != null){
                property.load(inputStream);
            }
            else {
                throw new FileNotFoundException("property file '" + propFileName + "' not found in the classpath");
            }
        }
        catch (Exception e) {
            logger.error("Failed to load configuration for API in:: ConfigManager" + e.getMessage());
        } finally {
            inputStream.close();
        }
    }

    public static ConfigManager getInstance(){
        if(manager == null){
            synchronized (ConfigManager.class){
                try {
                    manager = new ConfigManager();
                } catch (IOException e) {
                    e.printStackTrace();
                    logger.error("Failed to get configuration instance for API in:: getInstance" + e.getMessage());
                }
            }
        }
        return manager;
    }

    public String getString(String key){

        return System.getProperty(key, property.getProperty(key));
    }
}

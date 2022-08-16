package edu.SOV.Config;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class Config {
    public static final String DB_LOCAL_URL = "db.url";
    public static final String DB_LOCAL_LOGIN = "db.login";
    public static final String DB_LOCAL_PASSWORD = "db.password";

    private static Properties properties = new Properties();

    public static String getProperty(String string){
        if(properties.isEmpty()){
            try (InputStream is = Config.class.getClassLoader().getResourceAsStream("dao_test.properties")){
                properties.load(is);
            }catch (IOException e){
                e.printStackTrace();
            }

        }
        return properties.getProperty(string);
    }
}

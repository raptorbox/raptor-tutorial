package createnet.raptorbox.quickstart.Utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

import org.createnet.raptor.sdk.Raptor;

import createnet.raptorbox.quickstart.RaptorTutorial;

public class Raptorbox {
	
	static Raptor instance;
	static final String appProperties = "application.properties";
	
	static public Properties loadSettings() {

        Properties prop = new Properties();
        ClassLoader classLoader = RaptorTutorial.class.getClassLoader();
        InputStream in = classLoader.getResourceAsStream(appProperties);
        if (in == null) {
            throw new RuntimeException("Cannot find properties file");
        }
        try {
            prop.load(in);
            in.close();
        } catch (IOException e) {
            System.out.println("File not found or exception occured while loading file. Error " + e);
        }

		return prop;
	}

	static  public Raptor getRaptor() {
		Properties prop = loadSettings();
		if (instance == null) {
			String url = prop.getProperty("url");
            String username = prop.getProperty("username");
            String password = prop.getProperty("password");
            
			instance = new Raptor(url, username, password);
			instance.Auth().login();
		}
		return instance;
	}

}

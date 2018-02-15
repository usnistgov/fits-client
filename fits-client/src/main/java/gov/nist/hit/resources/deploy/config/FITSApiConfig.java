package gov.nist.hit.resources.deploy.config;

import java.io.IOException;
import java.util.Properties;
import org.springframework.beans.factory.annotation.Value;
import gov.nist.hit.resources.deploy.exception.InsupportedApiMethod;
import gov.nist.hit.resources.deploy.model.Action;


public class FITSApiConfig {

	public Properties env;
	
	@Value("${prefix}")
	private String prefix;

	public FITSApiConfig() throws IOException{
		env = new Properties();
		env.load(FITSApiConfig.class.getResourceAsStream("/api-config.properties"));
		this.prefix = env.getProperty("prefix");

	}
	
	public String urlFor(Action action) throws InsupportedApiMethod {
		if(env.containsKey(action.getCode())){
			String mapping = env.getProperty(action.getCode());
			return this.prefix + mapping;
		}
		else
			throw new InsupportedApiMethod();
	}

	public String login() {
		return prefix+env.getProperty("login");
	}

}

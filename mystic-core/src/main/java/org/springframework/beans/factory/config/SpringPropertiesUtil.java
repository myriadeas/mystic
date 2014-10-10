package org.springframework.beans.factory.config;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

import org.springframework.beans.BeansException;

public class SpringPropertiesUtil extends PropertyPlaceholderConfigurer {

    private static Map<String, String> propertiesMap;
    // Default as in PropertyPlaceholderConfigurer
    private int springSystemPropertiesMode = SYSTEM_PROPERTIES_MODE_FALLBACK;

    @Override
    public void setSystemPropertiesMode(int systemPropertiesMode) {
        super.setSystemPropertiesMode(systemPropertiesMode);
        springSystemPropertiesMode = systemPropertiesMode;
    }

    @Override
    protected void processProperties(ConfigurableListableBeanFactory beanFactory, Properties props) throws BeansException {
        super.processProperties(beanFactory, props);

        if (propertiesMap == null){
        	propertiesMap = new HashMap<String, String>();
        }
        
        for (Object key : props.keySet()) {
            String keyStr = key.toString();
            String valueStr = resolvePlaceholder(keyStr, props, springSystemPropertiesMode);
            propertiesMap.put(keyStr, valueStr);
        }
    }

    public static String getProperty(String name, String defaultValue) {
    	if (propertiesMap.containsKey(name)){
    		return propertiesMap.get(name).toString();
    	} else if (defaultValue != null) {
    		return defaultValue;
    	} else {
    		throw new PropertyNotFoundException(name);
    	}
    }
    
    public static String getProperty(String name) {
    	return getProperty(name, null);
    }
    
    

}

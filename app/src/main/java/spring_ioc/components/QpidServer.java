package spring_ioc.components;

import org.apache.qpid.server.SystemLauncher;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import java.net.URL;
import java.util.HashMap;
import java.util.Map;

@Configuration
@Profile("test")
public class QpidServer {

    private static final String INITIAL_CONFIGURATION = "qpid.json";

    @Bean
    public boolean brokerBean()
    {
        SystemLauncher launcher = new SystemLauncher();
        try
        {
            launcher.startup(createSystemConfig());
        }
        catch (Exception e)
        {
            launcher.shutdown();
        }
        return  false;
    }

    private Map<String, Object> createSystemConfig() {
        final Map<String, Object> attributes = new HashMap<>();
        final URL initialConfig = QpidServer.class.getClassLoader().getResource(INITIAL_CONFIGURATION);
        attributes.put("type", "Memory");
        attributes.put("initialConfigurationLocation", initialConfig.toExternalForm());
        attributes.put("startupLoggedToSystemOut", true);
        return attributes;
    }

}

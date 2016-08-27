package com.ychp.coding.freemarker;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactory;
import org.springframework.ui.freemarker.FreeMarkerConfigurationFactoryBean;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfig;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;
import org.springframework.web.servlet.view.freemarker.FreeMarkerViewResolver;

import javax.annotation.PostConstruct;
import javax.servlet.Servlet;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/7/30
 */
@Primary
@Configuration
@ConditionalOnClass({freemarker.template.Configuration.class, FreeMarkerConfigurationFactory.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@EnableConfigurationProperties({FreemarkerProperties.class})
public class FreeMarkerConfiguration{

    private static final Log logger = LogFactory.getLog(FreeMarkerConfiguration.class);
    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FreemarkerProperties properties;

    public FreeMarkerConfiguration() {
    }

    @PostConstruct
    public void checkTemplateLocationExists() {
        if(this.properties.isCheckTemplateLocation()) {
            TemplateLocation templatePathLocation = null;
            ArrayList locations = new ArrayList();
            String[] var3 = this.properties.getTemplateLoaderPath();
            int var4 = var3.length;

            for(int var5 = 0; var5 < var4; ++var5) {
                String templateLoaderPath = var3[var5];
                TemplateLocation location = new TemplateLocation(templateLoaderPath);
                locations.add(location);
                if(location.exists(this.applicationContext)) {
                    templatePathLocation = location;
                    break;
                }
            }

            if(templatePathLocation == null) {
                logger.warn("Cannot find template location(s): " + locations + " (please add some templates, " + "check your FreeMarker configuration, or set " + "spring.freemarker.checkTemplateLocation=false)");
            }
        }

    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    @ConditionalOnWebApplication
    public static class FreeMarkerWebConfiguration extends FreeMarkerConfiguration.FreemarkerConfiguration {
        public FreeMarkerWebConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean({FreeMarkerConfig.class})
        public FreeMarkerConfigurer freeMarkerConfigurer() {
            FreeMarkerConfigurer configurer = new FreeMarkerConfigurer();
            this.applyProperties(configurer);
            return configurer;
        }

        @Bean
        public freemarker.template.Configuration freeMarkerConfiguration(FreeMarkerConfig configurer) {
            return configurer.getConfiguration();
        }

        @Bean
        @ConditionalOnMissingBean(
                name = {"freeMarkerViewResolver"}
        )
        @ConditionalOnProperty(
                name = {"spring.freemarker.enabled"},
                matchIfMissing = true
        )
        public FreeMarkerViewResolver freeMarkerViewResolver() {
            FreeMarkerViewResolver resolver = new FreeMarkerViewResolver();
            this.properties.applyToViewResolver(resolver);
            return resolver;
        }
    }

    @Configuration
    @ConditionalOnNotWebApplication
    public static class FreeMarkerNonWebConfiguration extends FreeMarkerConfiguration.FreemarkerConfiguration {
        public FreeMarkerNonWebConfiguration() {
        }

        @Bean
        @ConditionalOnMissingBean
        public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
            FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
            this.applyProperties(freeMarkerFactoryBean);
            return freeMarkerFactoryBean;
        }
    }

    protected static class FreemarkerConfiguration {
        @Autowired
        protected FreemarkerProperties properties;

        protected FreemarkerConfiguration() {
        }

        protected void applyProperties(FreeMarkerConfigurationFactory factory) {
            factory.setTemplateLoaderPaths(this.properties.getTemplateLoaderPath());
            factory.setPreferFileSystemAccess(this.properties.isPreferFileSystemAccess());
            factory.setDefaultEncoding(this.properties.getCharsetName());
            Properties settings = new Properties();
            settings.putAll(this.properties.getSettings());
            factory.setFreemarkerSettings(settings);
            Map<String, Object> variables = new HashMap<String, Object>();
            variables.putAll(this.properties.getVariables());
            factory.setFreemarkerVariables(variables);
        }

    }
}

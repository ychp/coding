package com.ychp.coding.freemarker.configuration;

import com.ychp.coding.freemarker.properties.CustomerFreemarkerProperties;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.AutoConfigureAfter;
import org.springframework.boot.autoconfigure.condition.*;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerProperties;
import org.springframework.boot.autoconfigure.template.TemplateLocation;
import org.springframework.boot.autoconfigure.web.WebMvcAutoConfiguration;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@Slf4j
@Configuration
@ConditionalOnClass({freemarker.template.Configuration.class, FreeMarkerConfigurationFactory.class})
@AutoConfigureAfter({WebMvcAutoConfiguration.class})
@EnableConfigurationProperties({FreeMarkerProperties.class, CustomerFreemarkerProperties.class})
public class CustomerFreemarkerConfiguration {

    @Autowired
    private ApplicationContext applicationContext;

    @Autowired
    private FreeMarkerProperties properties;

    @PostConstruct
    public void checkTemplateLocationExists() {
        if(properties.isCheckTemplateLocation()) {
            TemplateLocation templatePathLocation = null;
            ArrayList<TemplateLocation> locations = new ArrayList<TemplateLocation>();
            String[] pathArr = properties.getTemplateLoaderPath();

            for (String templateLoaderPath : pathArr) {
                TemplateLocation location = new TemplateLocation(templateLoaderPath);
                locations.add(location);
                if (location.exists(applicationContext)) {
                    templatePathLocation = location;
                    break;
                }
            }

            if(templatePathLocation == null) {
                log.warn("Cannot find template location(s): " + locations + " (please add some templates, " + "check your FreeMarker configuration, or set " + "spring.freemarker.checkTemplateLocation=false)");
            }
        }

    }

    @Configuration
    @ConditionalOnClass({Servlet.class})
    @ConditionalOnWebApplication
    public static class FreeMarkerWebConfiguration extends CustomerFreemarkerConfiguration.FreemarkerConfiguration {

        @Bean
        @ConditionalOnMissingBean({FreeMarkerConfig.class})
        public FreeMarkerConfigurer freeMarkerConfigurer() {
            FreeMarkerConfigurer configurer = new CustomerFreeMarkerConfigurer();
            applyProperties(configurer);
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
            properties.applyToViewResolver(resolver);
            return resolver;
        }
    }

    @Configuration
    @ConditionalOnNotWebApplication
    public static class FreeMarkerNonWebConfiguration extends CustomerFreemarkerConfiguration.FreemarkerConfiguration {

        @Bean
        @ConditionalOnMissingBean
        public FreeMarkerConfigurationFactoryBean freeMarkerConfiguration() {
            FreeMarkerConfigurationFactoryBean freeMarkerFactoryBean = new FreeMarkerConfigurationFactoryBean();
            applyProperties(freeMarkerFactoryBean);
            return freeMarkerFactoryBean;
        }
    }

    protected static class FreemarkerConfiguration {

        @Autowired
        FreeMarkerProperties properties;

        @Autowired
        CustomerFreemarkerProperties customerFreemarkerProperties;

        protected void applyProperties(FreeMarkerConfigurationFactory factory) {
            factory.setTemplateLoaderPaths(properties.getTemplateLoaderPath());
            factory.setPreferFileSystemAccess(properties.isPreferFileSystemAccess());
            factory.setDefaultEncoding(properties.getCharsetName());
            Properties settings = new Properties();
            settings.putAll(properties.getSettings());
            factory.setFreemarkerSettings(settings);
            Map<String, Object> variables = new HashMap<>();
            variables.putAll(customerFreemarkerProperties.getVariables());
            factory.setFreemarkerVariables(variables);
        }

    }
}

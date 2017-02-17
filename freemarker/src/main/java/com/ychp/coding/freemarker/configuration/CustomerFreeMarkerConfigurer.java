package com.ychp.coding.freemarker.configuration;

import cn.org.rapid_framework.freemarker.directive.BlockDirective;
import cn.org.rapid_framework.freemarker.directive.ExtendsDirective;
import cn.org.rapid_framework.freemarker.directive.OverrideDirective;
import freemarker.template.TemplateException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.servlet.view.freemarker.FreeMarkerConfigurer;

import java.io.IOException;

/**
 * Desc:
 * Author: <a href="ychp@terminus.io">应程鹏</a>
 * Date: 16/12/13
 */
@Slf4j
public class CustomerFreeMarkerConfigurer extends FreeMarkerConfigurer {

    @Override
    public void afterPropertiesSet() throws IOException, TemplateException {
        log.debug("=====================>  shared variables ");
        super.afterPropertiesSet();
        this.getConfiguration().setSharedVariable("block", new BlockDirective());
        this.getConfiguration().setSharedVariable("override", new OverrideDirective());
        this.getConfiguration().setSharedVariable("extends", new ExtendsDirective());

        for(Object name : this.getConfiguration().getSharedVariableNames()){
            log.debug("==========>  {}", name.toString());
        }
        log.debug("=====================>  shared variables end ");
    }
}

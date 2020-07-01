package com.maple.starter.shiro;

import com.maple.starter.shiro.converter.String2ObjectGenericConverter;
import com.maple.starter.shiro.properties.ShiroProperties;
import lombok.AllArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.boot.context.properties.ConfigurationPropertiesBinding;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.convert.converter.GenericConverter;

/**
 * 配置解析转换器自动配置
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-29 16:24
 */
@Configuration
@AllArgsConstructor
@EnableConfigurationProperties(ShiroProperties.class)
public class GenericConverterAutoConfiguration implements InitializingBean {
    /**
     * 此配置用于将String 转化为Object
     */
    @Bean
    @ConfigurationPropertiesBinding
    public GenericConverter string2ObjectGenericConverter() {
        return new String2ObjectGenericConverter();
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        // init realms
//        shiroProperties.realms().forEach(item -> {
//            String name =
//                    Optional.ofNullable(item.getName()).orElse(item.getClass().getName() + Objects.toString(counter.incrementAndGet()));
//            genericApplicationContext.registerBean(name, Realm.class, () -> item);
//        });
        // 猜测与org.apache.shiro.spring.config.web.autoconfigure FilterRegistrationBean自动配置有关
        // 使用此方式自动注册之后会使过滤链顺序失效 凡带有 /**:[filter] 的无视所有规则拦截所有
        //        shiroProperties.getFilters().forEach((key,val) -> genericApplicationContext.registerBean(key,
        //        Filter.class, () -> val));
    }

    /**
     * @link {org.springframework.context.annotation.ImportBeanDefinitionRegistrar}
     * 使用此方式注册时无法取到shiroProperties
     */
    /*public void registerBeanDefinitions(AnnotationMetadata annotationMetadata, BeanDefinitionRegistry
            beanDefinitionRegistry) {
        // init realms
        shiroProperties.realms().forEach(item -> {
            String name =
                    Optional.ofNullable(item.getName()).orElse(item.getClass().getName() + Objects.toString
                            (counter.incrementAndGet()));
            beanDefinitionRegistry.registerBeanDefinition(name
                    , BeanDefinitionBuilder.genericBeanDefinition(Realm.class, () -> item).getBeanDefinition());
        });
    }*/
}

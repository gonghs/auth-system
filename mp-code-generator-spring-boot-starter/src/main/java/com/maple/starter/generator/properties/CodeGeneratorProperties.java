package com.maple.starter.generator.properties;

import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;
import lombok.Getter;
import lombok.Setter;
import lombok.experimental.Accessors;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;

/**
 * 配置类
 *
 * @author maple
 * @version 1.0
 * @since 2020-04-11 21:34
 */
@Data
@ConfigurationProperties("mp.tool.generator")
public class CodeGeneratorProperties {
    private String[] genTables;
    private String basePackage;
    private String modelName;
    private GlobalConfig globalConfig = new GlobalConfig();
    private StrategyConfig strategyConfig = new StrategyConfig();
    private PackageConfig packageConfig = new PackageConfig();
    private TemplateConfig templateConfig = new TemplateConfig();
    private EnumConfig enumConfig = new EnumConfig();

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class GlobalConfig extends com.baomidou.mybatisplus.generator.config.GlobalConfig {
        private String author = "maple";
        private String outputDir = System.getProperty("user.dir") + "/gen/";
        private boolean swagger2 = true;
        private boolean fileOverride = false;
        private String entityName = "%sDTO";
        private String mapperName = "%sMapper";
        private String xmlName = "%sMapper";
        private String serviceName = "%sService";
        private String serviceImplName = "%sServiceImpl";
        private String controllerName = "%sController";
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public class StrategyConfig extends com.baomidou.mybatisplus.generator.config.StrategyConfig {
        private boolean entityLombokModel = true;
        private String[] superEntityColumns = {"id", "desc", "modify_time", "create_time", "data_status"};
        private NamingStrategy naming = NamingStrategy.underline_to_camel;
        private NamingStrategy columnNaming = NamingStrategy.underline_to_camel;
        private String superEntityClass = "com.maple.dto.base.BaseDTO";
        private String superMapperClass = "com.baomidou.mybatisplus.core.mapper.BaseMapper";
        private String superServiceClass = "com.baomidou.mybatisplus.extension.service.IService";
        private String superServiceImplClass = "com.baomidou.mybatisplus.extension.service.impl.ServiceImpl";
        private String superControllerClass = "com.maple.controller.BaseController";

        @Override
        public String[] getInclude() {
            return genTables;
        }
    }


    @Getter
    @Setter
    @Accessors(chain = true)
    public class PackageConfig extends com.baomidou.mybatisplus.generator.config.PackageConfig {
        /**
         * dir和 package的区别在于 dir不会在生成时声明在文件头部包名处
         */
        private String entityDir = "";
        private String mapperDir = "";
        private String serviceDir = "";
        private String serviceImplDir = "";
        private String controllerDir = "";
        private String entity = "dto";
        private String mapper = "dao";
        private String service = "service";
        private String controller = "controller";

        @Override
        public String getEntity() {
            return joinModelName(entity, modelName);
        }

        @Override
        public String getMapper() {
            return joinModelName(mapper, modelName);
        }

        @Override
        public String getService() {
            return joinModelName(service, modelName);
        }

        @Override
        public String getController() {
            return joinModelName(controller, modelName);
        }

        @Override
        public String getServiceImpl() {
            return getService() + ".impl";
        }

        @Override
        public String getParent() {
            return basePackage;
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class TemplateConfig extends com.baomidou.mybatisplus.generator.config.TemplateConfig {
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class EnumConfig {
        /**
         * 枚举名称 允许定义一个%s或两个%s 两个时前一个是类名(大写驼峰)后一个是字段名(大写驼峰) 一个时只填入字段名
         */
        private String enumName = "%sEnum";
        /**
         * 枚举父类
         */
        private String superEnumClass = "";
        /**
         * 枚举模板路径
         */
        private String templatePath = "/templates/fieldEnum.java";
    }

    /**
     * 拼接 模块名
     *
     * @param name      名称
     * @param modelName 模块名
     * @return 拼接后的名称
     */
    private String joinModelName(String name, String modelName) {
        return StringUtils.isBlank(modelName) ? name : name + "." + modelName;
    }

}

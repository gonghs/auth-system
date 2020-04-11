package com.maple.starter.generator.properties;

import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import lombok.Data;
import lombok.Getter;
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

    @Getter
    public class GlobalConfig extends com.baomidou.mybatisplus.generator.config.GlobalConfig {
        private String author = "maple";
        private String outputDir = System.getProperty("user.dir") + "/server/src/main/java";
        private Boolean swagger2 = true;
        private Boolean fileOverride = false;
        private String entityName = "%sDTO";
        private String mapperName = "%sMapper";
        private String xmlName = "%sMapper";
        private String serviceName = "%sService";
        private String serviceImplName = "%sServiceImpl";
        private String controllerName = "%sController";
    }

    @Getter
    public class StrategyConfig extends com.baomidou.mybatisplus.generator.config.StrategyConfig {
        private Boolean entityLombokModel = true;
        private String[] superEntityColumns = {"id", "desc", "modify_time", "create_time", "data_status"};
        private NamingStrategy namingStrategy = NamingStrategy.underline_to_camel;
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
    public class PackageConfig extends com.baomidou.mybatisplus.generator.config.PackageConfig {
        private String entity = "dto";
        private String mapper = "dao";
        private String service = "service";
        private String controller = "dto";

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

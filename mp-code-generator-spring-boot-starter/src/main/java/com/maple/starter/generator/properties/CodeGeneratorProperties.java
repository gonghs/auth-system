package com.maple.starter.generator.properties;

import cn.hutool.core.collection.CollUtil;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.generator.config.rules.NamingStrategy;
import com.maple.starter.generator.constant.PathStructConst;
import lombok.*;
import lombok.experimental.Accessors;
import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.List;
import java.util.Map;

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
    /**
     * 定义组织目录的方式(即文件的目录结构) 以entity为例默认为 entityDir(dir) + basePackage(pkg) + entity(subPkg) + modelName(dir)
     */
    private String pathStruct = PathStructConst.DEFAULT;
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
        private String serviceImpl = "service";
        private String controller = "controller";
        /**
         * 由于按默认的目录组织方式 可能无法将serviceImpl生成在service同目录的impl文件夹下(因为model在末尾 因此增加这个配置用于覆盖默认配置
         * 全局参数和pathStruct一致{@link #pathStruct} 并增加global参数用于直接获取pathStruct值
         */
        private Map<String, String> customPath =
                MapUtil.builder("serviceImpl", PathStructConst.GLOBAL + "/impl").build();
        @Override
        public String getParent() {
            return basePackage;
        }
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class TemplateConfig extends com.baomidou.mybatisplus.generator.config.TemplateConfig {
        private String xml = null;
    }

    @Getter
    @Setter
    @Accessors(chain = true)
    public static class EnumConfig {
        /**
         * 是否启用枚举生成
         */
        private boolean enabled = false;
        /**
         * 是否启用lombok模式
         */
        private boolean lombokModel = true;
        /**
         * 生成目录名
         */
        private String enumDir = "";
        /**
         * 包名
         */
        private String enumPkgName = "enumeration";
        /**
         * 枚举名称 %s为字段名
         */
        private String enumName = "%sEnum";
        /**
         * 枚举父类
         */
        private String implementInterface = "com.maple.BaseEnum";
        /**
         * 枚举模板路径
         */
        private String templatePath = "/templates/fieldEnum.java";
        /**
         * 反序列化配置
         * fastJson在类上追加 @JsonDeserialize(using = JacksonEnumSerializer.class)
         * jackson在类上追加 @JSONType(deserializer = ${deserializerClassName}.class)
         */
        private DeserializerTypeEnum deserializerType = DeserializerTypeEnum.FAST_JSON;
        /**
         * 反序列化类
         */
        private String deserializerClass = "com.maple.EnumDeserializer";
        /**
         * 字段列表配置 TODO
         */
        private List<Field> fields = CollUtil.newArrayList(new Field("java.lang.Integer", "value")
                , new Field("java.lang.String", "desc"));

        @AllArgsConstructor
        @NoArgsConstructor
        public static class Field {
            /**
             * 字段类型
             */
            private String type;
            /**
             * 字段名
             */
            private String name;
        }

        public enum DeserializerTypeEnum {
            FAST_JSON, JACKSON,
        }
    }

}

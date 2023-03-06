package cn.shure.framework.core.extension;

import org.springframework.beans.factory.config.YamlPropertiesFactoryBean;
import org.springframework.core.env.PropertiesPropertySource;
import org.springframework.core.env.PropertySource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.core.io.support.PropertySourceFactory;

import java.util.Objects;
import java.util.Properties;

/**
 * @Description YamlSourceFactory
 * @Author 下课菌
 * @Date 2023/2/14
 */
public class YamlPropertySourceFactory implements PropertySourceFactory {

    /**
     * @param name：@PropertySource 注解 name 的值
     * @param encodedResource：资源
     */
    @Override
    public PropertySource<?> createPropertySource(String name, EncodedResource encodedResource) {
        YamlPropertiesFactoryBean factory = new YamlPropertiesFactoryBean();
        factory.setResources(encodedResource.getResource());
        Properties properties = factory.getObject();
        assert properties != null;
        return new PropertiesPropertySource(Objects.requireNonNull(encodedResource.getResource().getFilename()), properties);
    }

}

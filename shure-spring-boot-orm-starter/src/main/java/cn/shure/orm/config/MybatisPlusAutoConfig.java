package cn.shure.orm.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.autoconfigure.MybatisPlusAutoConfiguration;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.BlockAttackInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.OptimisticLockerInnerInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnMissingBean;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

/**
 * @Description MybatisPlusAutoConfig
 * @Author 下课菌
 * @Date 2023/3/6
 */
@Configuration
@ConditionalOnBean(MybatisPlusAutoConfiguration.class)
public class MybatisPlusAutoConfig {

    @Bean
    @ConditionalOnMissingBean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new BlockAttackInnerInterceptor());
        // 防全表更新
        interceptor.addInnerInterceptor(new OptimisticLockerInnerInterceptor());
        // 乐观锁插件
        PaginationInnerInterceptor paginationInnerInterceptor = new PaginationInnerInterceptor(DbType.MYSQL);
        paginationInnerInterceptor.setMaxLimit(20L);
        paginationInnerInterceptor.setOptimizeJoin(false);
        interceptor.addInnerInterceptor(paginationInnerInterceptor);
        // 分页插件
        return interceptor;
    }

}

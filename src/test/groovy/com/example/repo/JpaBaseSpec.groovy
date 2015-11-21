package com.example.repo

import com.example.BaseSpec
import org.springframework.boot.test.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ContextConfiguration

/**
 * @author zzhao
 */
@ContextConfiguration(
        classes = JpaTestConfig.class,
        initializers = ConfigFileApplicationContextInitializer.class
)
class JpaBaseSpec extends BaseSpec {
}

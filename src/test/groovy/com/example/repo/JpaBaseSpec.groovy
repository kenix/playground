package com.example.repo

import com.example.BaseSpec
import org.springframework.boot.test.context.ConfigFileApplicationContextInitializer
import org.springframework.test.context.ContextConfiguration

/**
 * @author zzhao
 */
@ContextConfiguration(
        classes = JpaTestConfig.class,
        initializers = ConfigFileApplicationContextInitializer
)
abstract class JpaBaseSpec extends BaseSpec {
}

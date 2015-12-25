package com.example.web

import com.example.BaseSpec
import com.example.DemoApplication
import org.springframework.boot.test.WebIntegrationTest
import org.springframework.test.context.ContextConfiguration

/**
 * @author zzhao
 */
@ContextConfiguration(
        classes = DemoApplication.class
)
@WebIntegrationTest(randomPort = true)
abstract class WebIntegrationBaseSpec extends BaseSpec {
}

package com.example.web

import com.example.BaseSpec
import com.example.DemoApplication
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.test.context.ContextConfiguration

/**
 * @author zzhao
 */
@ContextConfiguration(
        classes = DemoApplication.class
)
@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
abstract class WebIntegrationBaseSpec extends BaseSpec {
}

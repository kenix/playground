package com.example

import org.springframework.test.context.ActiveProfiles
import spock.lang.Specification

/**
 * @author zzhao
 */
@ActiveProfiles(['test'])
abstract class BaseSpec extends Specification {
}

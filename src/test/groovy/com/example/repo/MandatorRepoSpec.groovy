package com.example.repo

import com.example.domain.Mandator
import com.example.domain.template.Template
import com.example.repo.template.TemplateRepo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Shared
import spock.lang.Stepwise

/**
 * @author zzhao
 */
@Slf4j
@Stepwise
class MandatorRepoSpec extends JpaBaseSpec {

    @Autowired
    MandatorRepo mandatorRepo;

    @Shared
    String mandatorZone = 'abc'

    @Shared
    String mandatorName = 'abc GmbH'

    @Autowired
    TemplateRepo templateRepo

    def "mandator abc"() {
        given:
        def mandator = new Mandator(mandatorZone, mandatorName)

        when:
        this.mandatorRepo.save(mandator);
        then:
        mandator.id

        def abc = this.mandatorRepo.findByZone(mandatorZone)
        then:
        abc
        abc.id == mandator.id
        abc.zone == mandatorZone
        abc.name == mandatorName

        when:
        this.mandatorRepo.save(new Mandator(mandatorZone, 'def GmbH'))
        then:
        def e = thrown(DataIntegrityViolationException)
        log.info("<mandator abc> {}", e.message);
    }

    def "abc,temp_a"() {
        given:
        def mandator = this.mandatorRepo.findByZone(mandatorZone)

        when:
        def tempName = 'Template A'
        def tempA = new Template(mandator, tempName)
        this.templateRepo.save(tempA)
        then:
        tempA.id != 0

        when:
        def tempA0 = this.templateRepo.findByMandatorAndName(mandator, tempName)
        then:
        tempA0.id == tempA.id
        tempA0.mandator == tempA.mandator
        tempA0.name == tempA.name

        when:
        this.templateRepo.save(new Template(mandator, tempName))
        then:
        def e = thrown(DataIntegrityViolationException)
        log.info("<abc,temp_a> {}", e.message);
    }
}

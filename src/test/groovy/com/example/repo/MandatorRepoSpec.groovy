package com.example.repo

import com.example.domain.Mandator
import com.example.domain.template.*
import com.example.repo.template.SectionRepo
import com.example.repo.template.TemplateRepo
import com.example.repo.template.VersionRepo
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.dao.DataIntegrityViolationException
import org.springframework.test.annotation.Commit
import org.springframework.transaction.annotation.Transactional
import spock.lang.Shared
import spock.lang.Stepwise

import javax.validation.ConstraintViolationException

/**
 * @author zzhao
 */
@Slf4j
@Stepwise
@Transactional
class MandatorRepoSpec extends JpaBaseSpec {

    @Autowired
    MandatorRepo mandatorRepo

    @Shared
    String mandatorZone = 'abc'

    @Shared
    String mandatorName = 'abc GmbH'

    @Autowired
    TemplateRepo templateRepo

    @Shared
    String templateNameA = 'temp_a'

    @Shared
    String templateNameB = 'temp_b'

    @Autowired
    SectionRepo sectionRepo

    @Autowired
    VersionRepo versionRepo

    @Commit
    def "mandator with zone abc"() {
        given:
        def mandator = new Mandator(this.mandatorZone, this.mandatorName)

        when:
        this.mandatorRepo.save(mandator)
        then:
        mandator.id

        def abc = this.mandatorRepo.findByZone(this.mandatorZone)
        then:
        abc
        abc.id == mandator.id
        abc.zone == this.mandatorZone
        abc.name == this.mandatorName
    }

    def "another mandator with zone abc cannot be saved"() {
        when:
        this.mandatorRepo.save(new Mandator(this.mandatorZone, 'def GmbH'))
        then:
        def e = thrown(DataIntegrityViolationException)
        log.info("{}", e.message)
    }

    @Commit
    def "abc,temp_a"() {
        given:
        def mandator = this.mandatorRepo.findByZone(this.mandatorZone)

        when:
        def tempA = new Template(mandator, templateNameA)
        this.templateRepo.save(tempA)
        then:
        tempA.id
        mandator.templates == [tempA]

        when:
        def tempA0 = this.templateRepo.findByMandatorAndName(mandator, templateNameA)
        then:
        tempA0.id == tempA.id
        tempA0.mandator == tempA.mandator
        tempA0.name == templateNameA
    }

    @Commit
    def "abc,temp_b"() {
        given:
        def mandator = this.mandatorRepo.findByZone(this.mandatorZone)

        when:
        def tempB = new Template(mandator, templateNameB)
        this.templateRepo.save(tempB)
        then:
        tempB.id
        mandator.templates.size() == 2
        mandator.templates == [this.templateRepo.findByMandatorAndName(mandator, this.templateNameA), tempB]

        when:
        def tempB0 = this.templateRepo.findByMandatorAndName(mandator, templateNameB)
        then:
        tempB0.id == tempB.id
        tempB0.mandator == tempB.mandator
        tempB0.name == templateNameB
    }

    def "another abc,temp_a cannot be saved"() {
        given:
        def mandator = this.mandatorRepo.findByZone(this.mandatorZone)

        when:
        this.templateRepo.save(new Template(mandator, templateNameA))
        then:
        def e = thrown(DataIntegrityViolationException)
        log.info("{}", e.message)
    }

    @Commit
    def "abc,temp_a,sec_meta,sec_desc,sec_misc"() {
        given:
        def mandator = this.mandatorRepo.findByZone(this.mandatorZone)
        def tempA = this.templateRepo.findByMandatorAndName(mandator, templateNameA)

        when:
        def secMeta = new Section(SectionType.Meta, 'meta')
        this.sectionRepo.save(secMeta)
        tempA.addSection(secMeta)
        this.templateRepo.save(tempA)
        then:
        tempA.sections.size() == 1
        secMeta.templates.size() == 1
        secMeta.templates == [tempA]

        when:
        def secDesc = new Section(SectionType.Description, 'desc,data')
        this.sectionRepo.save(secDesc)
        tempA.addSection(secDesc)
        then:
        tempA.sections.size() == 2
        secDesc.templates == [tempA]

        when:
        def secMisc = new Section(SectionType.Misc, 'misc')
        this.sectionRepo.save(secMisc)
        tempA.addSection(secMisc)
        then:
        tempA.sections.size() == 3
        secMisc.templates == [tempA]

        when:
        def secList = this.sectionRepo.findByTemplate(tempA).sort()
        then:
        secList.size() == 3
        secList.collect { it.type } == [SectionType.Meta, SectionType.Description, SectionType.Misc]
        secList[1].docSectionIds == ['desc', 'data']
        secList.each {
            it.templates == [tempA]
        }
    }

    def "abc,temp_a,sec_meta,sec_desc_sec_misc cannot take another sec_meta"() {
        given:
        def mandator = this.mandatorRepo.findByZone(this.mandatorZone)
        def tempA = this.templateRepo.findByMandatorAndName(mandator, this.templateNameA)

        when:
        tempA.addSection(this.sectionRepo.save(new Section(SectionType.Meta, 'meta')))
        then:
        def e = thrown(ConstraintViolationException)
        log.info("{}", e.message);
    }

    @Commit
    def "mandator with zone bcd"() {
        given:
        def bcd = new Mandator('bcd', 'bcd AG')

        when:
        this.mandatorRepo.save(bcd)
        then:
        bcd.id
        this.mandatorRepo.count() == 2
    }

    @Commit
    def "bcd,temp_a"() {
        given:
        def bcd = this.mandatorRepo.findByZone('bcd')

        when:
        def tempA = new Template(bcd, this.templateNameA)
        this.templateRepo.save(tempA)
        then:
        tempA.id
        bcd.templates == [tempA]

        when:
        def tempA0 = this.templateRepo.findByMandatorAndName(bcd, templateNameA)
        then:
        tempA0.id == tempA.id
        tempA0.mandator == tempA.mandator
        tempA0.name == templateNameA
    }

    @Commit
    def "bcd,temp_a,sec_risk"() {
        given:
        def mandator = this.mandatorRepo.findByZone('bcd')
        def tempA = this.templateRepo.findByMandatorAndName(mandator, templateNameA)

        when:
        def secRisk = new Section(SectionType.Risk, 'risk')
        this.sectionRepo.save(secRisk)
        tempA.addSection(secRisk)
        then:
        tempA.sections.size() == 1
        secRisk.templates == [tempA]

        when:
        def secList = this.sectionRepo.findByTemplate(tempA)
        then:
        secList == [secRisk]
    }

    def "abc,temp_a,sec_meta,sec_desc,sec_misc cannot take another sec_risk from mandator bcd"() {
        given:
        def abc = this.mandatorRepo.findByZone(this.mandatorZone)
        def tempA = this.templateRepo.findByMandatorAndName(abc, this.templateNameA)
        def secRisk = this.sectionRepo.findByType(SectionType.Risk)[0]

        when:
        tempA.addSection(secRisk)
        then:
        def e = thrown(ConstraintViolationException)
        log.info("{}", e.message);
    }

    @Commit
    def "abc,temp_a,sec_meta,init,commit_1,pm_appr,lc_appr,release"() {
        given:
        def mandator = this.mandatorRepo.findByZone(this.mandatorZone)
        def tempA = this.templateRepo.findByMandatorAndName(mandator, this.templateNameA)
        def secMeta = this.sectionRepo.findByTemplateAndType(tempA, SectionType.Meta)

        when:
        def init = new Version('xyz', [new SectionText('*blah*: blah')], 'initial commit')
        this.versionRepo.save(init)
        then:
        init.id

        when:
        def init0 = this.versionRepo.findOne(init.id)
        then:
        init0.id == init.id
        init0.commitMessage == init.commitMessage
        init0.author
        !init0.pmApprove
        !init0.lcApprove
        !init0.release


        when:
        secMeta.commitVersion(init)
        this.sectionRepo.save(secMeta)
        def secMeta0 = this.sectionRepo.findByTemplateAndType(tempA, SectionType.Meta)
        then:
        secMeta0.versions == [init]

        when:
        def commit_1 = new Version('pm', [new SectionText('*foo*: bar')], 'foo bar')
        this.versionRepo.save(commit_1)
        then:
        commit_1.id
        commit_1.author.userId == 'pm'
        !commit_1.pmApprove
        !commit_1.lcApprove
        !commit_1.release

        when:
        secMeta.commitVersion(commit_1)
        this.sectionRepo.save(secMeta)
        secMeta0 = this.sectionRepo.findByTemplateAndType(tempA, SectionType.Meta)
        then:
        secMeta0.versions == [commit_1, init]

        when:
        def commit_1A = this.versionRepo.findOne(commit_1.id)
        commit_1.pmApprove('pm')
        this.versionRepo.save(commit_1)
        then:
        commit_1A.pmApprove
        commit_1A.userActions.collect { it.actionName } == [ActionName.ApprovePM, ActionName.Author]

        when:
        commit_1.lcApprove('lc')
        this.versionRepo.save(commit_1)
        then:
        commit_1A.lcApprove
        commit_1A.userActions.collect {
            it.actionName
        } == [ActionName.ApproveLC, ActionName.ApprovePM, ActionName.Author]

        when:
        commit_1.release('pm')
        this.versionRepo.save(commit_1)
        then:
        commit_1A.release
        commit_1A.userActions.collect {
            it.actionName
        } == [ActionName.Release, ActionName.ApproveLC, ActionName.ApprovePM, ActionName.Author]
    }
}

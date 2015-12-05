package com.example.repo

import com.example.domain.Person
import com.example.domain.Planet
import groovy.util.logging.Slf4j
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.beans.factory.annotation.Value
import org.springframework.data.domain.PageRequest

/**
 * @author zzhao
 */
@Slf4j
class PlanetRepoSpec extends JpaBaseSpec {

    @Autowired
    PlanetRepo planetRepo

    @Autowired
    PersonRepo personRepo

    @Value('${server.port}')
    int serverPort

    @Value('${spring.h2.console.enabled}')
    boolean enabled

    def "should go through"() {
        setup:
        def earth = new Planet(name: 'Earth', people: [])
        def arthur = new Person(firstName: 'Arthur', lastName: 'Dent', planet: earth)
        def trillian = new Person(firstName: 'Trillian', lastName: 'McMillan', planet: earth)
        earth.people.add(arthur)
        earth.people.add(trillian)

        def ford = new Person(firstName: 'Ford', lastName: 'Prefect', planet: new Planet(name: 'Betelgeuse 5'))

        log.info("<ServerPort> ${this.serverPort} ${this.enabled}");

        when:
        this.planetRepo.save(earth)
        this.personRepo.save(ford);

        then:
        this.planetRepo.count() == 2
        this.personRepo.count() == 3

        this.planetRepo.findAll(new PageRequest(0, 10))
                .collect { it.name }
                .sort() == ['Betelgeuse 5', 'Earth']
        this.personRepo.findAll(new PageRequest(0, 10))
                .collect { it.firstName }
                .sort() == ['Arthur', 'Ford', 'Trillian']
    }
}

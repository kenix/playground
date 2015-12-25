/*
* Created at 10:14 on 25/12/15
*/
package com.example.web.portfolio;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.user.SimpUser;
import org.springframework.messaging.simp.user.SimpUserRegistry;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zzhao
 */
@RequiredArgsConstructor(onConstructor = @__({@Autowired}))
@RestController("stomp")
public class UserController {

    private final SimpUserRegistry userRegistry;

    @RequestMapping(path = "users", method = RequestMethod.GET)
    public List<String> listUsers() {
        return this.userRegistry.getUsers()
                .stream()
                .map(SimpUser::toString)
                .collect(Collectors.toList());
    }
}

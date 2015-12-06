/*
* vwd KL
* Created by zzhao on 12/4/15 3:50 PM
*/
package com.example.domain.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import javax.persistence.Embeddable;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import java.time.LocalDateTime;
import java.util.EnumSet;

import static com.example.domain.template.ActionName.*;

/**
 * @author zzhao
 */
@ToString(of = {"userId"})
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Embeddable
@Getter
public class UserAction {
    private static final EnumSet<ActionName> ACTION_NAMES_PM = EnumSet.of(ApprovePM, DisapprovePM);

    private static final EnumSet<ActionName> ACTION_NAMES_LC = EnumSet.of(ApproveLC, DisapproveLC);

    private String userId;

    private LocalDateTime timestamp;

    @Enumerated(EnumType.STRING)
    private ActionName actionName;

    public UserAction(String userId, ActionName actionName) {
        this.userId = userId;
        this.timestamp = LocalDateTime.now();
        this.actionName = actionName;
    }

    public boolean isPmAction() {
        return ACTION_NAMES_PM.contains(this.actionName);
    }

    public boolean isLcAction() {
        return ACTION_NAMES_LC.contains(this.actionName);
    }

    public boolean isRelease() {
        return ActionName.Release == this.actionName;
    }
}

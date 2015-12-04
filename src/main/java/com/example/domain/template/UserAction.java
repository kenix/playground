/*
* vwd KL
* Created by zzhao on 12/4/15 3:50 PM
*/
package com.example.domain.template;

import java.time.LocalDateTime;
import java.util.EnumSet;

import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;

import lombok.Getter;

import static com.example.domain.template.ActionName.*;

/**
 * @author zzhao
 */
@Entity
@Table(name = "user_action")
@Getter
public class UserAction {
    private static final EnumSet<ActionName> ACTION_NAMES_PM = EnumSet.of(ApprovePM, DisapprovePM);

    private static final EnumSet<ActionName> ACTION_NAMES_LC = EnumSet.of(ApproveLC, DisapproveLC);

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

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

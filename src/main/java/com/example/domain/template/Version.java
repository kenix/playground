/*
* vwd KL
* Created by zzhao on 11/4/15 4:48 PM
*/
package com.example.domain.template;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.OneToMany;
import javax.persistence.PostLoad;
import javax.persistence.PrePersist;
import javax.persistence.PreUpdate;
import javax.persistence.Table;
import javax.persistence.Transient;

import com.example.util.StreamUtil;
import lombok.Getter;

import static com.example.domain.template.ActionName.*;

/**
 * @author zzhao
 */
@Entity
@Table(name = "version")
public class Version {
    private static final String SEP = "°";

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Getter
    private long id;

    @Lob
    private String content;

    @Transient
    @Getter
    private List<String> texts;

    @OneToMany(cascade = CascadeType.ALL)
    private List<UserAction> userActions = new ArrayList<>();

    public Version(String userId, List<String> texts) {
        this.texts = texts;
        addUserAction(userId, Author);
    }

    public UserAction getAuthor() {
        return this.userActions.get(0); // always the first action
    }

    public UserAction getPmApprove() {
        final Optional<UserAction> lastPmAction = StreamUtil
                .reverse(this.userActions)
                .filter(UserAction::isPmAction)
                .findFirst();
        return lastPmAction.isPresent() && lastPmAction.get().getActionName() == ApprovePM
                ? lastPmAction.get()
                : null;
    }

    public UserAction getLcApprove() {
        final Optional<UserAction> lastLcAction = StreamUtil
                .reverse(this.userActions)
                .filter(UserAction::isLcAction)
                .findFirst();
        return lastLcAction.isPresent() && lastLcAction.get().getActionName() == ApproveLC
                ? lastLcAction.get()
                : null;
    }

    public UserAction getRelease() {
        return StreamUtil
                .reverse(this.userActions)
                .filter(UserAction::isRelease)
                .findFirst().orElse(null);
    }

    @PrePersist
    @PreUpdate
    private void toContent() {
        this.content = String.join(SEP, this.texts);
    }

    @PostLoad
    private void toTexts() {
        this.texts = Arrays.asList(this.content.split(SEP));
    }

    private void addUserAction(String userId, ActionName actionName) {
        this.userActions.add(actionName.by(userId));
    }

    public void pmApprove(String userId) {
        addUserAction(userId, ApprovePM);
    }

    public void pmDisapprove(String userId) {
        addUserAction(userId, DisapprovePM);
    }

    public void lcApprove(String userId) {
        addUserAction(userId, ApproveLC);
    }

    public void lcDisapprove(String userId) {
        addUserAction(userId, DisapproveLC);
    }

    public void release(String userId) {
        addUserAction(userId, Release);
    }
}

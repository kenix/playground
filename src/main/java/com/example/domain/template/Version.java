/*
* vwd KL
* Created by zzhao on 11/4/15 4:48 PM
*/
package com.example.domain.template;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

import static com.example.domain.template.ActionName.*;

/**
 * @author zzhao
 */
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Table(name = "version")
public class Version {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private long id;

    @Lob
    private String commitMessage;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "version_text", joinColumns = @JoinColumn(name = "version_id"))
    @OrderColumn(name = "section_text_idx", updatable = false, nullable = false)
    private List<SectionText> sectionTexts;

    @ElementCollection(fetch = FetchType.LAZY)
    @CollectionTable(name = "version_action", joinColumns = @JoinColumn(name = "version_id"))
    @OrderBy("timestamp DESC")
    private List<UserAction> userActions = new ArrayList<>(5);

    public Version(String userId, List<SectionText> sectionTexts, String commitMessage) {
        this.sectionTexts = sectionTexts;
        this.commitMessage = commitMessage;
        addUserAction(userId, Author);
    }

    public UserAction getAuthor() {
        return this.userActions.get(this.userActions.size() - 1); // always the first action
    }

    public UserAction getPmApprove() {
        final UserAction lastPmAction = this.userActions
                .stream()
                .filter(UserAction::isPmAction)
                .findFirst()
                .orElse(null);
        return lastPmAction != null && lastPmAction.getActionName() == ApprovePM ? lastPmAction : null;
    }

    public UserAction getLcApprove() {
        final UserAction lastLcAction = this.userActions
                .stream()
                .filter(UserAction::isLcAction)
                .findFirst()
                .orElse(null);
        return lastLcAction != null && lastLcAction.getActionName() == ApproveLC ? lastLcAction : null;
    }

    public UserAction getRelease() {
        return this.userActions
                .stream()
                .filter(UserAction::isRelease)
                .findFirst().orElse(null);
    }

    private void addUserAction(String userId, ActionName actionName) {
        this.userActions.add(0, actionName.by(userId));
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

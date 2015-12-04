/*
* vwd KL
* Created by zzhao on 12/4/15 4:06 PM
*/
package com.example.domain.template;

/**
 * @author zzhao
 */
public enum ActionName {
    Author,
    ApprovePM,
    DisapprovePM,
    ApproveLC,
    DisapproveLC,
    Release,;

    public UserAction by(String userId) {
        return new UserAction(userId, this);
    }
}

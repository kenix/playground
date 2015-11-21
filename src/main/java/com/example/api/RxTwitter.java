/*
* Created at 11:11 on 21/11/15
*/
package com.example.api;

import rx.Observable;
import twitter4j.Status;

/**
 * @author zzhao
 */
public interface RxTwitter {

    Observable<Status> observe();
}

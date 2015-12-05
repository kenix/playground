/*
* Created at 18:33 on 29/11/15
*/
package com.example.api;

/**
 * @author zzhao
 */
public interface TokenBucket {

    void takeBlocking() throws InterruptedException;

    void takeBlocking(int amount) throws InterruptedException;

    boolean tryTake();

    boolean tryTake(int amount);
}

/*
* vwd KL
* Created by zzhao on 3/3/16 3:34 PM
*/
package com.example.service;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.stereotype.Service;

/**
 * @author zzhao
 */
@Service
public class RemoteCallServiceWrapper implements RemoteCallService {

  private RemoteCallService delegate;

  void setDelegate(RemoteCallService delegate) {
    this.delegate = delegate;
  }

  @Override
  @HystrixCommand(fallbackMethod = "fallbackCall")
  public String call(String req) throws Exception {
    return this.delegate.call(req);
  }

  public String fallbackCall(String req) {
    return "FALLBACK: " + req;
  }
}

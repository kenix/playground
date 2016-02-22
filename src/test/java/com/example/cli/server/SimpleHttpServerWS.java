/*
* vwd KL
* Created by zzhao on 2/19/16 4:57 PM
*/
package com.example.cli.server;

import java.io.StringReader;

import javax.xml.transform.Source;
import javax.xml.transform.stream.StreamSource;
import javax.xml.ws.Endpoint;
import javax.xml.ws.Provider;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;
import javax.xml.ws.WebServiceProvider;
import javax.xml.ws.http.HTTPBinding;

import com.example.util.CliUtils;

/**
 * @author zzhao
 */
@WebServiceProvider
@ServiceMode(Service.Mode.PAYLOAD)
public class SimpleHttpServerWS implements Provider<Source> {
    @Override
    public Source invoke(Source request) {
        return new StreamSource(new StringReader("<p>Hello There!</p>"));
    }

    public static void main(String[] args) {
        final String address = "http://127.0.0.1:8000/test";
        final Endpoint endpoint = Endpoint.create(HTTPBinding.HTTP_BINDING, new SimpleHttpServerWS());
        endpoint.publish(address);
        System.out.println("server started @" + address);
        CliUtils.enterToStop(endpoint::stop);
    }
}

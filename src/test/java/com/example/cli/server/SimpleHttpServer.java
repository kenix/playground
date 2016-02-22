/*
* vwd KL
* Created by zzhao on 2/19/16 4:31 PM
*/
package com.example.cli.server;

import java.io.IOException;
import java.io.OutputStream;
import java.net.InetSocketAddress;

import com.example.util.CliUtils;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;

/**
 * @author zzhao
 */
public class SimpleHttpServer {

    public static void main(String[] args) throws Exception {
        final HttpServer server = HttpServer.create(new InetSocketAddress(8000), 0);
        server.createContext("/test", new MyHandler());
        server.setExecutor(null);
        server.start();
        System.out.println("server started @" + server.getAddress());

        CliUtils.enterToStop(() -> server.stop(0));
    }

    private static class MyHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            final String resp = "This is the test response";
            httpExchange.sendResponseHeaders(200, resp.length());
            final OutputStream os = httpExchange.getResponseBody();
            os.write(resp.getBytes());
            os.close();
        }
    }
}

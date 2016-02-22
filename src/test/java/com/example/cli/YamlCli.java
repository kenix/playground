/*
* vwd KL
* Created by zzhao on 1/26/16 4:18 PM
*/
package com.example.cli;

import java.io.IOException;
import java.io.InputStream;
import java.util.Map;

import org.yaml.snakeyaml.Yaml;
import org.yaml.snakeyaml.constructor.SafeConstructor;

/**
 * @author zzhao
 */
public class YamlCli {

    public static void main(String[] args) throws IOException {
        try (final InputStream is = Thread.currentThread().getContextClassLoader()
                .getResourceAsStream("zone.yml")) {
            final Yaml yaml = new Yaml(new SafeConstructor());
            int cnt = 0;
            for (Object data : yaml.loadAll(is)) {
                final Map<String, Object> map = (Map<String, Object>) data;
                System.out.println(map);
                cnt++;
            }
            System.out.println(cnt);
        }
    }
}

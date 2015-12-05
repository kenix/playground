/*
* Created at 00:50 on 05/12/15
*/
package com.example;

import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.select.Elements;

/**
 * @author zzhao
 */
public class MainCli {

    public static void main(String[] args) throws Exception {
        final Document doc = Jsoup.connect("http://jsoup.org").get();
        final Elements elements = doc.select("a[href]");
        elements.forEach(a -> System.out.println(a.attr("abs:href")));
    }
}

package com;

import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class WeiboUtil {
    //话题榜
    private static final String topic = "https://weibo.com/ajax/statuses/topic_band?sid=v_weibopro&category=all&page=1&count=10";
    //热搜
    private static final String hot = "https://weibo.com/ajax/side/hotSearch";
    //要闻
    private static final String need = "https://weibo.com/ajax/statuses/news";

    public static void main(String[] args) throws Exception {
        Connection connection = Jsoup.connect(topic);
        connection.ignoreContentType(true);
        System.out.println(connection.get().body());

        connection = Jsoup.connect(hot);
        connection.ignoreContentType(true);
        System.out.println(connection.get().body());

        connection = Jsoup.connect(need);
        connection.ignoreContentType(true);
        String sub = GetCookieUtil.getSub();
        System.out.println(sub);
        connection.header("Cookie", String.format("SUB=%s;", sub));
        System.out.println(connection.get().body());
    }
}

package com.jung.notify.domain;

import lombok.Builder;
import lombok.Getter;
import lombok.ToString;

@Builder
@Getter
@ToString
public class News {

    private String title;
    private String link;
    private String description;
    private String pubDate;

    public static String replace(String text){
        return text.replace("<b>", "")
                .replace("</b>", "")
                .replace("&quot;", "\"");
    }
}

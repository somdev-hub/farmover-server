package com.farmover.server.farmover.payloads;

import java.util.List;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class ArticleDto {

    private Integer id;

    private String authorName;

    private String title;

    private String date;

    private String thumbnail;

    private String subHeading;

    private String content;

    private Integer upCount;

    private Integer downCount;

    private Integer commentCount;

    private Boolean isUpVoted;

    private Boolean isDownVoted;

    private List<String> tags;

    private List<CommentArticleDto> articleComment;
}

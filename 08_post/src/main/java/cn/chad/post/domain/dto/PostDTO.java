package cn.chad.post.domain.dto;

import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

@Data
public class PostDTO implements Serializable {
    private static final long serialVersionUID = 1L;

    private Integer id;

    private String title;

    private String content;

    private Short type;

    private Integer channelId;

    private String labels;

    private String images;
}

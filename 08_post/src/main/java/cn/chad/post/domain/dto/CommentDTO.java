package cn.chad.post.domain.dto;

import lombok.Data;

import java.io.Serializable;
@Data
public class CommentDTO implements Serializable {

    private static final long serialVersionUID = 1L;

    private String content; // 评论内容

    private String nickname; // 评论人昵称

    private String parentId; // 上级ID，为0表示文章的顶级评论
}

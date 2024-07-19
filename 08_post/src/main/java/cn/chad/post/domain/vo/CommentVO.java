package cn.chad.post.domain.vo;

import lombok.Data;

import java.time.LocalDateTime;
@Data
public class CommentVO {
    private String id; //_id

    private Long postId; // 帖子ID

    private String content; // 评论内容

    private String userid; // 评论人ID

    private String nickname; // 评论人昵称

    private LocalDateTime createDatetime; // 评论的日期时间

    private int likeNum; // 点赞数

    private int replyNum; // 回复数

    private String state; // 状态，0：不可见；1：可见

    private String parentId; // 上级ID，为0表示文章的顶级评论

    private Boolean isLike;
}

package cn.chad.post.domain.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@ApiModel("评论实体")
@Document(collection = "comment")
@Data
public class Comment {

    @Id
    private String id; //_id
    @Indexed
    private Long postId; // 帖子ID
    
    private String content; // 评论内容
    
    @Indexed
    private String userid; // 评论人ID
    
    private String nickname; // 评论人昵称
    
    private LocalDateTime createDatetime; // 评论的日期时间

    private int likeNum; // 点赞数

    private int replyNum; // 回复数

    private String state; // 状态，0：不可见；1：可见

    private String parentId; // 上级ID，为0表示文章的顶级评论
}
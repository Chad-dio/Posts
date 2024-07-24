package cn.chad.post.domain.po;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

@ApiModel("收藏实体")
@Document(collection = "favorite")
@Data
public class FavoriteColl {
    @Id
    private String id; //_id
    @Indexed
    private Long postId;

    private String title; //标题
    @Indexed
    private Integer channelId;//频道ID
}

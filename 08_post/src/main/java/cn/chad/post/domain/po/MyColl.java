package cn.chad.post.domain.po;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import org.bson.types.ObjectId;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.List;

@ApiModel("个人收藏实体")
@Document(collection = "myFavorite")
@Data
public class MyColl {
    @Id
    private String id; //_id

    @Indexed
    private Integer userId;

    private Long postId;

    private String title; //标题
    @Indexed
    private Integer channelId;//频道ID
}

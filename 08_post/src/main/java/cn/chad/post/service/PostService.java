package cn.chad.post.service;

import cn.chad.post.domain.dto.PostDTO;
import cn.chad.post.domain.po.Post;
import cn.chad.post.domain.vo.Result;
import com.baomidou.mybatisplus.extension.service.IService;
import org.springframework.stereotype.Service;

@Service
public interface PostService extends IService<Post> {
    Result saveOrUpdatePost(PostDTO postDTO);

    Result delPost(Integer userId, Long postId);
}

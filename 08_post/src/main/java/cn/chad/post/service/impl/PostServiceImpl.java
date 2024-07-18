package cn.chad.post.service.impl;

import cn.chad.post.domain.dto.PostDTO;
import cn.chad.post.domain.po.Post;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.mapper.PostMapper;
import cn.chad.post.service.PostService;
import cn.chad.post.utils.PostIdWorker;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;

@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Resource
    private PostIdWorker postIdWorker;
    @Override
    public Result saveOrUpdatePost(PostDTO postDTO) {
        if(BeanUtil.isEmpty(postDTO) || postDTO.getContent().isEmpty()){
            return Result.error("发帖内容不能为空");
        }
        LocalDateTime now = LocalDateTime.now();
        Post post = BeanUtil.copyProperties(postDTO, Post.class);
        post.setSubmittedTime(now);
        post.setEnable((short) 1);
        post.setStatus(Post.Status.PUBLISHED.getCode());
        if(postDTO.getId() == null || postDTO.getId() == 0){
            //新增
            post.setUserId(1);
            post.setCreatedTime(now);
            post.setReason("");
            post.setPostId(postIdWorker.nextID("post:"));
            save(post);
        }else{
            //更新
            updateById(post);
        }
        return Result.success("添加成功");
    }

    @Override
    public Result delPost(Integer userId, Long postId) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper();
        wrapper.eq(Post::getUserId, userId)
                .eq(Post::getPostId, postId)
                .eq(Post::getEnable, 1);
        remove(wrapper);
        return Result.success("删除成功");
    }
}

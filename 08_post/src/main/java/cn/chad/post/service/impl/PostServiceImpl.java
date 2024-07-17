package cn.chad.post.service.impl;

import cn.chad.post.domain.po.Post;
import cn.chad.post.mapper.PostMapper;
import cn.chad.post.service.PostService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
}

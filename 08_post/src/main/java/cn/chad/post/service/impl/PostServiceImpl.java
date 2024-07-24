package cn.chad.post.service.impl;

import cn.chad.post.domain.dto.PostDTO;
import cn.chad.post.domain.po.MyColl;
import cn.chad.post.domain.po.Post;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.mapper.PostMapper;
import cn.chad.post.repository.MyCollRepository;
import cn.chad.post.service.PostService;
import cn.chad.post.utils.PostIdWorker;
import cn.hutool.core.bean.BeanUtil;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;

import static cn.chad.post.utils.RedisConstant.POST_COLLECTED_KEY;

@Service
@Slf4j
public class PostServiceImpl extends ServiceImpl<PostMapper, Post> implements PostService {
    @Resource
    private PostIdWorker postIdWorker;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
//    @Resource
//    private FavoriteRepository favoriteRepository;
    @Resource
    private MyCollRepository myCollRepository;

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

    @Override
    public Result findAllByChannel(Integer channelId) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, Post.Status.PUBLISHED.getCode())
                .eq(Post::getEnable, 1)
                .eq(Post::getChannelId, channelId);
        List<Post> postList = list(wrapper).stream()
                .map(post -> addExtraField(post, 1)).toList();
        return Result.success(postList);
    }

    @Override
    public Result findAll() {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, Post.Status.PUBLISHED.getCode())
                .eq(Post::getEnable, 1);
        List<Post> postList = list(wrapper).stream()
                .map(post -> addExtraField(post, 1)).toList();
        return Result.success(postList);
    }

    @Override
    public Result findMyAll(Integer userId) {
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId)
                .eq(Post::getEnable, 1);
        List<Post> postList = list(wrapper).stream()
                .map(post -> addExtraField(post, 1)).toList();
        return Result.success(postList);
    }

    @Override
    public Result collectPost(Long postId) {
        //1.获取当前用户
        //TODO：接入用户模块后，待更新
        Integer userId = 1;
        //2.查看是否被收藏了
        String key = POST_COLLECTED_KEY + postId;
        String fieldKey = userId.toString();
        Double score = stringRedisTemplate.opsForZSet().score(key, fieldKey);
        if(score == null){
            //3.未收藏
            //3.1 用户添加到帖子的收藏集合里去
            stringRedisTemplate.opsForZSet().add(key, fieldKey, System.currentTimeMillis());
            //3.2 帖子添加到用户的收藏集合去
            LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
            wrapper.eq(Post::getPostId, postId)
                            .eq(Post::getEnable, 1);
            Post post = getOne(wrapper);
            MyColl myColl = new MyColl();
            myColl.setUserId(userId);
            myColl.setPostId(postId);
            myColl.setTitle(post.getTitle());
            myColl.setChannelId(post.getChannelId());
            myCollRepository.insert(myColl);
            return Result.success("收藏成功");
        }else{
            //4.已收藏
            //4.1 将用户从帖子的收藏集合中去掉
            stringRedisTemplate.opsForZSet().remove(key, fieldKey);
            //4.2 将帖子从用户的收藏集合去掉
            long l = myCollRepository.removeByUserIdAndPostId(userId, postId);
            if(l > 0){
                return Result.success("取消收藏成功");
            }else{
                return Result.error("取消收藏失败");
            }
        }
    }

    @Override
    public Result getMyCollection(Integer userId) {
        List<Long> my = myCollRepository.findAll().stream()
                .map(myColl -> myColl.getPostId())
                .toList();
        return Result.success(my);
    }

    private Post addExtraField(Post post, Integer userId){
        String key = POST_COLLECTED_KEY + post.getPostId();
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        post.setIsCollected((score != null));
        post.setCollectNum(stringRedisTemplate.opsForZSet().size(key));
        return post;
    }
}

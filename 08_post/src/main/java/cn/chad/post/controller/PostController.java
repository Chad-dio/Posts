package cn.chad.post.controller;

import cn.chad.post.domain.dto.PostDTO;
import cn.chad.post.domain.po.Post;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.service.PostService;
import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("帖子相关接口")
@RestController
@RequestMapping("/post")
public class PostController {
    @Resource
    private PostService postService;

    @GetMapping("/findAll")
    @ApiOperation("查询所有的发帖")
    public Result findAll(){
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, Post.Status.PUBLISHED.getCode())
                .eq(Post::getEnable, 1);
        return Result.success(postService.list(wrapper));
    }

    @GetMapping("/list/{channelId}")
    @ApiOperation("查询频道下的所有发帖")
    public Result findAllByChannel(@PathVariable Integer channelId){
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getStatus, Post.Status.PUBLISHED.getCode())
                .eq(Post::getEnable, 1)
                .eq(Post::getChannelId, channelId);
        return Result.success(postService.list(wrapper));
    }

    @PostMapping("/save")
    @ApiOperation("保存或者更新发帖")
    public Result saveOrUpdatePost(@RequestBody PostDTO postDTO){
        return postService.saveOrUpdatePost(postDTO);
    }

    @DeleteMapping("/del/{userId}/{postId}")
    @ApiOperation("删帖")
    public Result delPost(
            @PathVariable Integer userId,
            @PathVariable Long postId){
        return postService.delPost(userId, postId);
    }

    @GetMapping("/list/{userId}")
    @ApiOperation("查询自己发布的所有帖子")
    public Result findMyAll(@PathVariable Integer userId){
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getUserId, userId)
                .eq(Post::getEnable, 1);
        return Result.success(postService.list(wrapper));
    }
    @GetMapping("/collection/{userId}")
    @ApiOperation("查询自己收藏的所有帖子")
    public Result findMyCollection(@PathVariable Integer userId){
        // TODO:去mongodb里面查询
        return null;
    }
}

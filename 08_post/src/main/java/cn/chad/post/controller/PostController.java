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
        return postService.findAll();
    }

    @GetMapping("/listChannel/{channelId}")
    @ApiOperation("查询频道下的所有发帖")
    public Result findAllByChannel(@PathVariable Integer channelId){
        return postService.findAllByChannel(channelId);
    }

    @GetMapping("/getPost/{postId}")
    @ApiOperation("获取单个帖子")
    public Result getPost(@PathVariable Long postId){
        LambdaQueryWrapper<Post> wrapper = new LambdaQueryWrapper<>();
        wrapper.eq(Post::getPostId, postId)
                .eq(Post::getEnable, 1);
        return Result.success(postService.getOne(wrapper));
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

    @GetMapping("/listMy/{userId}")
    @ApiOperation("查询自己发布的所有帖子")
    public Result findMyAll(@PathVariable Integer userId){
        return postService.findMyAll(userId);
    }
    @GetMapping("/collection/{userId}")
    @ApiOperation("查询自己收藏的所有帖子")
    public Result findMyCollection(@PathVariable Integer userId){
        // TODO:去mongodb里面查询
        return null;
    }

    @PostMapping("/collect/{postId}")
    @ApiOperation("收藏/取消收藏")
    public Result collectPost(@PathVariable Long postId){
        return postService.collectPost(postId);
    }
}

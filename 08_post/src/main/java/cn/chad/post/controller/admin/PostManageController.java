package cn.chad.post.controller.admin;

import cn.chad.post.domain.vo.Result;
import cn.chad.post.service.PostService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("帖子管理模块")
@RestController
@RequestMapping("/admin/post")
public class PostManageController {
    @Resource
    private PostService postService;

    @ApiOperation("删除单个用户发的全部帖子")
    @PostMapping("/delAll")
    public Result delAllPostByUserId(@RequestParam Integer userId){
        return postService.delAllPostByUserId(userId);
    }

    @ApiOperation("删除单个帖子")
    @PostMapping("/delOne")
    public Result delOnePostByPostId(@RequestParam Long postId){
        return postService.delOnePostByPostId(postId);
    }

    @GetMapping("/list/{userId}")
    @ApiOperation("查看单个用户发布的所有帖子")
    public Result findMyAll(@PathVariable Integer userId){
        return postService.findMyAll(userId);
    }

    @GetMapping("/listChannel/{channelId}")
    @ApiOperation("查询频道下的所有发帖")
    public Result findAllByChannel(@PathVariable Integer channelId){
        return postService.findAllByChannel(channelId);
    }

    @GetMapping("/findAll")
    @ApiOperation("查询所有的发帖")
    public Result findAll(){
        return postService.findAll();
    }
}

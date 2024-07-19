package cn.chad.post.controller;

import cn.chad.post.domain.dto.CommentDTO;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.service.CommentServcie;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;

@Api("评论相关接口")
@RestController
@RequestMapping("/comment")
public class CommentController {
    @Resource
    private CommentServcie commentServcie;

    @GetMapping("/findAll/{postId}")
    @ApiOperation("查询发帖下的全部评论")
    public Result findAll(@PathVariable Long postId){
        return commentServcie.findAll(postId);
    }

    @PostMapping("/add/{postId}")
    @ApiOperation("新增评论")
    public Result save(
            @PathVariable Long postId,
            @RequestBody CommentDTO commentDTO){
        return commentServcie.save(postId, commentDTO);
    }

    @DeleteMapping("/del/{postId}/{commentId}")
    @ApiOperation("删除评论")
    public Result deleteById(
            @PathVariable Long postId,
            @PathVariable String commentId){
        return commentServcie.deleteById(postId, commentId);
    }
}

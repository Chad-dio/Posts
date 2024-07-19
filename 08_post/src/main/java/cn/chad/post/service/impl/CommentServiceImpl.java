package cn.chad.post.service.impl;

import cn.chad.post.domain.dto.CommentDTO;
import cn.chad.post.domain.po.Comment;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.repository.CommentRepository;
import cn.chad.post.service.CommentServcie;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

@Service
@Slf4j
public class CommentServiceImpl implements CommentServcie {
    @Resource
    private CommentRepository commentRepository;
    @Override
    public Result findAll(Long postId) {
        List<Comment> comments = commentRepository.findByPostId(postId);
        return Result.success(comments);
    }

    @Override
    public Result save(Long postId, CommentDTO commentDTO) {
        String parentId = commentDTO.getParentId();
        if(!"0".equals(parentId)){
            //评论不是顶级评论，是回复评论
            Comment parentComment = commentRepository.findById(parentId).get();
            parentComment.setReplyNum(parentComment.getReplyNum() + 1);
            commentRepository.deleteById(parentId);
            commentRepository.save(parentComment);
        }
        Comment comment = BeanUtil.copyProperties(commentDTO, Comment.class);
        comment.setPostId(postId);
        comment.setUserid("1");
        comment.setLikeNum(0);
        comment.setReplyNum(0);
        comment.setState("1");
        return Result.success(commentRepository.save(comment));
    }

    @Override
    public Result deleteById(Long postId, String commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        String parentId = comment.getParentId();
        if(BeanUtil.isEmpty(comment)){
            return Result.error("评论不存在");
        }
        if("0".equals(parentId)){
            //评论是顶级评论，直接删除
            commentRepository.deleteById(commentId);
        }else{
            Comment parentComment = commentRepository.findById(parentId).get();
            parentComment.setReplyNum(Math.max(parentComment.getReplyNum() - 1, 0));
            commentRepository.deleteById(parentId);
            commentRepository.save(parentComment);
            commentRepository.deleteById(commentId);
        }
        return Result.success("删除成功");
    }
}

package cn.chad.post.service;

import cn.chad.post.domain.dto.CommentDTO;
import cn.chad.post.domain.vo.Result;

public interface CommentServcie {
    Result findAll(Long postId);

    Result save(Long postId, CommentDTO commentDTO);

    Result deleteById(Long postId, String commentId);
}

package cn.chad.post.service.impl;

import cn.chad.post.domain.dto.CommentDTO;
import cn.chad.post.domain.po.Comment;
import cn.chad.post.domain.vo.CommentVO;
import cn.chad.post.domain.vo.Result;
import cn.chad.post.repository.CommentRepository;
import cn.chad.post.service.CommentServcie;
import cn.hutool.core.bean.BeanUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

import static cn.chad.post.utils.RedisConstant.POST_COLLECTED_KEY;

@Service
@Slf4j
public class CommentServiceImpl implements CommentServcie {
    @Resource
    private CommentRepository commentRepository;
    @Resource
    private MongoTemplate mongoTemplate;
    @Resource
    private StringRedisTemplate stringRedisTemplate;
    @Override
    public Result findAll(Long postId) {
        List<CommentVO> commentVOS = commentRepository.findByPostId(postId)
                .stream()
                .map(comment -> {
                    CommentVO commentVO = BeanUtil.copyProperties(comment, CommentVO.class);
                    commentVO.setIsLike(isLiked(commentVO.getId(), 1));
                    return commentVO;
                }).toList();
        return Result.success(commentVOS);
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
        comment.setCreateDatetime(LocalDateTime.now());
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

    @Override
    public Result findAllReply(String commentId) {
        return Result.success(commentRepository.findByParentId(commentId));
    }

    @Override
    public Result getComment(String commentId) {
        Comment comment = commentRepository.findById(commentId).get();
        CommentVO commentVO = BeanUtil.copyProperties(comment, CommentVO.class);
        commentVO.setIsLike(isLiked(commentVO.getId(), 1));
        return Result.success(commentVO);
    }

    @Override
    public Result likeComment(String commentId) {
        //1.取出当前用户
        Integer userId = 1;
        //2.查看评论点赞状态
        String key = POST_COLLECTED_KEY + commentId;
        if(!isLiked(commentId, userId)){
            //3.未点赞
            stringRedisTemplate.opsForZSet().add(key, userId.toString(), System.currentTimeMillis());
            updateLikeNum(commentId, 1);
            return Result.success("点赞成功");
        }else{
            //4.已点赞
            stringRedisTemplate.opsForZSet().remove(key, userId.toString());
            updateLikeNum(commentId, -1);
            return Result.success("取消点赞成功");
        }
    }

    //更新点赞数
    private Result updateLikeNum(String id, int increment) {
        Query query = Query.query(Criteria.where("id").is(id));
        Update update = new Update().inc("likeNum", increment);
        return Result.success(mongoTemplate.updateFirst(query, update, Comment.class));
    }

    private boolean isLiked(String commentId, Integer userId){
        String key = POST_COLLECTED_KEY + commentId;
        Double score = stringRedisTemplate.opsForZSet().score(key, userId.toString());
        return score != null;
    }
}

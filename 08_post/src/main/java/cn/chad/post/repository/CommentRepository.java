package cn.chad.post.repository;

import cn.chad.post.domain.po.Comment;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface CommentRepository extends MongoRepository<Comment, String> {
    List<Comment> findByPostId(Long postId);

    List<Comment> findByParentId(String parentId);
}

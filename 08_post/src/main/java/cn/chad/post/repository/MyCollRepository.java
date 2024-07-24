package cn.chad.post.repository;

import cn.chad.post.domain.po.MyColl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface MyCollRepository extends MongoRepository<MyColl, String> {
    @Query(value = "{ 'userId' : ?0, 'postId' : ?1 }", delete = true)
    long removeByUserIdAndPostId(Integer userId, Long postId);
}

package cn.chad.post.repository;

import cn.chad.post.domain.po.FavoriteColl;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface FavoriteRepository extends MongoRepository<FavoriteColl, String> {
    FavoriteColl findByPostId(Long postId);
}

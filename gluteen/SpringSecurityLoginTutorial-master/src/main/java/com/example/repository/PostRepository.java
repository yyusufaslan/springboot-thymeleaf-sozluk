package com.example.repository;

import com.example.model.Post;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by yusufaslan on 23.06.2017.
 */
public interface PostRepository extends JpaRepository<Post, Long> {



   // List<Post> findByPostSenderOrderByPostDate(Long senderId);
    List<Post> findByPostSender_IdOrderByPostDate(Long senderId);

    @Modifying
    @Query("select m from Post m where m.postTitle=:titleId order by m.postDate")
    List<Post> findByPostTitleOrderByPostDate(Long titleId);


    List<Post> findByPostTitle_IdOrderByPostDate(Long titleId);

    @Modifying
    @Transactional
    @Query("update Post m set m.postBody=:postBody where m.id =:id ")
    Integer updatePost(@Param("postBody") String postBody,@Param("id") Long id);






}

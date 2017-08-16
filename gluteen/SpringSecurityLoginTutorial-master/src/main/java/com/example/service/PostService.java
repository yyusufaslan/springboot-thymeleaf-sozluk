package com.example.service;

import com.example.model.Post;
import com.example.model.Title;
import com.example.model.User;
import com.example.repository.PostRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * Created by yusufaslan on 23.06.2017.
 */
@Service
public class PostService {

    @Autowired
    private PostRepository postRepository;



    public Post getPostById (Long id)
    {
        return postRepository.findOne(id);
    }

    public List<Post> getPostsByPerson(Long senderId)
    {
        return postRepository.findByPostSender_IdOrderByPostDate(senderId);
    }

    public List<Post> findByPostTitleOrderByPostDate(Long titleId)
    {
        return postRepository.findByPostTitle_IdOrderByPostDate(titleId);
    }

    public void updatePost(String postBody,Long id)
    {
        postRepository.updatePost(postBody,id );
    }

    public void deletePost(Long id)
    {
        postRepository.delete(id);
    }

    public void savePost(Post post)
    {
        postRepository.save(post);
    }

    public Post postSave(String body, Title postTitle, String postImage, User postSender) {

        if (!postImage.isEmpty() && postImage != null) {
            return new Post(body , postTitle,"/imgs/post/" + postImage, new Date(),postSender);
        }
        return new Post(body,postTitle, new Date(), postSender);

    }



}


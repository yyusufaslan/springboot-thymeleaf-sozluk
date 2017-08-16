package com.example.controller;

import com.example.model.Post;
import com.example.service.PostService;
import com.example.service.TitleService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yusufaslan on 23.06.2017.
 */
@RestController
public class PostController {

    private static final Logger log = LoggerFactory.getLogger(PostController.class);

    @Autowired
    private PostService postService;


    @RequestMapping(value = "post/save")
    public void savePost(Post post)
    {
        postService.savePost(post);
    }

//    @RequestMapping(value ="post/update")
//    public void updatePost(Post post)
//    {
//        postService.updatePost(post);
//    }

    @RequestMapping(value = "post/delete/{id}")
    public void deletePost(@PathVariable("id")Long id)
    {
        postService.deletePost(id);
    }

    @RequestMapping(value = "post/postOfTitle/{id}")//bu id title'ın id sidir. post_title_id si buna eşit olanları getireceğiz.
    public List<Post> getPostOfTitle(@PathVariable("id")Long id)
    {
        return postService.findByPostTitleOrderByPostDate(id);
    }

    @RequestMapping(value = "post/postOrPerson/{id}")//bu id person'ın id sidir. post_sender_id si buna eşit olanları getireceğiz.
    public List<Post> getPostOfPerson(@PathVariable("id")Long id)
    {
        return postService.getPostsByPerson(id);
    }
    @RequestMapping(value = "post/getPost/{id}")
    public Post getPostById(@PathVariable("id")Long id)
    {
        return postService.getPostById(id);
    }






}


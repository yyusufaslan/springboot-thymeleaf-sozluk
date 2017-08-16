package com.example.controller;

import com.example.model.Title;
import com.example.service.TitleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * Created by yusufaslan on 23.06.2017.
 */
@RestController
public class TitleController {

    @Autowired
    private TitleService titleService;

    @RequestMapping(value = "/title",method = RequestMethod.GET)
    public List<Title> getTitles(){
        Authentication auth = SecurityContextHolder.getContext().getAuthentication();

        return titleService.getTitlesOrderByDate();
    }



}

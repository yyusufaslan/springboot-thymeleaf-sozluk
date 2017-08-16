package com.example.service;

import com.example.model.Title;
import com.example.repository.TitleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * Created by yusufaslan on 23.06.2017.
 */
@Service
public class TitleService {

    @Autowired
    private TitleRepository titleRepository;

    public Title getTitleById(Long id)
    {
        return titleRepository.findById(id);
    }

    public List<Title> getTitlesOrderByDate()
    {
        return titleRepository.findAllByOrOrderById();
    }

    public List<Title> getTitlesByPerson(Long id){
        return titleRepository.findByTitleCreater_IdOrderByTitleCreated(id);
    }
    public void updateTitle(Title title)
    {
        titleRepository.updateTitle(title.getId(), title.getTitleBody());
    }

    public Title createTitle(Title title)
    {
        return titleRepository.save(title);
    }
    public void deleteTitle(Long id)
    {
        titleRepository.deleteTitleById(id);
    }

    public List<Title> titleAra(String aranan)
    {
        return titleRepository.findByTitleBodyContaining(aranan);
    }
}

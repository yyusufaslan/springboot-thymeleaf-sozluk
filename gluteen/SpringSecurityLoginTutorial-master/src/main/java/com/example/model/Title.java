package com.example.model;

import javax.persistence.*;
import java.util.Date;

/**
 * Created by yusufaslan on 23.06.2017.
 */
@Entity
public class Title {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String titleBody;

    @Temporal(TemporalType.TIMESTAMP)
    private Date titleCreated;

    @ManyToOne
    @JoinColumn(name = "title_creater_id")
    private User titleCreater;

    public Title(String titleBody, Date titleCreated, User titleCreater) {
        this.titleBody = titleBody;
        this.titleCreated = titleCreated;
        this.titleCreater = titleCreater;
    }
    public Title(){}
    public User getTitleCreater() {
        return titleCreater;
    }

    public void setTitleCreater(User titleCreater) {
        this.titleCreater = titleCreater;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitleBody() {
        return titleBody;
    }

    public void setTitleBody(String titleBody) {
        this.titleBody = titleBody;
    }

    public Date getTitleCreated() {
        return titleCreated;
    }

    public void setTitleCreated(Date titleCreated) {
        this.titleCreated = titleCreated;
    }



}

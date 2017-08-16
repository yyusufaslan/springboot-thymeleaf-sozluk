package com.example.repository;

import com.example.model.Title;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import javax.websocket.server.PathParam;
import java.util.List;

/**
 * Created by yusufaslan on 23.06.2017.
 */
public interface TitleRepository extends JpaRepository<Title,Long> {


    Title findById(Long id);

    @Modifying
    @Query("select m from Title m order by m.id")
    List<Title> findAllByOrOrderById();

    //List<Title> findAllByOrOrderById();
    Title deleteTitleById(Long id);

    @Modifying
    @Query("update Title m set m.titleBody =:titleBody where m.id =:id ")
    Title updateTitle(@PathParam("id") Long id , @PathParam("titleBody") String titleBody);


    List<Title> findByTitleBodyContaining(@PathParam("aranan")String aranan);

//    @Modifying
//    @Query("select m from Title m where m.titleCreater=:id order by m.titleCreated")
    List<Title> findByTitleCreater_IdOrderByTitleCreated(Long id);
}

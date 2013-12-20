package models;

import play.data.validation.Constraints.Required;
import play.db.ebean.Model;

import javax.persistence.*;

/**
 * Created by nepjua on 12/16/13.
 */
@Entity
public class Post extends Model {

    @Id
    public Long id;

    @Required
    @Column
    public String title;

    @Column(columnDefinition = "TEXT")
    public String markdown;

    @Column(columnDefinition = "TEXT")
    public String html;

    @ManyToOne(fetch = FetchType.LAZY)
    public User author;


    public static Finder<Long, Post> find = new Finder(Long.class, User.class);


}

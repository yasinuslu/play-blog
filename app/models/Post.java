package models;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.AppException;
import helpers.JsonHelper;
import helpers.StringHelper;
import helpers.ValidationParam;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

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

    @Required
    @Column(unique = true)
    public String slug;

    @ManyToOne(targetEntity = User.class, fetch = FetchType.EAGER)
    @JsonBackReference
    public User author;

    public static boolean validateJSON(JsonNode json, ObjectNode result) {
        JsonHelper.checkParams(json, new String[]{"title"}, result);

        if ("ok".equals(result.get("status").asText())) {
            return true;
        } else {
            return false;
        }
    }

    public static Finder<Long, Post> find = new Finder(Long.class, Post.class);

    public static Post build(JsonNode json, ObjectNode result) throws AppException {
        if (!validateJSON(json, result)) {
            return null;
        }

        Post post = Json.fromJson(json, Post.class);
        post.createSlug();

        return post;
    }

    private void createSlug() {
        this.slug = StringHelper.toSlug(this.title);
    }

    /**
     * @param json   (json that has user data)
     * @param result (result to given to rest client)
     * @return returns created user
     */
    public static Post create(JsonNode json, ObjectNode result) throws AppException {
        Post post = build(json, result);

        try {
            post = create(post);
        } catch (PersistenceException ex) {
            result.put("status", "duplicate-error");
            ArrayNode params = (ArrayNode) result.findPath("params");
            ValidationParam param = new ValidationParam("slug", json.get("title").asText(), "duplicate");
            params.add(JsonHelper.toNode(param));

            post = null;
        }

        return post;
    }

    public static Post create(Post post) {
        post.save();
        return post;
    }

    public static List<Post> search(String term) {
        String regex = "%" + term.toLowerCase() + "%";
        return find
                .where("lower(title) like :regex OR lower(html) like :regex OR lower(markdown) like :regex")
                .setParameter("regex", regex)
                .findList();
    }
}

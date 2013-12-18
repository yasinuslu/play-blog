package models;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.AppException;
import helpers.HashHelper;
import helpers.JsonHelper;
import helpers.ValidationParam;
import play.data.validation.Constraints.Required;
import play.db.ebean.Model;
import play.libs.Json;

import javax.persistence.*;
import java.util.List;

/**
 * Created by nepjua on 12/15/13.
 */
@Entity
public class User extends Model {

    @Id
    public Long id;

    @Required
    @Column(unique = true)
    public String username;

    @Required
    public String password;

    @Column(name = "full_name")
    public String name;

    @OneToMany(cascade=CascadeType.ALL, targetEntity = Post.class)
    public List<Post> posts;

    public static Finder<Long, User> find = new Finder(
            Long.class,
            User.class
    );

    public static List<User> search(String term) {
        String regex = "%" + term.toLowerCase() + "%";
        return find
                .where("lower(username) like :regex OR lower(full_name) like :regex")
                .setParameter("regex", regex)
                .findList();
    }

    public static User findById(Long id) {
        return find.byId(id);
    }

    public static List<User> all() {
        return find.all();
    }

    public void setPassword(String password) throws AppException {
        this.password = HashHelper.createPassword(password);
    }

    public void checkPassword(String candidate) {
        HashHelper.checkPassword(candidate, password);
    }

    public static boolean validateJSON(JsonNode json, ObjectNode result) {
        JsonHelper.checkParams(json, new String[]{"username", "password"}, result);

        if ("ok".equals(result.get("status").asText())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     * @param user object for saving to database and validating
     * @return user object after validation
     */

    public static User create(User user) throws PersistenceException {
        user.save();
        return user;
    }

    public static User build(JsonNode json, ObjectNode result) throws AppException {
        if (!validateJSON(json, result)) {
            return null;
        }

        User user = Json.fromJson(json, User.class);
        user.setPassword(json.get("password").asText());

        return user;
    }

    /**
     * @param json   (json that has user data)
     * @param result (result to given to rest client)
     * @return returns created user
     */
    public static User create(JsonNode json, ObjectNode result) throws AppException {
        User user = build(json, result);

        try {
            user = create(user);
        } catch (PersistenceException ex) {
            result.put("status", "duplicate-error");
            ArrayNode params = (ArrayNode) result.findPath("params");
            ValidationParam param = new ValidationParam("username", json.get("username").asText(), "duplicate");
            params.add(JsonHelper.toNode(param));

            user = null;
        }

        return user;
    }


}

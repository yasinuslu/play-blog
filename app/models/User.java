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
import play.db.DB;
import play.libs.Json;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.PersistenceException;
import java.util.List;

/**
 * Created by nepjua on 12/15/13.
 */
@Entity(name = "user")
public class User extends Model {

    @Id public Long id;

    @Required
    @Column( unique=true )
    public String username;
    @Required public String password;

    @Column(name="full_name")
    public String name;

    public static Finder<Long, User> find = new Finder(
        Long.class,
        User.class
    );

    public static List<User> search(String query) {
//        return find
//                .where("username LIKE '%" + query + "%'")
//                .where("name LIKE '%" + query + "%'")
//                .findList();

        return find.setQuery(
                "SELECT * FROM user " +
                "WHERE full_name LIKE '%" + query + "%'" +
                "AND username LIKE '%" + query + "%'").findList();
    }

    public static User findById(Long id) {
        return find.byId(id);
    }

    public static List<User> all () {
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

        if("ok".equals(result.get("status").asText())) {
            return true;
        } else {
            return false;
        }
    }

    /**
     *
     * @param user object for saving to database and validating
     * @return user object after validation
     */

    public static User create(User user) throws PersistenceException{
        user.save();
        return user;
    }

    /**
     *
     * @param json (json that has user data)
     * @param result (result to given to rest client)
     * @return returns created user
     */
    public static User create(JsonNode json, ObjectNode result) throws AppException {
        if(!validateJSON(json, result)) {
            return null;
        }

        User user = Json.fromJson(json, User.class);
        user.setPassword(json.get("password").asText());

        try {
            user = create(user);
        } catch(PersistenceException ex) {
            result.put("status", "duplicate-error");
            ArrayNode params = (ArrayNode) result.findPath("params");
            ValidationParam param = new ValidationParam("username", json.get("username").asText(), "duplicate");
            params.add(JsonHelper.toNode(param));

            user = null;
        }

        return user;
    }
}

package controllers.api;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.AppException;
import helpers.JsonHelper;
import models.User;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

public class UserController extends Controller {

    public static Result all() {
        String result = JsonHelper.toJSON(User.all());

        return ok(result);
    }

    public static Result getUser(Long id) {
        ObjectNode result = Json.newObject();

        User user = User.find.byId(id);

        if(user == null) {
            result.put("status", "not-found");
        } else {
            result.put("status", "ok");
        }

        result.put("data", JsonHelper.toNode(user));

        return ok(result);
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result createUser() throws AppException {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();

        User user = User.create(json, result);

        if(user == null) {
            return badRequest(result);
        } else {
            result.put("data", JsonHelper.toNode(user));
            return ok(result);
        }
    }

    public static Result searchUser(String query) {
        ObjectNode result = Json.newObject();

        List<User> users = User.search(query);

        result.put("status", "ok");
        result.put("data", JsonHelper.toNode(users));

        return ok(result);
    }

    public static Result putUser(Long id) {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();

        try {
            User user = User.build(json, result);

            if(user == null) {
                return badRequest(result);
            }

            user.update(id);

            result.put("data", JsonHelper.toNode(user));
            return ok(result);
        } catch(Exception e) {
            JsonHelper.handleError(e, result);

            return badRequest(result);
        }
    }

    public static Result deleteUser(Long id) {
        ObjectNode result = Json.newObject();

        User user = User.find.byId(id);

        if(user == null) {
            result.put("status", "not-found");
        } else {
            user.delete();
            result.put("status", "ok");
        }

        return ok(result);
    }
}

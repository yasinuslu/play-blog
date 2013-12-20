package controllers.api;

import models.Post;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.node.ObjectNode;
import exceptions.AppException;
import helpers.JsonHelper;
import models.User;
import play.db.ebean.Model;
import play.libs.Json;
import play.mvc.BodyParser;
import play.mvc.Controller;
import play.mvc.Result;

import java.util.List;

/**
 * Created by nepjua on 12/20/13.
 */
public class PostController extends Controller {

    public static Result all() {
        return ok(JsonHelper.toJSON(Post.find.all()));
    }

    @BodyParser.Of(BodyParser.Json.class)
    public static Result createPost() throws AppException {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();

        Post user = Post.create(json, result);

        if(user == null) {
            return badRequest(result);
        } else {
            result.put("data", JsonHelper.toNode(user));
            return ok(result);
        }
    }


    public static Result getPost(Long id) {
        ObjectNode result = Json.newObject();

        Post post = Post.find.byId(id);

        if(post == null) {
            result.put("status", "not-found");
        } else {
            result.put("status", "ok");
        }

        result.put("data", JsonHelper.toNode(post));

        return ok(result);
    }

    public static Result putPost(Long id) {
        JsonNode json = request().body().asJson();
        ObjectNode result = Json.newObject();

        try {
            Post post = Post.build(json, result);

            if(post == null) {
                return badRequest(result);
            }

            post.update(id);

            result.put("data", JsonHelper.toNode(post));
            return ok(result);
        } catch(Exception e) {
            JsonHelper.handleError(e, result);

            return badRequest(result);
        }
    }

    public static Result deletePost(Long id) {
        ObjectNode result = Json.newObject();

        Post post = Post.find.byId(id);

        if(post == null) {
            result.put("status", "not-found");
        } else {
            post.delete();
            result.put("status", "ok");
        }

        return ok(result);
    }

    public static Result searchPost(String q) {
        ObjectNode result = Json.newObject();

        List<Post> posts = Post.search(q);

        result.put("status", "ok");
        result.put("data", JsonHelper.toNode(posts));

        return ok(result);
    }
}

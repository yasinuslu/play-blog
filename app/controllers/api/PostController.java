package controllers.api;

import models.Post;

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

/**
 * Created by nepjua on 12/20/13.
 */
public class PostController extends Controller {

    public static Result all() {
        return ok(JsonHelper.toJSON(Post.find.all()));
    }



}

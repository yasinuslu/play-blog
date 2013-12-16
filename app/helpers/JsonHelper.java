package helpers;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;
import com.fasterxml.jackson.databind.node.JsonNodeFactory;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.fasterxml.jackson.databind.util.JSONPObject;
import play.libs.Json;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by nepjua on 12/15/13.
 */
public class JsonHelper {
    public static String toJSON(Object o) {
        return String.valueOf(Json.toJson(o));
    }

    public static JsonNode toNode(Object o) {
        return Json.toJson(o);
    }

    public static void checkParams(JsonNode json, String[] params, ObjectNode result) {
        ArrayNode missingParams = new ArrayNode(JsonNodeFactory.instance);
        ObjectNode error = Json.newObject();

        error.put("params", missingParams);

        result.put("error", error);
        result.put("status", "ok");

        if(json == null) {
            result.put("status", "invalid-json");
            error.put("message", "Json Expected");
            return;
        }

        for(final String param: params) {
            final String val = json.findPath(param).asText();
            if(val.isEmpty()) {
                result.put("status", "missing-param");

                ValidationParam validationParam = new ValidationParam(param, val, "missing");
                missingParams.add(toNode(validationParam));

                error.put("message", "One or more parameters are missing");
            }
        }
    }
}

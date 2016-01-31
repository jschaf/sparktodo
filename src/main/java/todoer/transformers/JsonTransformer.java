package todoer.transformers;

import com.google.gson.Gson;
import spark.ResponseTransformer;

public final class JsonTransformer {

    private JsonTransformer() {}

    public static String toJson(Object object) {
        return new Gson().toJson(object);
    }

    public static ResponseTransformer json() {
        return JsonTransformer::toJson;
    }
}

package com.sharathp.myorktimes.util.gson;

import com.google.gson.Gson;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.sharathp.myorktimes.models.Article;

import java.lang.reflect.Type;

public class ByLineDeserializer implements JsonDeserializer<Article.ByLine> {
    // this is thread-safe
    private final Gson mGson = new Gson();

    @Override
    public Article.ByLine deserialize(final JsonElement json, final Type typeOfT, final JsonDeserializationContext context) throws JsonParseException {
        // NewYork Times API is erroneously returning an empty array sometimes, if it is the
        // case, simply return null
        if (json.isJsonArray()) {
            return null;
        }

        return mGson.fromJson(json, Article.ByLine.class);
    }
}

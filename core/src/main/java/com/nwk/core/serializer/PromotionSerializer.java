package com.nwk.core.serializer;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonParseException;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;
import com.nwk.core.model.Promotion;
import com.nwk.core.model.PromotionDiscount;
import com.nwk.core.model.PromotionGeneral;
import com.nwk.core.model.PromotionReduction;

import java.lang.reflect.Type;

/**
 * Created by Andy on 12/23/2014.
 */
public class PromotionSerializer implements JsonSerializer<Promotion>, JsonDeserializer<Promotion> {
    @Override
    public JsonElement serialize(Promotion src, Type typeOfSrc, JsonSerializationContext context) {
        return new JsonPrimitive(src.toString());
    }

    @Override
    public Promotion deserialize(JsonElement json, Type typeOfT, JsonDeserializationContext context) throws JsonParseException {
        Promotion p = null;
        if(json.getAsJsonObject().get("original_price")!=null){
            p = new GsonBuilder()
                    .create()
                    .fromJson(json, PromotionReduction.class);
        }else if(json.getAsJsonObject().get("price")!=null){
            p = new GsonBuilder()
                    .create()
                    .fromJson(json, PromotionGeneral.class);
        }else if(json.getAsJsonObject().get("discount")!=null){
            p = new GsonBuilder()
                    .create()
                    .fromJson(json, PromotionDiscount.class);
        }else{
            p = new GsonBuilder()
                    .create()
                    .fromJson(json, Promotion.class);
        }
        return p;
    }
}

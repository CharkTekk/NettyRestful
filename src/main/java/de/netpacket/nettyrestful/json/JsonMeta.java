package de.netpacket.nettyrestful.json;

import com.google.gson.*;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashSet;
import java.util.Set;


public class JsonMeta {

    private static final JsonParser JSON_PARSER = new JsonParser();
    private final JsonObject jsonObject;
    private final Gson gson;


    public JsonMeta() {
        this.jsonObject = new JsonObject();
        this.gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();
    }

    public JsonMeta(JsonObject jsonObject) {
        this.jsonObject = jsonObject;
        this.gson = new GsonBuilder().serializeNulls().disableHtmlEscaping().setPrettyPrinting().create();

    }

    public JsonMeta append(String key, String value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public JsonMeta append(String key, Number value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public JsonMeta append(String key, Boolean value) {
        this.jsonObject.addProperty(key, value);
        return this;
    }

    public JsonMeta append(String key, JsonMeta value) {
        this.jsonObject.add(key, value.jsonObject);
        return this;
    }

    public JsonMeta append(String key, Object value) {
        if (value == null) return this;
        this.jsonObject.add(key, gson.toJsonTree(value));
        return this;
    }

    public String getString(String key) {
        if (!this.jsonObject.has(key)) return null;
        return this.jsonObject.get(key).getAsString();
    }

    public int getInt(String key) {
        if (!this.jsonObject.has(key)) return 0;
        return this.jsonObject.get(key).getAsInt();
    }

    public double getDouble(String key) {
        if (!this.jsonObject.has(key)) return 0.0D;
        return this.jsonObject.get(key).getAsDouble();
    }

    public boolean getBoolean(String key) {
        if (!this.jsonObject.has(key)) return false;
        return this.jsonObject.get(key).getAsBoolean();
    }

    public <T> T getObject(String key, Class<T> class_) {
        if (!this.jsonObject.has(key)) return null;
        JsonElement element = this.jsonObject.get(key);

        return gson.fromJson(element, class_);
    }

    public JsonArray getArray(String key) {
        return this.jsonObject.get(key).getAsJsonArray();
    }

    public JsonMeta getDocument(String key) {
        return new JsonMeta(jsonObject.get(key).getAsJsonObject());
    }

    public boolean contains(String key){
        return this.jsonObject.entrySet().stream().anyMatch((t) -> t.getKey().equalsIgnoreCase(key));
    }

    public Set<String> keySet(){
        Set<String> keys = new HashSet<>();
        jsonObject.entrySet().forEach(e -> {
            keys.add(e.getKey());
        });
        return keys;
    }

    public String toJsonString() {
        return gson.toJson(jsonObject.getAsJsonObject()).replace("_", "#");
    }

    public void saveAsConfig(File backend) {
        if (backend == null) return;

        if (backend.exists()) {
            backend.delete();
        }

        try (OutputStreamWriter writer = new OutputStreamWriter(new FileOutputStream(backend), StandardCharsets.UTF_8)) {
            gson.toJson(jsonObject, writer);
        } catch (IOException ex) {
            ex.getStackTrace();

        }
    }

    public static JsonMeta load(String input) {
        return new JsonMeta(JSON_PARSER.parse(input.replace("#","_")).getAsJsonObject());
    }

    public static JsonMeta loadConfig(File backend) {
        try (InputStreamReader reader = new InputStreamReader(new FileInputStream(backend), StandardCharsets.UTF_8)) {
            JsonObject object = JSON_PARSER.parse(new BufferedReader(reader)).getAsJsonObject();
            return new JsonMeta(object);
        } catch (Exception ex) {
            ex.getStackTrace();
            return new JsonMeta();
        }
    }


}

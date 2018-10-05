package com.github.blarosen95.mywarpsgui.Util;

import org.apache.commons.io.IOUtils;
import org.json.simple.JSONObject;
import org.json.simple.JSONValue;
import org.json.simple.parser.ParseException;

import java.io.IOException;
import java.net.URL;

public class NameToUUID {
    public String getUUID(String name) {
        String url = "https://api.mojang.com/users/profiles/minecraft/" + name;

        try {
            String uuidJson = IOUtils.toString(new URL(url), "UTF-8");
            if (uuidJson.isEmpty()) return "invalid name";
            JSONObject uuidObject = (JSONObject) JSONValue.parseWithException(uuidJson);
            return uuidObject.get("id").toString();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return null; // TODO: 10/5/2018 Make sure variables assigned to the return of this method are not null before using them.
    }
}

package com.bluerover.gson;

import java.lang.reflect.Type;

import com.bluerover.model.Result;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ResultDeserializer<T> implements JsonDeserializer<Result<T>> {

	@Override
	public Result<T> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {

		final Result<T> result = new Result<T>();
		
		try {
			JsonObject jsonObject = json.getAsJsonObject();
			
//			resultsList.add(context.deserialize(jsonObject.get("events"),
//					Event[].class));
			result.setJsonObject(jsonObject);
		} catch (Exception e) {
			JsonArray jsonArray = json.getAsJsonArray();
			result.setJsonArray(jsonArray);
//			ArrayList<T> tmpList = new ArrayList<T>();
//			for (JsonElement obj : jsonArray) {
//				tmpList.add((T) context.deserialize(obj, Device.class));
//			}
//			resultsList.add(tmpList.toArray());
		}

//		System.out.println(jsonObject);
//		System.out.println(jsonArray);
//		System.out.println(resultsList);

		// Device[] devices = context.deserialize(jsonObject.get("device"),
		// Device[].class);
		// Rfid[] rfids = context.deserialize(jsonObject.get("rfid"),
		// Rfid[].class);

		return result;
	}
}

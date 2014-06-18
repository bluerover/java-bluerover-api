package com.bluerover.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import com.bluerover.model.Device;
import com.bluerover.model.Event;
import com.bluerover.model.Result;
import com.bluerover.model.Rfid;
import com.google.gson.JsonArray;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;
import com.google.gson.reflect.TypeToken;

public class ResultDeserializer<T> implements JsonDeserializer<Result<T>> {

	@SuppressWarnings({ "unchecked", "rawtypes" })
	@Override
	public Result<T> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		JsonObject jsonObject = null;
		JsonArray jsonArray = null;
		int page = 0;
		int pages = 0;
		int results = 0;
		ArrayList resultsList = new ArrayList();

		final Result<T> result = new Result<T>();
		
//		Type fooType = new TypeToken<T>(){}.getType();
//		System.out.println(fooType);
		try {
			jsonObject = json.getAsJsonObject();
			
			page = jsonObject.get("page").getAsInt();
			pages = jsonObject.get("pages").getAsInt();
			results = jsonObject.get("results").getAsInt();
			resultsList.add(context.deserialize(jsonObject.get("events"),
					Event[].class));
		} catch (Exception e) {
			jsonArray = json.getAsJsonArray();
			ArrayList<T> tmpList = new ArrayList<T>();
			for (JsonElement obj : jsonArray) {
				tmpList.add((T) context.deserialize(obj, Device.class));
			}
			resultsList.add(tmpList.toArray());
		}

//		System.out.println(jsonObject);
//		System.out.println(jsonArray);
//		System.out.println(resultsList);

		// Device[] devices = context.deserialize(jsonObject.get("device"),
		// Device[].class);
		// Rfid[] rfids = context.deserialize(jsonObject.get("rfid"),
		// Rfid[].class);

		result.setPage(page);
		result.setPages(pages);
		result.setResults(results);
		result.setList(new ArrayList<T>(Arrays.asList((T[]) resultsList.get(0))));
		return result;
	}
}

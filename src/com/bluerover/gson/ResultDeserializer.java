package com.bluerover.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import com.bluerover.model.Device;
import com.bluerover.model.Event;
import com.bluerover.model.Result;
import com.bluerover.model.Rfid;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ResultDeserializer<T> implements JsonDeserializer<Result<T>> {

	@SuppressWarnings("unchecked")
	@Override
	public Result<T> deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		final int page = jsonObject.get("page").getAsInt();
		final int pages = jsonObject.get("pages").getAsInt();
		final int results = jsonObject.get("results").getAsInt();
		
		Event[] events = context.deserialize(jsonObject.get("events"),
				Event[].class);
		Device[] devices = context.deserialize(jsonObject.get("device"), Device[].class);
		Rfid[] rfids = context.deserialize(jsonObject.get("rfid"), Rfid[].class);
		final Result<T> result = new Result<T>();
		result.setPage(page);
		result.setPages(pages);
		result.setResults(results);
		result.setList(new ArrayList<T>(Arrays.asList((T[])events)));
		return result;
	}
}

package com.bluerover.gson;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Arrays;

import com.bluerover.api.Event;
import com.bluerover.api.Result;
import com.google.gson.JsonDeserializationContext;
import com.google.gson.JsonDeserializer;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParseException;

public class ResultDeserializer implements JsonDeserializer<Result> {

	@Override
	public Result deserialize(JsonElement json, Type typeOfT,
			JsonDeserializationContext context) throws JsonParseException {
		final JsonObject jsonObject = json.getAsJsonObject();
		final int page = jsonObject.get("page").getAsInt();
		final int pages = jsonObject.get("pages").getAsInt();
		final int results = jsonObject.get("results").getAsInt();
		Event[] events = context.deserialize(jsonObject.get("events"),
				Event[].class);
		final Result result = new Result();

		result.setPage(page);
		result.setPages(pages);
		result.setResults(results);
		result.setList(new ArrayList<Object>(Arrays.asList(events)));
		return result;
	}
}

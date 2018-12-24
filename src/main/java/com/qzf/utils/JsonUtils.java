package com.qzf.utils;

import org.apache.commons.lang3.StringUtils;

import com.fasterxml.jackson.core.JsonFactory;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.core.JsonToken;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ArrayNode;

public class JsonUtils {

	
	/**
	 * 转换Json字符串为JsonArry字符串
	 * @param jsonString {"xx":"xx"} / [{"xx":"xx"}]
	 * @return [{"xx":"xx"}]
	 */
	public static String tranferJsonToJsonArryString(ObjectMapper mapper, String jsonString) {
		String jsonArray = "";
		if(StringUtils.isBlank(jsonString))
			return jsonArray;
		
		JsonFactory factory = mapper.getFactory();
		try {
			JsonParser parser = factory.createParser(jsonString);
			JsonToken token = parser.nextToken();
			if(JsonToken.START_OBJECT.equals(token)) {
				JsonNode jsonNode = mapper.readTree(jsonString);
				ArrayNode arrayNode = mapper.createArrayNode();
				arrayNode.add(jsonNode);
				return mapper.writeValueAsString(arrayNode);
			}else if(JsonToken.START_ARRAY.equals(token)) {
				return jsonString;
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return jsonArray;
	}
}

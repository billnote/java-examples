package com.bill.examples.fastjson;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.Map;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.bill.examples.FileHelper;
import com.bill.examples.xml.XmlPraserExample;

public class JsonExamples {
	private static String basePath = XmlPraserExample.class.getClassLoader().getResource("").getPath() + File.separator;
	
	public static void main(String[] args) throws UnsupportedEncodingException, IOException {
		String sampleJson = basePath + "sample.json";
		String json = new String(FileHelper.readBytes(sampleJson), "UTF-8");
		@SuppressWarnings("unchecked")
		Map<String, JSONObject> reslut = (Map<String, JSONObject>) JSON.parse(json);
		for (String key : reslut.keySet()) {
			System.out.println(key);
			SampleJson resultJson = JSON.toJavaObject(reslut.get(key), SampleJson.class);
			System.out.println(resultJson.getName());
		}
	}
}

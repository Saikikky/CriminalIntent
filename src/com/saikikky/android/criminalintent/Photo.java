package com.saikikky.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private static final String JSON_FILENAME = "filename";
	
	private String mFilename;
	
	//根据给定的文件名创建Photo对象
	public Photo(String filename) {
		mFilename = filename;
	}
	
	//JSON序列化方法 在保存及加载Photo类型的数据时 Crime会用到
	public Photo(JSONObject json) throws JSONException {
		mFilename = json.getString(JSON_FILENAME);
	}
	
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_FILENAME, mFilename);
		return json;
	}
	
	public String getFilename() {
		return mFilename;
	}

}

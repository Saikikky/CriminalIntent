package com.saikikky.android.criminalintent;

import org.json.JSONException;
import org.json.JSONObject;

public class Photo {
	private static final String JSON_FILENAME = "filename";
	
	private String mFilename;
	
	//���ݸ������ļ�������Photo����
	public Photo(String filename) {
		mFilename = filename;
	}
	
	//JSON���л����� �ڱ��漰����Photo���͵�����ʱ Crime���õ�
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

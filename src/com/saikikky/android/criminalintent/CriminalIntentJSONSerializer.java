package com.saikikky.android.criminalintent;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.util.ArrayList;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONTokener;


import android.content.Context;

public class CriminalIntentJSONSerializer {
	
	private Context mContext;
	private String mFilename;
	
	public CriminalIntentJSONSerializer(Context c, String f) {
		mContext = c;
		mFilename = f;
	}
	
	//添加一个从文件中加载crime记录的loadCrimes()方法
	//从文件中读取数据并转换成为JSONObject类型的string 然后再解析为JSONArray 然后解析为ArrayList最后返回ArrayList
	public ArrayList<Crime> loadCrimes() throws IOException, JSONException {
		ArrayList<Crime> crimes = new ArrayList<Crime>();
		BufferedReader reader = null;
		try {
			InputStream in = mContext.openFileInput(mFilename);
			reader = new BufferedReader(new InputStreamReader(in));
			StringBuilder jsonString = new StringBuilder();
			String line = null;
			while ((line = reader.readLine()) != null) {
				jsonString.append(line);
			}
			JSONArray array = (JSONArray) new JSONTokener(jsonString.toString()).nextValue();
			for (int i = 0; i < array.length(); i++) {
				crimes.add(new Crime(array.getJSONObject(i)));
			}
		}catch (FileNotFoundException e) {
			
		} finally {
			if (reader != null) {
				reader.close();
			}
		}
		return crimes;
	}
	public void saveCrimes(ArrayList<Crime> crimes) throws JSONException, IOException {
		//先创建一个JSONArray数组对象 针对数组列表中的所有crime记录调用toJSON()方法 并将结果保存至JSONArray数组中
		JSONArray array = new JSONArray();
		for (Crime c : crimes)
			array.put(c.toJSON());
		
		Writer writer = null;
		try {
			OutputStream out = mContext.openFileOutput(mFilename, Context.MODE_PRIVATE);
			writer = new OutputStreamWriter(out);
			writer.write(array.toString());
		}finally {
			if (writer != null) writer.close();
		}
	}
}

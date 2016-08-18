package com.saikikky.android.criminalintent;

import java.util.Date;
import java.util.UUID;

import org.json.JSONException;
import org.json.JSONObject;

public class Crime {
	
	private static final String JSON_ID = "id";
	private static final String JSON_TITLE = "title";
	private static final String JSON_SOLVED = "solved";
	private static final String JSON_DATE = "date";
	private static final String JSON_PHOTO = "photo";
	private static final String JSON_SUSPECT = "suspect";
	
	//通用唯一识别码
	private UUID mId;
	private String mTitle;
	//Crime发生的时间
	private Date mDate;
	//表示crime是否已经得到处理
	private boolean mSolved;
	private Photo mPhoto;
	private String mSuspect;
	
	
	public Crime() {
		//生成唯一标识符
		mId = UUID.randomUUID();
		//默认的构造方法初始化Data变量
		//设置mDate变量值为当前日期，该日期将作为crime默认的发生时间
		mDate = new Date();
	}
	
	//实现Crime(JSONObject)方法
	public Crime(JSONObject json) throws JSONException {
		mId = UUID.fromString(json.getString(JSON_ID));
		if (json.has(JSON_TITLE)) {
			mTitle = json.getString(JSON_TITLE);
		}
		mSolved = json.getBoolean(JSON_SOLVED);
		mDate = new Date(json.getLong(JSON_DATE));
		if (json.has(JSON_PHOTO))
			mPhoto = new Photo(json.getJSONObject(JSON_PHOTO));
		if (json.has(JSON_SUSPECT))
			mSuspect = json.getString(JSON_SUSPECT);
	}
	
	
	//使用JSONObject类的方法 将Crime对象数据转换为可写入JSON文件的JSONObject对象数据
	public JSONObject toJSON() throws JSONException {
		JSONObject json = new JSONObject();
		json.put(JSON_ID, mId.toString());
		json.put(JSON_TITLE, mTitle);
		json.put(JSON_SOLVED, mSolved);
		json.put(JSON_DATE, mDate.getTime());
		if (mPhoto != null) {
			json.put(JSON_PHOTO, mPhoto.toJSON());
		}
		json.put(JSON_SUSPECT, mSuspect);
		return json;
	}
	@Override
	public String toString() {
		return mTitle;
	}
	
	public Date getDate() {
		return mDate;
	}

	public void setDate(Date date) {
		mDate = date;
	}

	public boolean isSolved() {
		return mSolved;
	}

	public void setSolved(boolean solved) {
		mSolved = solved;
	}

	public String getTitle() {
		return mTitle;
	}

	public void setTitle(String title) {
		mTitle = title;
	}

	public UUID getId() {
		return mId;
	}
	
	public Photo getPhoto() {
		return mPhoto;
	}
	
	public void setPhoto(Photo p) {
		mPhoto = p;
	}
	
	public String getSuspect() {
		return mSuspect;
	}
	
	public void setSuspect(String suspect) {
		mSuspect = suspect;
	}
}

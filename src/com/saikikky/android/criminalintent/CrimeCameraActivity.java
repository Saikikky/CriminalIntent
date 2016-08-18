package com.saikikky.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity {

	//覆盖createFragment()方法返回一个CrimeCammeraFragment
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new CrimeCameraFragment();
	}
	
	//隐藏标题栏的操作必须在activity中做
	//在创建activity视图之前 这几个方法即必须调用
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//隐藏标题栏
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//隐藏状态栏
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}
}

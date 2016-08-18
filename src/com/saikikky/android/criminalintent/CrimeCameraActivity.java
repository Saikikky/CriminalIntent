package com.saikikky.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.Window;
import android.view.WindowManager;

public class CrimeCameraActivity extends SingleFragmentActivity {

	//����createFragment()��������һ��CrimeCammeraFragment
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new CrimeCameraFragment();
	}
	
	//���ر������Ĳ���������activity����
	//�ڴ���activity��ͼ֮ǰ �⼸���������������
	@Override
	public void onCreate(Bundle savedInstanceState) {
		//���ر�����
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		//����״̬��
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN);
		
		super.onCreate(savedInstanceState);
	}
}

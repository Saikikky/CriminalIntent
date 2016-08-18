/*
 * �����ʵ��createFragment()��������һ����Activity�йܵ�fragmentʵ��
 */
package com.saikikky.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity {
	protected abstract Fragment createFragment();
	
	//����SingleFragmentActivity�������� ʹ���������ͨ�����Ǵ˷���������Ҫ�Ĳ���
	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //  setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());
        
        //��������Ǽ������� ��ֱ�Ӽ̳�Activity�ಢ����getFragmentManager()����
        FragmentManager fm = getSupportFragmentManager();
        
        //ʹ��������ԴID��FragmentManager�����ȡfragment
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
        	fragment = createFragment();
        	//�������ύһ��fragment����(��������ӡ��Ƴ������ӡ�������滻fragment�����е�fragment)
        	//FragmentManager.beginTransaction()��������������FragmentTransactionʵ��
        	//����һ���µ�fragment���񣬼���һ����Ӳ�����Ȼ���ύ������
        	fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
	}
}

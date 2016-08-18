/*
 * 子类会实现createFragment()方法返回一个由Activity托管的fragment实例
 */
package com.saikikky.android.criminalintent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;

public abstract class SingleFragmentActivity extends FragmentActivity {
	protected abstract Fragment createFragment();
	
	//增加SingleFragmentActivity类的灵活性 使其子类可以通过覆盖此方法调用需要的布局
	protected int getLayoutResId() {
		return R.layout.activity_fragment;
	}
	@Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    //  setContentView(R.layout.activity_fragment);
        setContentView(getLayoutResId());
        
        //如果不考虑兼容问题 可直接继承Activity类并调用getFragmentManager()方法
        FragmentManager fm = getSupportFragmentManager();
        
        //使用容器资源ID向FragmentManager请求获取fragment
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        if (fragment == null) {
        	fragment = createFragment();
        	//创建并提交一个fragment事务(被用来添加、移除、附加、分离或替换fragment队列中的fragment)
        	//FragmentManager.beginTransaction()方法创建并返回FragmentTransaction实例
        	//创建一个新的fragment事务，加入一个添加操作，然后提交该事务
        	fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
	}
}

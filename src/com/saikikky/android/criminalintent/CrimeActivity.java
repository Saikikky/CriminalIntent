package com.saikikky.android.criminalintent;

import java.util.UUID;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;


public class CrimeActivity extends SingleFragmentActivity {//FragmentActivity 
/*
 * 将代码转入到抽象类中避免代码重复 因为几乎所有的Activity类的代码都类似
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        
        //如果不考虑兼容问题 可直接继承Activity类并调用getFragmentManager()方法
        FragmentManager fm = getSupportFragmentManager();
        
        //使用容器资源ID向FragmentManager请求获取fragment
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        
        //如果本来就存在则返还
        //不存在开启一个新的Fragment事务然后添加提交
        if (fragment == null) {
        	fragment = new CrimeFragment();
        	//创建并提交一个fragment事务(被用来添加、移除、附加、分离或替换fragment队列中的fragment)
        	//FragmentManager.beginTransaction()方法创建并返回FragmentTransaction实例
        	//创建一个新的fragment事务，加入一个添加操作，然后提交该事务
        	fm.beginTransaction().add(R.id.fragmentContainer, fragment).commit();
        }
        
    }
*/
/*
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.crime, menu);
        return true;
    }
*/
   @Override
   protected Fragment createFragment() {
	  // return new CrimeFragment();
	   //从CrimeActivity的intent中获取extra数据信息
	   UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
	   
	   //将数据信息传入CrimeFragment.newInstance(UUID)方法
	   return CrimeFragment.newInstance(crimeId);
   }
}

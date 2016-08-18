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
 * ������ת�뵽�������б�������ظ� ��Ϊ�������е�Activity��Ĵ��붼����
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fragment);
        
        //��������Ǽ������� ��ֱ�Ӽ̳�Activity�ಢ����getFragmentManager()����
        FragmentManager fm = getSupportFragmentManager();
        
        //ʹ��������ԴID��FragmentManager�����ȡfragment
        Fragment fragment = fm.findFragmentById(R.id.fragmentContainer);
        
        //��������ʹ����򷵻�
        //�����ڿ���һ���µ�Fragment����Ȼ������ύ
        if (fragment == null) {
        	fragment = new CrimeFragment();
        	//�������ύһ��fragment����(��������ӡ��Ƴ������ӡ�������滻fragment�����е�fragment)
        	//FragmentManager.beginTransaction()��������������FragmentTransactionʵ��
        	//����һ���µ�fragment���񣬼���һ����Ӳ�����Ȼ���ύ������
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
	   //��CrimeActivity��intent�л�ȡextra������Ϣ
	   UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
	   
	   //��������Ϣ����CrimeFragment.newInstance(UUID)����
	   return CrimeFragment.newInstance(crimeId);
   }
}

package com.saikikky.android.criminalintent;

import java.util.ArrayList;
import java.util.UUID;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;

public class CrimePagerActivity extends FragmentActivity
	implements CrimeFragment.Callbacks {

	private ViewPager mViewPager;
	
	private ArrayList<Crime> mCrimes;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		//实例化ViewPager类并以代码的方式创建内容视图
		mViewPager = new ViewPager(this);
		//在id.xml中创建了资源ID即可创建并显示ViewPager
		mViewPager.setId(R.id.viewPager);
		//并将其设置为内容视图
		setContentView(mViewPager);
		
		//从CrimeLab(crime的ArrayList)中获取数据集
		mCrimes = CrimeLab.get(this).getCrimes();
		
		//获取activity的FragmentManager实例
		FragmentManager fm = getSupportFragmentManager();
		
		//设置ViewPager的pager adapter
		//设置adapter为FragmentStatePagerAdapter的一个匿名实例
		//代理将返回的fragment添加给托管activity并帮助ViewPager找到fragment的视图并一一对应
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			//获得数组的长度
			@Override
			public int getCount() {
				
				return mCrimes.size();
			}
			
			//获取crime数组指定位置的Crime时会返回一个已配置的用于显示指定位置crime信息的crimeFragment
			//用该实例的ID创建并返回一个有效配置的CrimeFragment
			@Override
			public Fragment getItem(int pos) {
				Crime crime = mCrimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			//只关心哪一个页面被选中
			@Override
			public void onPageSelected(int pos) {
				Crime crime = mCrimes.get(pos);
				if (crime.getTitle() != null) {
					setTitle(crime.getTitle());
				}
				
			}
			
			//告知页面将会滑向哪里
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			//告知当前页面所处的行为状态
			//如 正在被用户滑动  页面滑动入位到完全静止以及页面切换完成之后的闲置状态
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//设置初始分页显示项
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i = 0; i < mCrimes.size(); i++) {
			//若匹配 则将当前要显示的列表项设置为Crime在数组中的索引位置
			if (mCrimes.get(i).getId().equals(crimeId)) {
				mViewPager.setCurrentItem(i);
				break;
			}
		}
	}

	@Override
	public void onCrimeUpdated(Crime crime) {
		// TODO Auto-generated method stub
		
	}
}

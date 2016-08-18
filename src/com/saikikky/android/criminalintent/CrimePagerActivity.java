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
		
		//ʵ����ViewPager�ಢ�Դ���ķ�ʽ����������ͼ
		mViewPager = new ViewPager(this);
		//��id.xml�д�������ԴID���ɴ�������ʾViewPager
		mViewPager.setId(R.id.viewPager);
		//����������Ϊ������ͼ
		setContentView(mViewPager);
		
		//��CrimeLab(crime��ArrayList)�л�ȡ���ݼ�
		mCrimes = CrimeLab.get(this).getCrimes();
		
		//��ȡactivity��FragmentManagerʵ��
		FragmentManager fm = getSupportFragmentManager();
		
		//����ViewPager��pager adapter
		//����adapterΪFragmentStatePagerAdapter��һ������ʵ��
		//�������ص�fragment��Ӹ��й�activity������ViewPager�ҵ�fragment����ͼ��һһ��Ӧ
		mViewPager.setAdapter(new FragmentStatePagerAdapter(fm) {
			
			//�������ĳ���
			@Override
			public int getCount() {
				
				return mCrimes.size();
			}
			
			//��ȡcrime����ָ��λ�õ�Crimeʱ�᷵��һ�������õ�������ʾָ��λ��crime��Ϣ��crimeFragment
			//�ø�ʵ����ID����������һ����Ч���õ�CrimeFragment
			@Override
			public Fragment getItem(int pos) {
				Crime crime = mCrimes.get(pos);
				return CrimeFragment.newInstance(crime.getId());
			}
		});
		
		mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
			
			//ֻ������һ��ҳ�汻ѡ��
			@Override
			public void onPageSelected(int pos) {
				Crime crime = mCrimes.get(pos);
				if (crime.getTitle() != null) {
					setTitle(crime.getTitle());
				}
				
			}
			
			//��֪ҳ�潫�Ử������
			@Override
			public void onPageScrolled(int pos, float posOffset, int posOffsetPixels) {
				// TODO Auto-generated method stub
				
			}
			
			//��֪��ǰҳ����������Ϊ״̬
			//�� ���ڱ��û�����  ҳ�滬����λ����ȫ��ֹ�Լ�ҳ���л����֮�������״̬
			@Override
			public void onPageScrollStateChanged(int state) {
				// TODO Auto-generated method stub
				
			}
		});
		
		//���ó�ʼ��ҳ��ʾ��
		UUID crimeId = (UUID)getIntent().getSerializableExtra(CrimeFragment.EXTRA_CRIME_ID);
		for (int i = 0; i < mCrimes.size(); i++) {
			//��ƥ�� �򽫵�ǰҪ��ʾ���б�������ΪCrime�������е�����λ��
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

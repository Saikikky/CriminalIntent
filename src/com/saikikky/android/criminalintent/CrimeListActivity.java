package com.saikikky.android.criminalintent;


import android.content.Intent;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;

public class CrimeListActivity extends SingleFragmentActivity 
	implements CrimeListFragment.Callbacks, CrimeFragment.Callbacks {

	//实现该方法使其能够返回一个新的CrimeListFragment实例
	@Override
	protected Fragment createFragment() {
		// TODO Auto-generated method stub
		return new CrimeListFragment();
	}
	
	@Override
	protected int getLayoutResId() {
		//return R.layout.activity_twopane;
		return R.layout.activity_masterdetail;
	}

	@Override
	public void onCrimeSelected(Crime crime) {
		//此时是手机用户
		if (findViewById(R.id.detailFragmentContainer) == null) {
			//创建一个CrimePagerActivity实例
			Intent i = new Intent(this, CrimePagerActivity.class);
			i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
			startActivity(i);
			//平板用户
		} else {
			//创建fragment事务
			FragmentManager fm = getSupportFragmentManager();
			FragmentTransaction ft = fm.beginTransaction();
			
			Fragment oldDetail = fm.findFragmentById(R.id.detailFragmentContainer);
			Fragment newDetail = CrimeFragment.newInstance(crime.getId());
			
			if (oldDetail != null) {
				ft.remove(oldDetail);
			}
			
			ft.add(R.id.detailFragmentContainer, newDetail);
			ft.commit();
		}
		
	}

	@Override
	public void onCrimeUpdated(Crime crime) {
		FragmentManager fm = getSupportFragmentManager();
		CrimeListFragment listFragment = (CrimeListFragment)fm.findFragmentById(R.id.fragmentContainer);
		listFragment.updateUI();
		
	}

}

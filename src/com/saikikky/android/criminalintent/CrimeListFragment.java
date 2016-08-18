package com.saikikky.android.criminalintent;

import java.util.ArrayList;

import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.ContextMenu;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SurfaceHolder.Callback;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView.MultiChoiceModeListener;
import android.widget.AdapterView.AdapterContextMenuInfo;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.ListView;
import android.widget.TextView;

public class CrimeListFragment extends ListFragment {
//!!	private static final int REQUEST_CRIME = 1;
	private ArrayList<Crime> mCrimes;
	private static final String TAG = "CrimeListFragment";
	private boolean mSubtitleVisible;
	//添加回调接口
	
	//存放实现Callbacks接口的对象(手机用户或者平板用户)
	private Callbacks mCallbacks;
	
	public interface Callbacks {
		void onCrimeSelected(Crime crime);
	}
	
	//将托管activity强制转换成为Callbacks对象并赋值给Callbacks类型变量
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	// 变量清空
	@Override 
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//通知FragmentManager CrimeList 
		setHasOptionsMenu(true);
		//使用Activity.setTitle(int)方法，将显示在操作栏上的标题文字替换为传入的字符串
		//资源中设定的文字
		getActivity().setTitle(R.string.crimes_title);
		
		//先获取CrimeLab单例，再获取其中的crime列表
		//getActivity()方法可以返回托管Activity
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		/*
		//创建一个ArrayAdapter<T>实例并设置其为CrimeListFragment中ListView的adapter
		ArrayAdapter<Crime> adapter = 
				new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		*/
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		//使用它可为CrimeListFragment管理的内置ListView设置adapter
		//adapter是一个控制器对象，从模型层获取数据，并将之提供给ListView显示
		setListAdapter(adapter);
		
		//保留CrimeListFragment并初始化变量
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	//getListAdapter()可以返回设置在ListFragment列表视图上的adapter
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//Crime c = (Crime)(getListAdapter()).getItem(position);
		//从Adapter中获取crime
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
	//	Log.d(TAG, c.getTitle() + "was clicked");
		
		//第二个参数表示第一个参数在哪个包里可以找到
		//启动Activity
		//第一个参数是Context对象 CrimeListFragment是使用getActivity()方法传入它的托管Activity来满足的
		//启动CrimeActivity 现将其更改为启动CrimePagerActivity
		//Intent i = new Intent(getActivity(), CrimeActivity.class);
	//	Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		//将mCrimeId值附加到Intent的extra上 可以告知CrimeFragment应显示的Crime
		//!! startActivityForResult(i, REQUEST_CRIME);
	///	i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
	//	startActivity(i);
		mCallbacks.onCrimeSelected(c);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	//实例化生成选项菜单
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		//传入菜单文件的ID 将文件中定义的菜单项目填充到Menu实例中
		inflater.inflate(R.menu.fragment_crime_list, menu);
		//基于mSubtitleVisible变量的值 正确显示菜单项标题
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}
	
	//相应菜单项选择事件
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			//创建一个新的Crime实例 并将其添加到CrimeLab中
			Crime crime = new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
			//然后启动一个CrimePagerActivity实例 让用户可以编辑新创建的Crime记录
	//		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
	//		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
	//		startActivityForResult(i, 0);
			//一旦新增就必须要更新列表
			((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
			mCallbacks.onCrimeSelected(crime);
			return true;
		case R.id.menu_item_show_subtitle:
			//响应Show Subtitle菜单项单击事件
			//实现菜单项标题与子标题的联动显示
			if (getActivity().getActionBar().getSubtitle() == null) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
				mSubtitleVisible = true;
				item.setTitle(R.string.hide_subtitle);
			} else {
				getActivity().getActionBar().setSubtitle(null);
				mSubtitleVisible = false;
				item.setTitle(R.string.show_subtitle);
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	//创建上下文菜单
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	//监听上下文菜单项选择事件(浮动上下文菜单)
	//ListView是AdapterView的子类
	@Override
	public boolean onContextItemSelected(MenuItem item) {
		AdapterContextMenuInfo info = (AdapterContextMenuInfo)item.getMenuInfo();
		int position = info.position;
		CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
		Crime crime = adapter.getItem(position);
		
		switch (item.getItemId()) {
		case R.id.menu_item_delete_crime:
			CrimeLab.get(getActivity()).deleteCrime(crime);
			adapter.notifyDataSetChanged();
			return true;
		}
		return super.onContextItemSelected(item);
	}
	
	//根据变量mSubtitleVisible的值设置子标题
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		//引用登记ListView  为上下文菜单登记一个视图触发菜单的创建
		//android.R.id.list资源ID获取ListFragment管理的ListView
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			//在老版系统上使用上下文浮动窗口
			registerForContextMenu(listView);
		} else {
			//在高版本的系统上使用上下文操作栏
			//设置可多选
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			
			//设置MultiChoiceModeListener监听器
			listView.setMultiChoiceModeListener(new MultiChoiceModeListener() {
				
				@Override
				public boolean onPrepareActionMode(ActionMode arg0, Menu arg1) {
					// TODO Auto-generated method stub
					return false;
				}
				
				@Override
				public void onDestroyActionMode(ActionMode arg0) {
					// TODO Auto-generated method stub
					
				}
				
				//ActionMode.CallBack接口
				@Override
				public boolean onCreateActionMode(ActionMode mode, Menu menu) {
					// TODO Auto-generated method stub
					MenuInflater inflater = mode.getMenuInflater();
					inflater.inflate(R.menu.crime_list_item_context, menu);
					return true;
				}
				
				@Override
				public boolean onActionItemClicked(ActionMode mode, MenuItem item) {
					switch (item.getItemId()) {
					case R.id.menu_item_delete_crime:
						CrimeAdapter adapter = (CrimeAdapter)getListAdapter();
						CrimeLab crimeLab = CrimeLab.get(getActivity());
						for (int i = adapter.getCount() - 1; i >= 0; i--) {
							if (getListView().isItemChecked(i)) {
								crimeLab.deleteCrime(adapter.getItem(i));
							}
						}
						mode.finish();
						adapter.notifyDataSetChanged();
						return true;
					default:
						return false;
					}
				}
				
				@Override
				public void onItemCheckedStateChanged(ActionMode arg0, int arg1, long arg2,
						boolean arg3) {
					// TODO Auto-generated method stub
					
				}
			});
		}

		return v;
	}
	//覆盖onResume()方法刷新显示列表项
	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	
	//创建一个ArrayAdapter的子类作为CrimeListFragment的内部类
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter(ArrayList<Crime> crimes) {
			//使用超类的构造方法来绑定Crime对象的数组列表 
			//由于不打算使用预定义布局所以传0参
			super(getActivity(), 0, crimes);
		}
		
		//创建并返回定制列表项是在一下ArrayAdapter<T>中实现的
		//覆盖getView方法返回产生于定制布局的视图对象，并填充对应的Crime数据
		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			
			//首先检查传入的视图对象是否是复用对象 如果不是则从定制布局中产生一个新的视图对象
			if (converView == null) {
				converView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			//调用Adapter的getItem(int)方法获取列表中当前position的Crime对象
			Crime c = getItem(position);
			
			//获取Crime对象后 引用视图对象的各个组件并以Crime的数据信息对应配置视图对象
			TextView titleTextView = 
					(TextView)converView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView dataTextView = 
					(TextView)converView.findViewById(R.id.crime_list_item_dataTextView);
			dataTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox = 
					(CheckBox)converView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			
			//把视图对象返回给ListView
			return converView;
		}
	}

	//重新加载刷新CrimeListFragment列表
	public void updateUI() {
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
}

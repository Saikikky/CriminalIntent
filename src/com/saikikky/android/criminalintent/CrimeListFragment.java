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
	//��ӻص��ӿ�
	
	//���ʵ��Callbacks�ӿڵĶ���(�ֻ��û�����ƽ���û�)
	private Callbacks mCallbacks;
	
	public interface Callbacks {
		void onCrimeSelected(Crime crime);
	}
	
	//���й�activityǿ��ת����ΪCallbacks���󲢸�ֵ��Callbacks���ͱ���
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	// �������
	@Override 
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		//֪ͨFragmentManager CrimeList 
		setHasOptionsMenu(true);
		//ʹ��Activity.setTitle(int)����������ʾ�ڲ������ϵı��������滻Ϊ������ַ���
		//��Դ���趨������
		getActivity().setTitle(R.string.crimes_title);
		
		//�Ȼ�ȡCrimeLab�������ٻ�ȡ���е�crime�б�
		//getActivity()�������Է����й�Activity
		mCrimes = CrimeLab.get(getActivity()).getCrimes();
		
		/*
		//����һ��ArrayAdapter<T>ʵ����������ΪCrimeListFragment��ListView��adapter
		ArrayAdapter<Crime> adapter = 
				new ArrayAdapter<Crime>(getActivity(), android.R.layout.simple_list_item_1, mCrimes);
		*/
		CrimeAdapter adapter = new CrimeAdapter(mCrimes);
		//ʹ������ΪCrimeListFragment���������ListView����adapter
		//adapter��һ�����������󣬴�ģ�Ͳ��ȡ���ݣ�����֮�ṩ��ListView��ʾ
		setListAdapter(adapter);
		
		//����CrimeListFragment����ʼ������
		setRetainInstance(true);
		mSubtitleVisible = false;
	}
	
	//getListAdapter()���Է���������ListFragment�б���ͼ�ϵ�adapter
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//Crime c = (Crime)(getListAdapter()).getItem(position);
		//��Adapter�л�ȡcrime
		Crime c = ((CrimeAdapter)getListAdapter()).getItem(position);
	//	Log.d(TAG, c.getTitle() + "was clicked");
		
		//�ڶ���������ʾ��һ���������ĸ���������ҵ�
		//����Activity
		//��һ��������Context���� CrimeListFragment��ʹ��getActivity()�������������й�Activity�������
		//����CrimeActivity �ֽ������Ϊ����CrimePagerActivity
		//Intent i = new Intent(getActivity(), CrimeActivity.class);
	//	Intent i = new Intent(getActivity(), CrimePagerActivity.class);
		//��mCrimeIdֵ���ӵ�Intent��extra�� ���Ը�֪CrimeFragmentӦ��ʾ��Crime
		//!! startActivityForResult(i, REQUEST_CRIME);
	///	i.putExtra(CrimeFragment.EXTRA_CRIME_ID, c.getId());
	//	startActivity(i);
		mCallbacks.onCrimeSelected(c);
	}
	
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	//ʵ��������ѡ��˵�
	@Override
	public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
		super.onCreateOptionsMenu(menu, inflater);
		//����˵��ļ���ID ���ļ��ж���Ĳ˵���Ŀ��䵽Menuʵ����
		inflater.inflate(R.menu.fragment_crime_list, menu);
		//����mSubtitleVisible������ֵ ��ȷ��ʾ�˵������
		MenuItem showSubtitle = menu.findItem(R.id.menu_item_show_subtitle);
		if (mSubtitleVisible && showSubtitle != null) {
			showSubtitle.setTitle(R.string.hide_subtitle);
		}
	}
	
	//��Ӧ�˵���ѡ���¼�
	@TargetApi(11)
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.menu_item_new_crime:
			//����һ���µ�Crimeʵ�� ��������ӵ�CrimeLab��
			Crime crime = new Crime();
			CrimeLab.get(getActivity()).addCrime(crime);
			//Ȼ������һ��CrimePagerActivityʵ�� ���û����Ա༭�´�����Crime��¼
	//		Intent i = new Intent(getActivity(), CrimePagerActivity.class);
	//		i.putExtra(CrimeFragment.EXTRA_CRIME_ID, crime.getId());
	//		startActivityForResult(i, 0);
			//һ�������ͱ���Ҫ�����б�
			((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
			mCallbacks.onCrimeSelected(crime);
			return true;
		case R.id.menu_item_show_subtitle:
			//��ӦShow Subtitle�˵�����¼�
			//ʵ�ֲ˵���������ӱ����������ʾ
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
	
	//���������Ĳ˵�
	@Override
	public void onCreateContextMenu(ContextMenu menu, View v, ContextMenuInfo menuInfo) {
		getActivity().getMenuInflater().inflate(R.menu.crime_list_item_context, menu);
	}
	
	//���������Ĳ˵���ѡ���¼�(���������Ĳ˵�)
	//ListView��AdapterView������
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
	
	//���ݱ���mSubtitleVisible��ֵ�����ӱ���
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent, Bundle savedInstanceState) {
		View v = super.onCreateView(inflater, parent, savedInstanceState);
		
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			if (mSubtitleVisible) {
				getActivity().getActionBar().setSubtitle(R.string.subtitle);
			}
		}
		
		//���õǼ�ListView  Ϊ�����Ĳ˵��Ǽ�һ����ͼ�����˵��Ĵ���
		//android.R.id.list��ԴID��ȡListFragment�����ListView
		ListView listView = (ListView)v.findViewById(android.R.id.list);
		if (Build.VERSION.SDK_INT < Build.VERSION_CODES.HONEYCOMB) {
			//���ϰ�ϵͳ��ʹ�������ĸ�������
			registerForContextMenu(listView);
		} else {
			//�ڸ߰汾��ϵͳ��ʹ�������Ĳ�����
			//���ÿɶ�ѡ
			listView.setChoiceMode(ListView.CHOICE_MODE_MULTIPLE_MODAL);
			
			//����MultiChoiceModeListener������
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
				
				//ActionMode.CallBack�ӿ�
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
	//����onResume()����ˢ����ʾ�б���
	@Override
	public void onResume() {
		super.onResume();
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
	
	
	//����һ��ArrayAdapter��������ΪCrimeListFragment���ڲ���
	private class CrimeAdapter extends ArrayAdapter<Crime> {
		public CrimeAdapter(ArrayList<Crime> crimes) {
			//ʹ�ó���Ĺ��췽������Crime����������б� 
			//���ڲ�����ʹ��Ԥ���岼�����Դ�0��
			super(getActivity(), 0, crimes);
		}
		
		//���������ض����б�������һ��ArrayAdapter<T>��ʵ�ֵ�
		//����getView�������ز����ڶ��Ʋ��ֵ���ͼ���󣬲�����Ӧ��Crime����
		@Override
		public View getView(int position, View converView, ViewGroup parent) {
			
			//���ȼ�鴫�����ͼ�����Ƿ��Ǹ��ö��� ���������Ӷ��Ʋ����в���һ���µ���ͼ����
			if (converView == null) {
				converView = getActivity().getLayoutInflater().inflate(R.layout.list_item_crime, null);
			}
			//����Adapter��getItem(int)������ȡ�б��е�ǰposition��Crime����
			Crime c = getItem(position);
			
			//��ȡCrime����� ������ͼ����ĸ����������Crime��������Ϣ��Ӧ������ͼ����
			TextView titleTextView = 
					(TextView)converView.findViewById(R.id.crime_list_item_titleTextView);
			titleTextView.setText(c.getTitle());
			TextView dataTextView = 
					(TextView)converView.findViewById(R.id.crime_list_item_dataTextView);
			dataTextView.setText(c.getDate().toString());
			CheckBox solvedCheckBox = 
					(CheckBox)converView.findViewById(R.id.crime_list_item_solvedCheckBox);
			solvedCheckBox.setChecked(c.isSolved());
			
			//����ͼ���󷵻ظ�ListView
			return converView;
		}
	}

	//���¼���ˢ��CrimeListFragment�б�
	public void updateUI() {
		((CrimeAdapter)getListAdapter()).notifyDataSetChanged();
	}
}

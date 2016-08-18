package com.saikikky.android.criminalintent;

import java.util.Date;
import java.util.UUID;


import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.drawable.BitmapDrawable;
import android.hardware.Camera;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.NavUtils;
import android.text.Editable;
import android.text.TextWatcher;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;

public class CrimeFragment extends Fragment {

	public static final String TAG = "CrimeFragment";
	
	public static final String EXTRA_CRIME_ID = 
			"com.saikikky.android.criminalintent.crime_id";
	
	public static final int REQUEST_DATE = 0;
	
	//以接收返回值的方法启动CrimeCameraActivity
	public static final int REQUEST_PHOTO = 1;
	
	//新增请求码常量
	public static final int REQUEST_CONTACT = 2;
	
	public static final String DIALOG_DATE = "date";
	
	private static final String DIALOG_IMAGE = "image";
	
	private Crime mCrime;
	
	private EditText mTitleField;
	//显示Crime类中的mDate变量的日期值
	private Button mDateButton;
	//CheckBox需显示Crime是否已经得到处理 
	private CheckBox mSolvedCheckBox;
	
	private ImageButton mPhotoButton;
	
	private ImageView mPhotoView;
	
	private Button mSuspectButton;

	//新增回调接口 关于更新列表视图
	private Callbacks mCallbacks;
	

	public interface Callbacks {
		void onCrimeUpdated(Crime crime);
	}
	
	@Override
	public void onAttach(Activity activity) {
		super.onAttach(activity);
		mCallbacks = (Callbacks)activity;
	}
	
	@Override
	public void onDetach() {
		super.onDetach();
		mCallbacks = null;
	}
	//覆盖Fragment.onCreate(Bundle)方法并新增一个Crime实例成员变量
	//Fragment.onCreate(Bundle)是一个公共方法
	//因为需要被托管Fragment的任何Activity调用
	//Activity.onCreate(Bundle)是一个保护方法

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * 原先是直接使用getActivity方法获取CrimeActivity的intent 返回到CrimeFragment类
		 * 然后再onCreate方法中得到CrimeActivity的intent内的extra信息 再得到Crime对象
		 * 但CrimeFragment无法再用于任何其他的activity
		 * 依赖于CrimeActivity的intent内指定extra的存在
		 */
		
		//从CrimeActivity的intent中获取extra数据信息 此时的CrimeFragment已经不再是封装的
	//	UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		
		//将代码变成从Fragment的argument中获取UUID
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	//	mCrime = new Crime();
		
		//开启选项菜单处理
		setHasOptionsMenu(true);
	}
	
	//通过该方法生成fragment视图的布局，然后将生成的View返回给托管Activity
	//fragment的视图是直接通过调用LayoutInflater inflater()方法并传入布局ID生成的
	//第二个参数是视图的父视图，通常我们需要父视图来正确配置组件
	//第三个参数告知布局生成器是否将生成的视图添加给父视图 此时要通过Activity代码的方法添加视图
	//启用向上导航按钮
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		//启用向上导航按钮
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//为了避免误导用户 当没有父Activity的时候不显示箭头
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		//Activity.findViewById(int)能够在后台自动调用View.findViewById(int)方法
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(
					//CharSequence代表用户输入
					CharSequence c, int start, int before, int count) {
				//最后返回用来设置Crime标题的字符串
				mCrime.setTitle(c.toString());
				
				mCallbacks.onCrimeUpdated(mCrime);
				getActivity().setTitle(mCrime.getTitle());
			}
			public void beforeTextChanged(
					CharSequence c, int start, int count, int after) {
				
			}
			
			public void afterTextChanged(Editable c) {
				
			}
		});
		
		mDateButton = (Button)v.findViewById(R.id.crime_data);
		updateDate();
		//禁用按钮保证它不相应用户的单击事件
	//	mDateButton.setEnabled(false);
		
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			//show用来显示对话框
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
			//	DatePickerFragment dialog = new DatePickerFragment();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				
				//设置目标fragment
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		//修改mSolved的值
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
				
				mCallbacks.onCrimeUpdated(mCrime);
			}
		});
		
		//启动CrimeCameraActivity
		mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				//startActivity(i);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		
		
		//从CrimeFragment中弹出显示图片的对话框
		mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Photo p = mCrime.getPhoto();
				if (p == null)
					return;
				
				//添加给CrimePagerActivity的FragmentManager
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
				//创建一个ImageFragment实例并调用Show方法
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		
		//判断设备是否有相机
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
				Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
				Camera.getNumberOfCameras() > 0;
		if (!hasACamera) {
			mPhotoButton.setEnabled(false);
		}
		
		//发送陋习报告
		Button reportButton = (Button)v.findViewById(R.id.crime_reportButton);
		reportButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//要执行的操作
				Intent i = new Intent(Intent.ACTION_SEND);
				//涉及的数据类型
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				//使用选择器 即每次使用时都会弹出可以使用的应用
				i = Intent.createChooser(i, getString(R.string.send_report));
				startActivity(i);
				
			}
		});
		
		mSuspectButton = (Button)v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Intent.ACTION_PICK操作方法  ContactsContract.Contacts.CONTENT_URI获取联系人位置
				Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, REQUEST_CONTACT);
			}
		});
		
		//如果Crime有预知相关联的联系人 就将其姓名显示在按钮上
		if (mCrime.getSuspect() != null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}
		return v;
	}
	
	//当CrimeActivity创建CrimeFragment时 会调用newInstance(UUID)方法并传入extra中获取的UUID值
	//创建可以接受UUID参数的newInstance(UUID)方法，完成arguments bundle以及
	//fragment实例的创建 最后附加argument给fragment
	public static CrimeFragment newInstance(UUID crimeId) {
		//创建argument
		Bundle args = new Bundle();
		//序列化
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		//创建fragment实例
		CrimeFragment fragment = new CrimeFragment();
		//将argument附加给fragment
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public void updateDate() {
		mDateButton.setText(mCrime.getDate().toString());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		//获取日期
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
		} else if (requestCode == REQUEST_PHOTO) {
			//获取照片 并放置于crime  处理新照片
			String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if (filename != null) {
				//Log.i(TAG, "filename: " + filename);
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				//Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
				//确保用户从CameraActivity返回之后可以看到用户所拍的照片
				mCallbacks.onCrimeUpdated(mCrime);
				showPhoto();
			}
			///获取联系人姓名
		} else if (requestCode == REQUEST_CONTACT) {
			Uri contactUri = data.getData();
			
			//返回所有联系人的显示名字
			String[] queryFields = new String[] {
					ContactsContract.Contacts.DISPLAY_NAME
			};
			
			// 得到一个可用的Cursor
			Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
			
			if (c.getCount() == 0) {
				c.close();
				return;
			}
			
			//放置到第一个记录
			c.moveToFirst();
			String suspect = c.getString(0);
			mCrime.setSuspect(suspect);
			mCallbacks.onCrimeUpdated(mCrime);
			mSuspectButton.setText(suspect);
			c.close();
		}	
	}
	
	//响应应用图标(Home键)菜单项
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//如果有父Activity则调用navigateUpFromSameTask方法导航至父Activity界面
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				NavUtils.navigateUpFromSameTask(getActivity());
			}
			return true;
		default:
			return super.onOptionsItemSelected(item);
		}
	}
	
	@Override
	public void onPause() {
		super.onPause();
		CrimeLab.get(getActivity()).saveCrimes();
	}
	
	//将缩放之后的图片设置给ImageView视图
	private void showPhoto() {
		Photo p = mCrime.getPhoto();
		BitmapDrawable b = null;
		if (p != null) {
			String path = getActivity()
					.getFileStreamPath(p.getFilename()).getAbsolutePath();
			b = PictureUtils.getScaledDrawable(getActivity(), path);
		}
		mPhotoView.setImageDrawable(b);
	}
	
	//加载图片 只要CrimeFragment的视图出现在屏幕上就调用showPhoto方法
	@Override 
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	//卸载图片
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}

	//创建四段字符串信息 并返回拼接完整的陋习报告文本信息
	private String getCrimeReport() {
		String solvedString = null;
		if (mCrime.isSolved()) {
			solvedString = getString(R.string.crime_report_solved);
		} else {
			solvedString = getString(R.string.crime_report_unsolved);
		}
		
		String dateFormat = "EEE, MMM dd";
		String dateString = DateFormat.format(dateFormat, mCrime.getDate()).toString();
		
		String suspect = mCrime.getSuspect();
		if (suspect == null) {
			suspect = getString(R.string.crime_report_no_suspect);
		} else {
			suspect = getString(R.string.crime_report_suspect, suspect);
		}
		
		String report = getString(R.string.crime_report, mCrime.getTitle(), dateString, solvedString, suspect);
		
		return report;
	}

}



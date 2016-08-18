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
	
	//�Խ��շ���ֵ�ķ�������CrimeCameraActivity
	public static final int REQUEST_PHOTO = 1;
	
	//���������볣��
	public static final int REQUEST_CONTACT = 2;
	
	public static final String DIALOG_DATE = "date";
	
	private static final String DIALOG_IMAGE = "image";
	
	private Crime mCrime;
	
	private EditText mTitleField;
	//��ʾCrime���е�mDate����������ֵ
	private Button mDateButton;
	//CheckBox����ʾCrime�Ƿ��Ѿ��õ����� 
	private CheckBox mSolvedCheckBox;
	
	private ImageButton mPhotoButton;
	
	private ImageView mPhotoView;
	
	private Button mSuspectButton;

	//�����ص��ӿ� ���ڸ����б���ͼ
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
	//����Fragment.onCreate(Bundle)����������һ��Crimeʵ����Ա����
	//Fragment.onCreate(Bundle)��һ����������
	//��Ϊ��Ҫ���й�Fragment���κ�Activity����
	//Activity.onCreate(Bundle)��һ����������

	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		/*
		 * ԭ����ֱ��ʹ��getActivity������ȡCrimeActivity��intent ���ص�CrimeFragment��
		 * Ȼ����onCreate�����еõ�CrimeActivity��intent�ڵ�extra��Ϣ �ٵõ�Crime����
		 * ��CrimeFragment�޷��������κ�������activity
		 * ������CrimeActivity��intent��ָ��extra�Ĵ���
		 */
		
		//��CrimeActivity��intent�л�ȡextra������Ϣ ��ʱ��CrimeFragment�Ѿ������Ƿ�װ��
	//	UUID crimeId = (UUID)getActivity().getIntent().getSerializableExtra(EXTRA_CRIME_ID);
		
		//�������ɴ�Fragment��argument�л�ȡUUID
		UUID crimeId = (UUID)getArguments().getSerializable(EXTRA_CRIME_ID);
		mCrime = CrimeLab.get(getActivity()).getCrime(crimeId);
	//	mCrime = new Crime();
		
		//����ѡ��˵�����
		setHasOptionsMenu(true);
	}
	
	//ͨ���÷�������fragment��ͼ�Ĳ��֣�Ȼ�����ɵ�View���ظ��й�Activity
	//fragment����ͼ��ֱ��ͨ������LayoutInflater inflater()���������벼��ID���ɵ�
	//�ڶ�����������ͼ�ĸ���ͼ��ͨ��������Ҫ����ͼ����ȷ�������
	//������������֪�����������Ƿ����ɵ���ͼ��Ӹ�����ͼ ��ʱҪͨ��Activity����ķ��������ͼ
	//�������ϵ�����ť
	@TargetApi(11)
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup parent,
			Bundle savedInstanceState) {
		View v = inflater.inflate(R.layout.fragment_crime, parent, false);
		
		//�������ϵ�����ť
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.HONEYCOMB) {
			//Ϊ�˱������û� ��û�и�Activity��ʱ����ʾ��ͷ
			if (NavUtils.getParentActivityName(getActivity()) != null) {
				getActivity().getActionBar().setDisplayHomeAsUpEnabled(true);
			}
		}
		
		//Activity.findViewById(int)�ܹ��ں�̨�Զ�����View.findViewById(int)����
		mTitleField = (EditText)v.findViewById(R.id.crime_title);
		mTitleField.setText(mCrime.getTitle());
		mTitleField.addTextChangedListener(new TextWatcher() {
			public void onTextChanged(
					//CharSequence�����û�����
					CharSequence c, int start, int before, int count) {
				//��󷵻���������Crime������ַ���
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
		//���ð�ť��֤������Ӧ�û��ĵ����¼�
	//	mDateButton.setEnabled(false);
		
		mDateButton.setOnClickListener(new View.OnClickListener() {
			
			//show������ʾ�Ի���
			@Override
			public void onClick(View v) {
				FragmentManager fm = getActivity().getSupportFragmentManager();
			//	DatePickerFragment dialog = new DatePickerFragment();
				DatePickerFragment dialog = DatePickerFragment.newInstance(mCrime.getDate());
				
				//����Ŀ��fragment
				dialog.setTargetFragment(CrimeFragment.this, REQUEST_DATE);
				
				dialog.show(fm, DIALOG_DATE);
			}
		});
		
		//�޸�mSolved��ֵ
		mSolvedCheckBox = (CheckBox)v.findViewById(R.id.crime_solved);
		mSolvedCheckBox.setChecked(mCrime.isSolved());
		mSolvedCheckBox.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				mCrime.setSolved(isChecked);
				
				mCallbacks.onCrimeUpdated(mCrime);
			}
		});
		
		//����CrimeCameraActivity
		mPhotoButton = (ImageButton)v.findViewById(R.id.crime_imageButton);
		mPhotoButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Intent i = new Intent(getActivity(), CrimeCameraActivity.class);
				//startActivity(i);
				startActivityForResult(i, REQUEST_PHOTO);
			}
		});
		
		
		//��CrimeFragment�е�����ʾͼƬ�ĶԻ���
		mPhotoView = (ImageView)v.findViewById(R.id.crime_imageView);
		mPhotoView.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				Photo p = mCrime.getPhoto();
				if (p == null)
					return;
				
				//��Ӹ�CrimePagerActivity��FragmentManager
				FragmentManager fm = getActivity().getSupportFragmentManager();
				String path = getActivity().getFileStreamPath(p.getFilename()).getAbsolutePath();
				//����һ��ImageFragmentʵ��������Show����
				ImageFragment.newInstance(path).show(fm, DIALOG_IMAGE);
			}
		});
		
		
		//�ж��豸�Ƿ������
		PackageManager pm = getActivity().getPackageManager();
		boolean hasACamera = pm.hasSystemFeature(PackageManager.FEATURE_CAMERA) ||
				pm.hasSystemFeature(PackageManager.FEATURE_CAMERA_FRONT) ||
				Build.VERSION.SDK_INT < Build.VERSION_CODES.GINGERBREAD ||
				Camera.getNumberOfCameras() > 0;
		if (!hasACamera) {
			mPhotoButton.setEnabled(false);
		}
		
		//����ªϰ����
		Button reportButton = (Button)v.findViewById(R.id.crime_reportButton);
		reportButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Ҫִ�еĲ���
				Intent i = new Intent(Intent.ACTION_SEND);
				//�漰����������
				i.setType("text/plain");
				i.putExtra(Intent.EXTRA_TEXT, getCrimeReport());
				i.putExtra(Intent.EXTRA_SUBJECT, getString(R.string.crime_report_subject));
				//ʹ��ѡ���� ��ÿ��ʹ��ʱ���ᵯ������ʹ�õ�Ӧ��
				i = Intent.createChooser(i, getString(R.string.send_report));
				startActivity(i);
				
			}
		});
		
		mSuspectButton = (Button)v.findViewById(R.id.crime_suspectButton);
		mSuspectButton.setOnClickListener(new View.OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				//Intent.ACTION_PICK��������  ContactsContract.Contacts.CONTENT_URI��ȡ��ϵ��λ��
				Intent i = new Intent(Intent.ACTION_PICK, ContactsContract.Contacts.CONTENT_URI);
				startActivityForResult(i, REQUEST_CONTACT);
			}
		});
		
		//���Crime��Ԥ֪���������ϵ�� �ͽ���������ʾ�ڰ�ť��
		if (mCrime.getSuspect() != null) {
			mSuspectButton.setText(mCrime.getSuspect());
		}
		return v;
	}
	
	//��CrimeActivity����CrimeFragmentʱ �����newInstance(UUID)����������extra�л�ȡ��UUIDֵ
	//�������Խ���UUID������newInstance(UUID)���������arguments bundle�Լ�
	//fragmentʵ���Ĵ��� ��󸽼�argument��fragment
	public static CrimeFragment newInstance(UUID crimeId) {
		//����argument
		Bundle args = new Bundle();
		//���л�
		args.putSerializable(EXTRA_CRIME_ID, crimeId);
		//����fragmentʵ��
		CrimeFragment fragment = new CrimeFragment();
		//��argument���Ӹ�fragment
		fragment.setArguments(args);
		
		return fragment;
	}
	
	public void updateDate() {
		mDateButton.setText(mCrime.getDate().toString());
	}
	
	@Override
	public void onActivityResult(int requestCode, int resultCode, Intent data) {
		if (resultCode != Activity.RESULT_OK) return;
		//��ȡ����
		if (requestCode == REQUEST_DATE) {
			Date date = (Date)data.getSerializableExtra(DatePickerFragment.EXTRA_DATE);
			mCrime.setDate(date);
			mCallbacks.onCrimeUpdated(mCrime);
			updateDate();
		} else if (requestCode == REQUEST_PHOTO) {
			//��ȡ��Ƭ ��������crime  ��������Ƭ
			String filename = data.getStringExtra(CrimeCameraFragment.EXTRA_PHOTO_FILENAME);
			if (filename != null) {
				//Log.i(TAG, "filename: " + filename);
				Photo p = new Photo(filename);
				mCrime.setPhoto(p);
				//Log.i(TAG, "Crime: " + mCrime.getTitle() + " has a photo");
				//ȷ���û���CameraActivity����֮����Կ����û����ĵ���Ƭ
				mCallbacks.onCrimeUpdated(mCrime);
				showPhoto();
			}
			///��ȡ��ϵ������
		} else if (requestCode == REQUEST_CONTACT) {
			Uri contactUri = data.getData();
			
			//����������ϵ�˵���ʾ����
			String[] queryFields = new String[] {
					ContactsContract.Contacts.DISPLAY_NAME
			};
			
			// �õ�һ�����õ�Cursor
			Cursor c = getActivity().getContentResolver().query(contactUri, queryFields, null, null, null);
			
			if (c.getCount() == 0) {
				c.close();
				return;
			}
			
			//���õ���һ����¼
			c.moveToFirst();
			String suspect = c.getString(0);
			mCrime.setSuspect(suspect);
			mCallbacks.onCrimeUpdated(mCrime);
			mSuspectButton.setText(suspect);
			c.close();
		}	
	}
	
	//��ӦӦ��ͼ��(Home��)�˵���
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case android.R.id.home:
			//����и�Activity�����navigateUpFromSameTask������������Activity����
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
	
	//������֮���ͼƬ���ø�ImageView��ͼ
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
	
	//����ͼƬ ֻҪCrimeFragment����ͼ��������Ļ�Ͼ͵���showPhoto����
	@Override 
	public void onStart() {
		super.onStart();
		showPhoto();
	}
	
	//ж��ͼƬ
	@Override
	public void onStop() {
		super.onStop();
		PictureUtils.cleanImageView(mPhotoView);
	}

	//�����Ķ��ַ�����Ϣ ������ƴ��������ªϰ�����ı���Ϣ
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



package com.coreform.android.open.mapfragmentexample;

import com.coreform.android.open.tandemactivities.TandemActivitiesTabFragmentActivity;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Example03Tab1NonMapFragment extends Fragment {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example03Tab1NonMapFragment";
	
	private LinearLayout mFragmentLinearLayout;
	private Button mShowMapFragmentButton;
	
	private Handler mHandler;
	
	@Override
	public void onAttach(Activity activity) {
		if(DEBUG) Log.d(TAG, ".onAttach()...");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreate()...");
		super.onCreate(savedInstanceState);
		
		mHandler = new Handler();
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreateView()...");
		super.onCreateView(inflater, container, savedInstanceState);
		if(DEBUG) Log.d(TAG, "...this fragment's Activity: "+this.getActivity().getClass().getSimpleName());
		View view = inflater.inflate(R.layout.fragment_example03_nonmap, container, false);
        mFragmentLinearLayout = (LinearLayout) view;
        return view;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onActivityCreated()...");
        super.onActivityCreated(savedInstanceState);
        
		//forcibly set the Tab that this Fragment exists within (because FragmentTransactions do not handle changing Tab for us)
		//((TandemActivitiesTabFragmentActivity)this.getActivity()).changeToTab(((TandemActivitiesTabFragmentActivity)this.getActivity()).getTabTag_map());
		//another Inazaruk trick found at: http://stackoverflow.com/questions/7700226/display-fragment-viewpager-within-a-fragment/7700305#7700305
        /*
        mHandler.post(new Runnable() {
			@Override
			public void run() {
				((TandemActivitiesTabFragmentActivity)Example03Tab1NonMapFragment.this.getActivity()).changeToTab(((TandemActivitiesTabFragmentActivity)Example03Tab1NonMapFragment.this.getActivity()).getTabTag_map());
			}
		});
		*/
	}
	
	@Override
	public void onStart() {
		if(DEBUG) Log.d(TAG, ".onStart()...");
		super.onStart();
		
		//TODO you should setup Views here (e.g. use findViewById() here)
		Button mShowMapFragmentButton = (Button) mFragmentLinearLayout.findViewById(R.id.showMapFragmentButton);
		
		mShowMapFragmentButton.setOnClickListener(new ShowMapFragmentButtonOnClickListener());
	}
	
	@Override
	public void onResume() {
		if(DEBUG) Log.d(TAG, ".onResume()...");
		super.onResume();
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
		if(DEBUG) Log.d(TAG, ".onSaveInstanceState()...");
        super.onSaveInstanceState(outState);
        
    }
	
	@Override
	public void onPause() {
		if(DEBUG) Log.d(TAG, ".onAttach()...");
		super.onPause();
	}
	
	@Override
	public void onStop() {
		if(DEBUG) Log.d(TAG, ".onStop()...");
		super.onStop();
	}
	
	@Override
	public void onDestroyView() {
		if(DEBUG) Log.d(TAG, ".onDestroyView()...");
		super.onDestroyView();
	}
	
	@Override
	public void onDestroy() {
		if(DEBUG) Log.d(TAG, ".onDestroy()...");
		super.onDestroy();
	}
	
	@Override
	public void onDetach() {
		if(DEBUG) Log.d(TAG, ".onDetach()...");
		super.onDetach();
	}
	
	/*
	 * METHODS
	 */
	
	/*
	 * INNER CLASSES
	 */
	
	private class ShowMapFragmentButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(DEBUG) Log.d(TAG, "...MOVING TO FRAGMENT: Example03Tab1MapFragment...");
			//define next fragment and pass it startup values via Bundle
			Fragment nextFragment = new Example03Tab1MapFragment();
			Bundle nextFragmentArgumentsBundle = new Bundle();
			nextFragmentArgumentsBundle.putString("targetTabTag", ((TandemActivitiesTabFragmentActivity)Example03Tab1NonMapFragment.this.getActivity()).getTabTag_map());
			nextFragment.setArguments(nextFragmentArgumentsBundle);
			//start fragment transaction, add current fragment to backStack, replace current fragment with nextFragment
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.tabfragmentcontent, nextFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}
}

package com.coreform.android.open.mapfragmentexample;

import android.app.Activity;
import android.os.Build;
import android.os.Bundle;
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

public class Example02NonMapFragment extends Fragment {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example02NonMapFragment";
	
	private LinearLayout mFragmentLinearLayout;
	private Button mShowMapFragmentButton;
	
	private boolean mIsInsideTopFragmentContainer = true;
	
	@Override
	public void onAttach(Activity activity) {
		if(DEBUG) Log.d(TAG, ".onAttach()...");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreate()...");
		super.onCreate(savedInstanceState);
		
		//grab any values passed into this Fragment via a Bundle.
		//in this case, isInsideTopFragmentContainer specifies which FragmentContainer this Fragment exists within,
		//so that the appropriate ShowNextFragmentButtonOnClickListener.onClick() result can be achieved.
		Bundle fragmentArgumentsBundle = this.getArguments();
		if(fragmentArgumentsBundle != null) {
			mIsInsideTopFragmentContainer = fragmentArgumentsBundle.getBoolean("isInsideTopFragmentContainer", mIsInsideTopFragmentContainer);
		}
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreateView()...");
		super.onCreateView(inflater, container, savedInstanceState);
		if(DEBUG) Log.d(TAG, "...this fragment's Activity: "+this.getActivity().getClass().getSimpleName());
		View view = inflater.inflate(R.layout.fragment_example02_nonmap, container, false);
        mFragmentLinearLayout = (LinearLayout) view;
        return view;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onActivityCreated()...");
        super.onActivityCreated(savedInstanceState);
	}
	
	@Override
	public void onStart() {
		if(DEBUG) Log.d(TAG, ".onStart()...");
		super.onStart();
		
		//TODO you should setup Views here (e.g. use findViewById() here)
		Button mShowMapFragmentButton = (Button) mFragmentLinearLayout.findViewById(R.id.showMapFragmentButton);
		
		mShowMapFragmentButton.setOnClickListener(new ShowNextFragmentButtonOnClickListener());
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
	
	private class ShowNextFragmentButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(mIsInsideTopFragmentContainer) {
				if(DEBUG) Log.d(TAG, "...MOVING TO FRAGMENT: Example02MapFragment...");
				//define next fragment and pass it startup values via Bundle
				Fragment nextFragment = new Example02MapFragment();
				Bundle nextFragmentArgumentsBundle = new Bundle();
				nextFragmentArgumentsBundle.putBoolean("isInsideTopFragmentContainer", true);
				nextFragmentArgumentsBundle.putInt("mapFragmentResource", R.layout.fragment_example01_map);	//will be passed through to MapActivity
				nextFragment.setArguments(nextFragmentArgumentsBundle);
				//start fragment transaction, add current fragment to backStack, replace current fragment with nextFragment
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.fragmenttopcontent, nextFragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			} else {
				if(DEBUG) Log.d(TAG, "...MOVING TO PREVIOUS FRAGMENT...");
				//move back to previous fragment
				getFragmentManager().popBackStack();
			}
		}
	}
}

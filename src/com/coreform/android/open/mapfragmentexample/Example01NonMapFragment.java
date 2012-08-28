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

public class Example01NonMapFragment extends Fragment {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example01NonMapFragment";
	
	private LinearLayout mFragmentLinearLayout;
	private Button mShowMapFragmentButton;
	
	@Override
	public void onAttach(Activity activity) {
		if(DEBUG) Log.d(TAG, ".onAttach()...");
		super.onAttach(activity);
	}
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreate()...");
		super.onCreate(savedInstanceState);
	}
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreateView()...");
		super.onCreateView(inflater, container, savedInstanceState);
		if(DEBUG) Log.d(TAG, "...this fragment's Activity: "+this.getActivity().getClass().getSimpleName());
		View view = inflater.inflate(R.layout.fragment_example01_nonmap, container, false);
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
			if(DEBUG) Log.d(TAG, "...MOVING TO FRAGMENT: Example01MapFragment...");
			//define next fragment and pass it startup values via Bundle
			Fragment nextFragment = new Example01MapFragment();
			Bundle mapActivityStartupBundle = new Bundle();
			mapActivityStartupBundle.putInt("mapFragmentResource", R.layout.fragment_example01_map);
			nextFragment.setArguments(mapActivityStartupBundle);
			//start fragment transaction, add current fragment to backStack, replace current fragment with nextFragment
			FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
			fragmentTransaction.replace(R.id.fragmentcontent, nextFragment);
			fragmentTransaction.addToBackStack(null);
			fragmentTransaction.commit();
		}
	}
}

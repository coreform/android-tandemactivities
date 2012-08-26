package com.coreform.android.open.mapfragmentexample;

import com.coreform.android.open.tandemactivities.TandemActivitiesActivityInterface;
import com.coreform.android.open.tandemactivities.TandemActivitiesMapActivity;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

public class Example02MapFragment extends Fragment {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example02MapFragment";
	
	private static final String KEY_STATE_BUNDLE = "localActivityManagerState";
	
	@SuppressWarnings("deprecation")
	private LocalActivityManager mLocalActivityManager;	//necessary to get MapActivity invoked within the Fragment lifecycle
	private Window mWindow;
	
	private LinearLayout mFragmentLinearLayout;
	private Button mShowNonMapFragmentButton;
	private ViewGroup mContainer;
	private View mFragmentBaseView;
	private MapView mMapView;
	
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
		
		Bundle state = null;
        if (savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }
        
        //setup our LocalActivityManager, which will invoke the MapActivity in tandem to this Fragment's FragmentActivity
		mLocalActivityManager = new LocalActivityManager(getActivity(), true);
		mLocalActivityManager.dispatchCreate(state);
		Intent mapActivityIntent = new Intent(getActivity(), TandemActivitiesMapActivity.class);
		mWindow = mLocalActivityManager.startActivity("mapActivity", mapActivityIntent);
		
		//ensure this FragmentActivity takes key events, rather than the MapActivity
		//getActivity().takeKeyEvents(true);	//note: doesn't work when MapActivity is in foreground
		((TandemActivitiesActivityInterface)mLocalActivityManager.getActivity("mapActivity")).setContainingFragmentActivity(getActivity());
		
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
		//note that we only setup a containing LinearLayout here and populate it in onActivityCreated()
		mContainer = new LinearLayout(getActivity());
		return mContainer;
    }
	
	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onActivityCreated()...");
        super.onActivityCreated(savedInstanceState);
		mFragmentBaseView = mWindow.getDecorView();
		mFragmentBaseView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
		mFragmentBaseView.setVisibility(View.VISIBLE);
		mFragmentBaseView.setFocusableInTouchMode(true);
		((ViewGroup)mFragmentBaseView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		if(mFragmentBaseView.getParent() != null) {
			((ViewGroup)mFragmentBaseView.getParent()).removeView(mFragmentBaseView);
		}
		mContainer.addView(mFragmentBaseView);
		mFragmentLinearLayout = (LinearLayout) mFragmentBaseView.findViewById(R.id.baseLinearLayout);
		mFragmentBaseView.requestFocus();
		mFragmentBaseView.invalidate();
	}
	
	@Override
	public void onStart() {
		if(DEBUG) Log.d(TAG, ".onStart()...");
		super.onStart();
		
		//TODO you should setup Views here (e.g. use findViewById() here)
		mMapView = (MapView) mFragmentLinearLayout.findViewById(R.id.mapview);
		mShowNonMapFragmentButton = (Button) mFragmentLinearLayout.findViewById(R.id.showNonMapFragmentButton);
		
		mMapView.setBuiltInZoomControls(true);
		mMapView.displayZoomControls(true);
		mShowNonMapFragmentButton.setOnClickListener(new ShowNextFragmentButtonOnClickListener());
	}
	
	@Override
	public void onResume() {
		if(DEBUG) Log.d(TAG, ".onResume()...");
		super.onResume();
		mLocalActivityManager.dispatchResume();
		mFragmentBaseView = mWindow.getDecorView();
		mFragmentBaseView.setVisibility(View.VISIBLE);
		mFragmentBaseView.setFocusableInTouchMode(true);
		((ViewGroup)mFragmentBaseView).setDescendantFocusability(ViewGroup.FOCUS_AFTER_DESCENDANTS);
		if(mFragmentBaseView.getParent() != null) {
			((ViewGroup)mFragmentBaseView.getParent()).removeView(mFragmentBaseView);
		}
		mContainer.addView(mFragmentBaseView);
		mFragmentLinearLayout = (LinearLayout) mFragmentBaseView.findViewById(R.id.baseLinearLayout);
		mFragmentBaseView.requestFocus();
		mFragmentBaseView.invalidate();
		
		
	}
	
	@Override
    public void onSaveInstanceState(Bundle outState) {
		if(DEBUG) Log.d(TAG, ".onSaveInstanceState()...");
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE, mLocalActivityManager.saveInstanceState());
    }
	
	@Override
	public void onPause() {
		if(DEBUG) Log.d(TAG, ".onAttach()...");
		mLocalActivityManager.dispatchPause(getActivity().isFinishing());
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
		mLocalActivityManager.destroyActivity("mapActivity", true);
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
				if(DEBUG) Log.d(TAG, "...MOVING TO PREVIOUS FRAGMENT...");
				//move back to previous fragment
				getFragmentManager().popBackStack();
			} else {
				if(DEBUG) Log.d(TAG, "...MOVING TO FRAGMENT: Example02NonMapFragment...");
				//define next fragment and pass it startup values via Bundle
				Fragment nextFragment = new Example02NonMapFragment();
				Bundle nextFragmentArgumentsBundle = new Bundle();
				nextFragmentArgumentsBundle.putBoolean("isInsideTopFragmentContainer", false);
				nextFragment.setArguments(nextFragmentArgumentsBundle);
				//start fragment transaction, add current fragment to backStack, replace current fragment with nextFragment
				FragmentTransaction fragmentTransaction = getFragmentManager().beginTransaction();
				fragmentTransaction.replace(R.id.fragmentbottomcontent, nextFragment);
				fragmentTransaction.addToBackStack(null);
				fragmentTransaction.commit();
			}
		}
	}
}

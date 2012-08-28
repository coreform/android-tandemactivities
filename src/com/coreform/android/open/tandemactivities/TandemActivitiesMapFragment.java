package com.coreform.android.open.tandemactivities;

import com.coreform.android.open.mapfragmentexample.R;
import com.coreform.android.open.mapfragmentexample.R.id;
import com.google.android.maps.MapView;

import android.app.Activity;
import android.app.LocalActivityManager;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.LinearLayout;

public class TandemActivitiesMapFragment extends Fragment implements TandemActivitiesMapFragmentInterface {
	private static final boolean DEBUG = true;
	private static final String TAG = "TandemActivitiesMapFragment";
	
	private static final String KEY_STATE_BUNDLE = "localActivityManagerState";
	
	@SuppressWarnings("deprecation")
	private LocalActivityManager mLocalActivityManager;	//necessary to get MapActivity invoked within the Fragment lifecycle
	private Window mWindow;
	
	private LinearLayout mFragmentLinearLayout;
	private ViewGroup mContainer;
	private View mFragmentBaseView;
	
	@Override
	public void onAttach(Activity activity) {
		if(DEBUG) Log.d(TAG, ".onAttach()...");
		super.onAttach(activity);
	}
	
	@SuppressWarnings("deprecation")
	@Override
	public void onCreate(Bundle savedInstanceState) {
		if(DEBUG) Log.d(TAG, ".onCreate()...");
		super.onCreate(savedInstanceState);
		
		Bundle state = null;
        if (savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }
		mLocalActivityManager = new LocalActivityManager(getActivity(), true);
		mLocalActivityManager.dispatchCreate(state);
		//pass the mapActivityStartupBundle along to the MapActivity
		Intent mapActivityIntent = new Intent(getActivity(), TandemActivitiesMapActivity.class);
		Bundle mapActivityStartupBundle = this.getArguments();
		mapActivityIntent.putExtras(mapActivityStartupBundle);
		mWindow = mLocalActivityManager.startActivity("mapActivity", mapActivityIntent);
		
		//ensure this FragmentActivity takes key events, rather than the MapActivity
		//getActivity().takeKeyEvents(true);	//note: doesn't work when MapActivity is in foreground
		((TandemActivitiesActivityInterface)mLocalActivityManager.getActivity("mapActivity")).setContainingFragmentActivity(getActivity());
		//TODo try using .requestFocus(View.FOCUS_FORWARD); and return mTabContent.dispatchKeyEvent(event); instead of ^ perhaps??
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
        //TODO repeating this, why?
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
	
	@SuppressWarnings("deprecation")
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
	
	@SuppressWarnings("deprecation")
	@Override
    public void onSaveInstanceState(Bundle outState) {
		if(DEBUG) Log.d(TAG, ".onSaveInstanceState()...");
        super.onSaveInstanceState(outState);
        outState.putBundle(KEY_STATE_BUNDLE, mLocalActivityManager.saveInstanceState());
    }
	
	@SuppressWarnings("deprecation")
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
	
	@SuppressWarnings("deprecation")
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
	
	public View getFragmentLayout() {
		return mFragmentLinearLayout;
	}
	
	/*
	 * INNER CLASSES
	 */
	
}

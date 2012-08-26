package com.coreform.android.open.mapfragmentexample;

import com.coreform.android.open.tandemactivities.TandemActivitiesMapFragment;
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

public class Example01MapFragment extends TandemActivitiesMapFragment {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example01MapFragment";
	
	private LinearLayout mFragmentLinearLayout;
	private Button mShowNonMapFragmentButton;
	private MapView mMapView;
	
	@Override
	public void onStart() {
		if(DEBUG) Log.d(TAG, ".onStart()...");
		super.onStart();
		
		//get the fragment's LinearLayout that has been created by this extended TandemActivitiesMapFragment
		mFragmentLinearLayout = (LinearLayout) super.getFragmentLayout();
		
		//TODO you should setup Views here (e.g. use findViewById() here)
		mMapView = (MapView) mFragmentLinearLayout.findViewById(R.id.mapview);
		mShowNonMapFragmentButton = (Button) mFragmentLinearLayout.findViewById(R.id.showNonMapFragmentButton);
		
		mMapView.setBuiltInZoomControls(true);
		mMapView.displayZoomControls(true);
		mShowNonMapFragmentButton.setOnClickListener(new ShowNonMapFragmentButtonOnClickListener());
	}
	
	/*
	 * METHODS
	 */
	
	/*
	 * INNER CLASSES
	 */
	
	private class ShowNonMapFragmentButtonOnClickListener implements OnClickListener {
		@Override
		public void onClick(View v) {
			if(DEBUG) Log.d(TAG, "...MOVING TO PREVIOUS FRAGMENT...");
			//move back to previous fragment
			getFragmentManager().popBackStack();
		}
	}
}

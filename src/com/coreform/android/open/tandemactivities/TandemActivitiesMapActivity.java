package com.coreform.android.open.tandemactivities;

import com.coreform.android.open.mapfragmentexample.R;
import com.coreform.android.open.mapfragmentexample.R.layout;
import com.google.android.maps.MapActivity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;

public class TandemActivitiesMapActivity extends MapActivity implements TandemActivitiesActivityInterface {
	private static final boolean DEBUG = true;
	private static final String TAG = "TandemActivitiesMapActivity";
    
	private FragmentActivity mContainingFragmentActivity;
	
	private FragmentManager mSupportFragmentManager;
	
	private Button mListButton;
	
    @Override
    protected void onCreate(Bundle icicle) {
    	if(DEBUG) Log.d(TAG, ".onCreate()...");
        super.onCreate(icicle);
        //TODO setup means to pass the desired content view within activityStartupBundle
        //TODO this is currently causing crash!
        //extract desired content view resource from activityStartupBundle
        Bundle activityStartupBundle = this.getIntent().getExtras();
        int mapFragmentResource = activityStartupBundle.getInt("mapFragmentResource");
        setContentView(mapFragmentResource);	//R.layout.fragment_example01_map
    }
    
    @Override
    protected boolean isRouteDisplayed() {
        return false;
    }
    
    @Override
	protected void onResume() {
		if(DEBUG) Log.d(TAG, ".onResume()...");
		super.onResume();
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
		if(DEBUG) Log.d(TAG, ".onSaveInstanceState()...");
        super.onSaveInstanceState(outState);
    }
	
	@Override
	protected void onPause() {
		if(DEBUG) Log.d(TAG, ".onPause()...");
		super.onPause();
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		switch(keyCode) {
		case KeyEvent.KEYCODE_BACK:
			if(DEBUG) Log.d(TAG, "...BACK KeyEvent...");
			//catch this and pass to the containing FragmentActivity
			if(mContainingFragmentActivity != null) {
				if(((TandemActivitiesFragmentActivityInterface)mContainingFragmentActivity).handleBackKeyEvent()) {
					return true;
				}
			} else {
				//abort this BACK press (note: depending on your implementation, you may not want to do this!)
				if(DEBUG) Log.d(TAG, "...did you forget to call 'setContainingFragmentActivity(FragmentActivity)'?");
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
    
    /*
     * METHODS
     */
	
	public void setContainingFragmentActivity(FragmentActivity containingFragmentActivity) {
		mContainingFragmentActivity = containingFragmentActivity;
		//mContainingFragmentActivity.takeKeyEvents(true);
	}
    
    /*
     * INNER CLASSES
     */
}

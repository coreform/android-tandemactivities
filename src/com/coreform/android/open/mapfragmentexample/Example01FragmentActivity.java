package com.coreform.android.open.mapfragmentexample;

import com.coreform.android.open.tandemactivities.TandemActivitiesFragmentActivityInterface;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;

public class Example01FragmentActivity extends FragmentActivity implements TandemActivitiesFragmentActivityInterface {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example01FragmentActivity";
	
	@Override    
    protected void onCreate(Bundle savedInstanceState) {      
		if(DEBUG) Log.d(TAG, ".onCreate()...");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_fragment_activity);       
        setContentView(R.layout.fragmentcontainer_example01);
        
        if(DEBUG) getSupportFragmentManager().addOnBackStackChangedListener(new Example01OnBackStackChangedListener());
        
        //default fragment setup (note: does get added to backstack because User may navigate to a subsequent fragment.
        //BUT must manually handle situation where that doesn't happen, to bypass this fragment on the backstack...
        //Otherwise this fragment will be removed from container before the User can nav back again to close this Activity.)
		FragmentTransaction ft = getSupportFragmentManager().beginTransaction();
		Fragment defaultFragment = Fragment.instantiate(this, Example01NonMapFragment.class.getName());
		ft.add(R.id.fragmentcontent, defaultFragment, "nonmap");
		//ft.addToBackStack(null);
		ft.commit();
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
			if(handleBackKeyEvent()) {
				return true;
			}
		}
		return super.onKeyDown(keyCode, event);
	}
	
	/*
	 * METHODS
	 */
	
	public boolean handleBackKeyEvent() {
		//check if there are no remaining FragmentTransactions in the backstack
		if(getSupportFragmentManager().getBackStackEntryCount() > 0) {
			if(DEBUG) Log.d(TAG, "...some remaining backstack entries...");
			getSupportFragmentManager().popBackStack();
			return true;
		} else {
			if(DEBUG) Log.d(TAG, "...no remaining backstack entries...");
			//pause activity
			Example01FragmentActivity.this.finish();
			return true;
		}
	}
	
	/*
	 * INNER CLASSES
	 */
	
	private class Example01OnBackStackChangedListener implements OnBackStackChangedListener {
		@Override
		public void onBackStackChanged() {
			if(DEBUG) Log.d(TAG, ".onBackStackChanged()...entry count: "+getSupportFragmentManager().getBackStackEntryCount());
		}
	}
}

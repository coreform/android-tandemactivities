package com.coreform.android.open.mapfragmentexample;

import com.coreform.android.open.tandemactivities.TandemActivitiesMapActivity;
import com.coreform.android.open.tandemactivities.TandemActivitiesTabFragmentActivity;
import com.coreform.android.open.tandemactivities.TandemActivitiesTabFragmentActivityInterface;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager.OnBackStackChangedListener;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.TabHost.TabSpec;

public class Example03TabFragmentActivity extends TandemActivitiesTabFragmentActivity implements TandemActivitiesTabFragmentActivityInterface {
	private static final boolean DEBUG = true;
	private static final String TAG = "Example03FragmentActivity";
	
	@Override    
    protected void onCreate(Bundle savedInstanceState) {      
		if(DEBUG) Log.d(TAG, ".onCreate()...");
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.main_fragment_activity);       
        setContentView(R.layout.fragmentcontainer_example03);
        
        if(DEBUG) getSupportFragmentManager().addOnBackStackChangedListener(new Example03OnBackStackChangedListener());
        
        //the magic that sets up TandemActivitiesTabFragmentActivity
        //you must call super.preTabSetup(savedInstanceState) before calling setupTabs()
        super.preTabSetup(savedInstanceState);	
        setupTabs();
        //you should call super.postTabSetup() after calling setupTabs(),
        //especially if you want to be able to "open into" any specified Tab from any external Activities
        super.postTabSetup();
        
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
	
	@Override
	public void setupTabs() {
		//make sure to customise this method to setup tabs the way your implementation requires
		//note: you will need to specify an extra tab for the map itself IFF the map fragment is NOT the 1st fragment in its tab
		//note: BetterTabManager.addTab() will handle the .setContent() call for Activity/Fragment so no need to .setContent() here
		TabSpec tab_main = getTabHost().newTabSpec("main").setIndicator("Main");
		TabSpec tab_tab1 = getTabHost().newTabSpec(getTabTag_map()).setIndicator("Tab1");
		TabSpec tab_tab1_map = getTabHost().newTabSpec(getTabTag_extraMap()).setIndicator("Tab1");	//tab just for the MapFragment, hidden by default, shown when MapFragment needs to be shown
		TabSpec tab_tab2 = getTabHost().newTabSpec("tab2").setIndicator("Tab2");
		TabSpec tab_tab3 = getTabHost().newTabSpec("tab3").setIndicator("Tab3");
		
		//mBetterTabManager = new BetterTabManager(this, mTabHost, R.id.tabfragmentcontent);
		
		getBetterTabManager().addTab(tab_main, MainActivity.class, null, true);
		getBetterTabManager().addTab(tab_tab1, Example03Tab1NonMapFragment.class, null, false);	//this is the tab that "contains" the map fragment, but really the map fragment is in its own tab that pretends to be this one (only one of the two is visible at any given time)
		Bundle activityStartupBundle = new Bundle();
		activityStartupBundle.putInt("mapFragmentResource", R.layout.fragment_example03_tab_map);
		getBetterTabManager().addTab(tab_tab1_map, TandemActivitiesMapActivity.class, activityStartupBundle, true);	//added for 'map is not 1st screen in tab' problem...this Tab will be hidden until the map is accessed, at which time the normal tab will become hidden and this Tab visible
		getBetterTabManager().addTab(tab_tab2, Example03Tab2NonMapFragment.class, null, false);
		activityStartupBundle.putInt("mapFragmentResource", R.layout.fragment_example03_tab3_map);
		getBetterTabManager().addTab(tab_tab3, TandemActivitiesMapActivity.class, activityStartupBundle, true);	//this is a tab that contains a map as its first screen, no need to have an extra hidden tab in this case
		
		getTabHost().getTabWidget().getChildAt(0).setOnClickListener(new TabMainOnClickListener());
		getTabHost().getTabWidget().getChildAt(0).setOnFocusChangeListener(new TabMainOnFocusChangeListener());
		getTabHost().getTabWidget().getChildAt(2).setVisibility(View.GONE);	//StoresMAP hidden by default
		
		//set default tab.
		//in this case, the first Tab is more of a Button that yields the MainActivity, the first "real" Tab is the 2nd one
		getTabHost().setCurrentTabByTag(getTabTag_map());
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
			//finish activity
			Example03TabFragmentActivity.this.finish();
			return true;
		}
	}
	
	/*
	 * INNER CLASSES
	 */
	
	private class Example03OnBackStackChangedListener implements OnBackStackChangedListener {
		@Override
		public void onBackStackChanged() {
			if(DEBUG) Log.d(TAG, ".onBackStackChanged()...entry count: "+getSupportFragmentManager().getBackStackEntryCount());
		}
	}
	
	private class TabMainOnClickListener implements OnClickListener {
		public void onClick(View arg0) {
			Intent displayHomeActivityIntent = new Intent(Example03TabFragmentActivity.this, MainActivity.class);
			displayHomeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(displayHomeActivityIntent);
		}
    }
    
    private class TabMainOnFocusChangeListener implements OnFocusChangeListener {
		public void onFocusChange(View v, boolean hasFocus) {
			Intent displayHomeActivityIntent = new Intent(Example03TabFragmentActivity.this, MainActivity.class);
			displayHomeActivityIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        	startActivity(displayHomeActivityIntent);
		}
    }
}

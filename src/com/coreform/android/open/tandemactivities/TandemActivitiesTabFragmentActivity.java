package com.coreform.android.open.tandemactivities;

import java.util.HashMap;
import java.util.Map.Entry;
import java.util.Set;

import com.coreform.android.open.mapfragmentexample.R;
import com.coreform.android.open.mapfragmentexample.R.id;

import android.app.LocalActivityManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.TabHost;
import android.widget.TabHost.TabSpec;

public abstract class TandemActivitiesTabFragmentActivity extends FragmentActivity {
	private static final boolean DEBUG = true;
	private static final String TAG = "TandemActivitiesTabFragmentActivity";
	
	private static final String KEY_STATE_BUNDLE = "localActivityManagerState";
	
	protected String mTabTag_map = "mapTab";
	protected String mTabTag_extraMap = "mapExtraTab";
	protected int mTabNum_map = 1;
	protected int mTabNum_extraMap = 2;
	
	@SuppressWarnings("deprecation")
	private LocalActivityManager mLocalActivityManager;	//necessary to get MapActivity invoked within the Fragment lifecycle
	private TabHost mTabHost;
	private BetterTabManager mBetterTabManager;
	
	//note: extending class should override onCreate itself and call preTabSetup(), setupTabs(), and postTabTestup()
	
	@Override
	protected void onResume() {
		if(DEBUG) Log.d(TAG, ".onResume()...");
		mLocalActivityManager.dispatchResume();
		super.onResume();
	}
	
	@Override
    protected void onSaveInstanceState(Bundle outState) {
		if(DEBUG) Log.d(TAG, ".onSaveInstanceState()...");
        super.onSaveInstanceState(outState);
        outState.putString("tab", mTabHost.getCurrentTabTag());
    }
	
	@Override
	protected void onPause() {
		if(DEBUG) Log.d(TAG, ".onPause()...");
		mLocalActivityManager.dispatchPause(isFinishing());
		super.onPause();
	}
	
	/*
	 * METHODS
	 */
	
	protected void preTabSetup(Bundle savedInstanceState, String mapTabTag, String mapExtraTabTag, int mapTabNum, int mapExtraTabNum) {
		mTabTag_map = mapTabTag;
		mTabTag_extraMap = mapExtraTabTag;
		mTabNum_map = mapTabNum;
		mTabNum_extraMap = mapExtraTabNum;
		
		preTabSetup(savedInstanceState);
	}
	
	protected void preTabSetup(Bundle savedInstanceState) {
		//note: LocalActivityManager is deprecated, but necessary for showing MapFragment in tandem with a FragmentActivity!
        Bundle state = null;
        if(savedInstanceState != null) {
            state = savedInstanceState.getBundle(KEY_STATE_BUNDLE);
        }
        mLocalActivityManager = new LocalActivityManager(this, true);
        mLocalActivityManager.dispatchCreate(state);
        
        mTabHost = (TabHost) findViewById(android.R.id.tabhost);
		mTabHost.setup(mLocalActivityManager);
		
		mBetterTabManager = new BetterTabManager(this, mTabHost, R.id.tabfragmentcontent);
	}
	
	protected void postTabSetup() {
		//handle Intent extra data specifying which Tab to open as default (so HomeScreenActivity links can go straight to their corresponding Tab)
        //...note: it would seem neat to use the #fragment portion of a URI to specify the fragment to load, however there's phones with known bugs related to correctly interpreting the #fragment portion - so avoid that!
        //......besides, the Uri portion of the Intent is already utilised in passing TabsFragmentActivity.class
        //......so, use Extras Bundle and (potentially) expect a String named 'targetTabTag'
        Bundle incomingIntentExtrasBundle = this.getIntent().getExtras();
        if(incomingIntentExtrasBundle != null) {
	        String targetTabTag = incomingIntentExtrasBundle.getString("targetTabTag");
	        if(targetTabTag != null && !"".equals(targetTabTag)) {
	        	mTabHost.setCurrentTabByTag(targetTabTag);
	        }
        }
	}
	
	//make sure to customise this method as per your implementation's needs when extending this class
	public abstract void setupTabs();
	
	public TabHost getTabHost() {
		return mTabHost;
	}
	
	public BetterTabManager getBetterTabManager() {
		return mBetterTabManager;
	}
	
	public String getTabTag_map() {
		return mTabTag_map;
	}
	
	public String getTabTag_extraMap() {
		return mTabTag_extraMap;
	}
	
	public int getTabNum_map() {
		return mTabNum_map;
	}
	
	public int getTabNum_extraMap() {
		return mTabNum_extraMap;
	}
	
	//proxy-method for accessing mLocalActivityManager.destroyActivity()
	public void finishCurrentActivity() {
		mLocalActivityManager.getCurrentActivity().finish();
	}
	
	//method used by Fragments to ensure their appropriate Tab is the current one
	//...because fragment transactions do not handle changing tab
	public void changeToTab(String tabTag) {
		mTabHost.setCurrentTabByTag(tabTag);
	}
	
	/*
	 * INNER CLASSES
	 */
	
	/**
     * This is a helper class that implements a generic mechanism for
     * associating fragments with the tabs in a tab host.  It relies on a
     * trick.  Normally a tab host has a simple API for supplying a View or
     * Intent that each tab will show.  This is not sufficient for switching
     * between fragments.  So instead we make the content part of the tab host
     * 0dp high (it is not shown) and the TabManager supplies its own dummy
     * view to show as the tab content.  It listens to changes in tabs, and takes
     * care of switch to the correct fragment shown in a separate content area
     * whenever the selected tab changes.
     * 
     * LD: no longer static as it needs to reference its outer class
     */
    public class BetterTabManager implements TabHost.OnTabChangeListener {
    	private static final boolean DEBUG = true;
    	private static final String TAG = "BetterTabManager";
    	
        private final FragmentActivity mActivity;
        private final TabHost mTabHost;
        private final int mContainerId;
        private final HashMap<String, TabInfo> mTabs = new HashMap<String, TabInfo>();
        TabInfo mLastTab;

        public BetterTabManager(FragmentActivity activity, TabHost tabHost, int containerId) {
            mActivity = activity;
            mTabHost = tabHost;
            mContainerId = containerId;
            mTabHost.setOnTabChangedListener(this);
        }

        public void addTab(TabHost.TabSpec tabSpec, Class<?> clss, Bundle args, boolean isLegacyActivityTab) {
        	//BetterTabManager handles extra isLegacyActivityTab, in which case it is expected that clss is an Activity that should be added to TabSpec in place of DummyTabFactory
            if(isLegacyActivityTab) {
            	Intent legacyActivityIntent = new Intent(mActivity, clss);
            	legacyActivityIntent.putExtras(args);
            	tabSpec.setContent(legacyActivityIntent);
            	String tag = tabSpec.getTag();
                TabInfo info = new TabInfo(tag, clss, args, true);
                mTabs.put(tag, info);
                mTabHost.addTab(tabSpec);
            } else {
            	tabSpec.setContent(new DummyTabFactory(mActivity));
            	String tag = tabSpec.getTag();
                TabInfo info = new TabInfo(tag, clss, args, false);
                // Check to see if we already have a fragment for this tab, probably
                // from a previously saved state.  If so, deactivate it, because our
                // initial state is that a tab isn't shown.
                info.fragment = mActivity.getSupportFragmentManager().findFragmentByTag(tag);
                if (info.fragment != null && !info.fragment.isDetached()) {
                    FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
                    ft.detach(info.fragment);
                    ft.commit();
                }
                mTabs.put(tag, info);
                mTabHost.addTab(tabSpec);
            }
        }

        //@Override
        public void onTabChanged(String tabId) {
        	if(DEBUG) Log.d(TAG, ".onTabChanged()...tabId: "+tabId);
            TabInfo newTab = mTabs.get(tabId);
            if(DEBUG) Log.d(TAG, "...newTab tag: "+newTab.tag);
            //remove any FragmentTransactions from last tab
            //this unfortunately results in the last tab's first transaction's fragment being redisplayed, which then sets the tab back for itself (IFF fragments are setting their own appropriate tab)
            if(mLastTab != null && mLastTab != newTab) {
            	if(DEBUG) Log.d(TAG, "...attempting to remove any FragmentTransactions from last tab...backstack entry count: "+getSupportFragmentManager().getBackStackEntryCount());
            	getSupportFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
            }
            //create an "empty" FragmentTransaction as the first FragmentTransaction of this tab
            /*
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            ft.addToBackStack(null);
            ft.commit();
            */
            FrameLayout dummyFrameLayout = (FrameLayout) findViewById(android.R.id.tabcontent);
            FragmentTransaction ft = mActivity.getSupportFragmentManager().beginTransaction();
            //ft = mActivity.getSupportFragmentManager().beginTransaction();
            if (mLastTab != null) {
            	if(DEBUG) Log.d(TAG, "...last tab exists...mLastTab.tag: "+mLastTab.tag);
                if (mLastTab.fragment != null) {
                	if(DEBUG) Log.d(TAG, "...detaching last tab's fragment...mLastTab.tag: "+mLastTab.tag);
                    ft.detach(mLastTab.fragment);
                    //check if last Tab is a legacyActivityTab, if so hide it now
                	/*
                	if(mLastTab.tag.equals(mTabTag_map)) {
                		//note: below is useless as the extraMap tab is already "gone", seems it's ghostly image remains however...
                		if(DEBUG) Log.d(TAG, "...last tab map tab, attempting to hide it's faux tab (which contains the map)...mLastTab.tag: "+mLastTab.tag);
                		View newTabView = mTabHost.getTabWidget().getChildAt(mTabNum_extraMap);
                		newTabView.setVisibility(View.GONE);
                	}
                	*/
                } else {
                	//check if last Tab is a legacyActivityTab, if so hide it now
                	if(mLastTab.isLegacyActivityTab) {
                		if(DEBUG) Log.d(TAG, "...last tab isLegacyActivityTab, attempting to hide it...mLastTab.tag: "+mLastTab.tag);
                		View newTabView = mTabHost.getTabWidget().getChildAt(mTabNum_extraMap);
                		newTabView.setVisibility(View.GONE);
                	}
                }
            }
            if(!newTab.isLegacyActivityTab) {
            	if(DEBUG) Log.d(TAG, "...handling NON-legacyActivityTab, manhandling the dummyFrameLayout...");
            	dummyFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(0, 0));
	            if (mLastTab != newTab) {
	            	if(mTabTag_map.equals(newTab.tag)) {
	            		//special case for a Map that is not the 1st screen in a tab
	            		//...need to unhide the normal version of tab, hide the map version of tab
	            		//...so effectively, the map IS the 1st and only screen within its tab
	            		View normalTabView = mTabHost.getTabWidget().getChildAt(mTabNum_map);
	            		normalTabView.setVisibility(View.VISIBLE);
	            		View newTabView = mTabHost.getTabWidget().getChildAt(mTabNum_extraMap);
	            		newTabView.setVisibility(View.GONE);
	            	}
	                if (newTab != null) {
	                    if (newTab.fragment == null) {
	                        newTab.fragment = Fragment.instantiate(mActivity, newTab.clss.getName(), newTab.args);
	                        ft.add(mContainerId, newTab.fragment, newTab.tag);
	                    } else {
	                        ft.attach(newTab.fragment);
	                    }
	                }
	            }
            } else {
            	if(DEBUG) Log.d(TAG, "...handling legacyActivityTab, manhandling the dummyFrameLayout...");
            	//in attempt to solve the 'fragments not displaying' problem, try forcing the 'dummy' FrameLayout's height to fill_parent
            	//...in turn, we have to "reopen" the 'dummy' FrameLayout when the tab !isLegacyActivityTab
            	//...which indicates that the DummyTabFactory isn't handling this as well as Google suggested it would
            	dummyFrameLayout.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.FILL_PARENT, LinearLayout.LayoutParams.FILL_PARENT));
            	//we don't instantiate a new fragment here because the legacy Activity is NOT a fragment
            	if(mTabTag_extraMap.equals(newTab.tag)) {
            		//special case for a Map that is not the 1st screen in a tab
            		//...need to hide the normal version of tab, unhide the map version of tab
            		//...so effectively, the map IS the 1st and only screen within its tab
            		View normalTabView = mTabHost.getTabWidget().getChildAt(mTabNum_map);
            		normalTabView.setVisibility(View.GONE);
            		View newTabView = mTabHost.getTabWidget().getChildAt(mTabNum_extraMap);
            		newTabView.setVisibility(View.VISIBLE);
            	}
            }
            mLastTab = newTab;
            ft.commit();
            mActivity.getSupportFragmentManager().executePendingTransactions();
        }
    }
    
    //moved out from BetterTabManager since that class is no longer static
    static final class TabInfo {
        private final String tag;
        private final Class<?> clss;
        private final Bundle args;
        private final boolean isLegacyActivityTab;
        private Fragment fragment;

        TabInfo(String _tag, Class<?> _class, Bundle _args, boolean _isLegacyActivityTab) {
            tag = _tag;
            clss = _class;
            args = _args;
            isLegacyActivityTab = _isLegacyActivityTab;
        }
    }
    
    //moved out from BetterTabManager since that class is no longer static
    static class DummyTabFactory implements TabHost.TabContentFactory {
    	private static final boolean DEBUG = true;
    	private static final String TAG = "DummyTabFactory";
    	
        private final Context mContext;

        public DummyTabFactory(Context context) {
            mContext = context;
        }

        //@Override
        public View createTabContent(String tag) {
        	if(DEBUG) Log.d(TAG, ".createTabContent()...");
            View v = new View(mContext);
            v.setMinimumWidth(0);
            v.setMinimumHeight(0);
            return v;
        }
    }
}

package com.edwardsterkin;

import android.app.ActionBar.Tab;
import android.app.Activity;
import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentTransaction;


public class ActivityTabListener implements ActionBar.TabListener {
	private Fragment mFragment;
	private final EventsActivity mActivity;
	private final String mTag;
	private final Class<?> mClass;

	public ActivityTabListener(Activity activity, String tag, Class<?> cls) {
		mActivity = (EventsActivity) activity;
		mTag = tag;
		mClass = cls;
	}

	/* The following are each of the ActionBar.TabListener callbacks */

	public void onTabSelected(Tab tab, FragmentTransaction ft) {
		// Check if the fragment is already initialized
		if (mFragment == null) {

			System.out.println("ActivityTabListner: Fragment null");
			// If not, instantiate and add it to the activity

			mFragment = Fragment.instantiate(mActivity, mClass.getName());
			System.out.println(mClass.getName());
			if(mClass.getName().equals("com.edwardsterkin.MapsFragment")){
				mActivity.toggleOptionItems(false);
			}
			
			else if(mClass.getName().equals("com.edwardsterkin.EventsFragment")){
				mActivity.toggleOptionItems(true);
			}
			
			ft.add(android.R.id.content, mFragment, mTag);
		} else {
			// If it exists, simply attach it in order to show it
			ft.attach(mFragment);
		}
	}
	
	public void onTabUnselected(Tab tab, FragmentTransaction ft) {
		if (mFragment != null) {
			// Detach the fragment, because another one is being attached
			ft.detach(mFragment);
			System.out.println(mClass.getName());	
			mFragment = null;

		}
	}

	public void onTabReselected(Tab tab, FragmentTransaction ft) {
		// User selected the already selected tab. Usually do nothing.
	}
}
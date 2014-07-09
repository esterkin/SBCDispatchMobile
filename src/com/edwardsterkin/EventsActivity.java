package com.edwardsterkin;

import android.app.ActionBar;
import android.app.ActionBar.Tab;
import android.app.Activity;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.SearchView;

public class EventsActivity extends Activity implements
		SearchView.OnQueryTextListener {

	private EventsFragment eventsfragment;
	// private MapsFragment fragment2;
	private ActionBar actionBar;
	private Menu menu;
	private boolean viewCreated = false;

	//if value is -1 tooltip will not be shown
	public static int eventTooltipNum = -1;

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);

		actionBar = getActionBar();
		actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);
		actionBar.setDisplayShowTitleEnabled(false);
		actionBar.setDisplayShowHomeEnabled(true);

		Tab alertsTab = actionBar
				.newTab()
				.setText("Alerts")
				.setTabListener(
						new ActivityTabListener(this, "events",
								EventsFragment.class));
		actionBar.addTab(alertsTab);

		Tab mapsTab = actionBar
				.newTab()
				.setText("Map")
				.setTabListener(
						new ActivityTabListener(this, "map", MapsFragment.class));
		actionBar.addTab(mapsTab);
		viewCreated = true;

		// TODO uncomment when GraphView is implemented

		// Tab graphTab = actionBar
		// .newTab()
		// .setText("Graph")
		// .setTabListener(
		// new ActivityTabListener(this, "graph", GraphFragment.class));
		// actionBar.addTab(graphTab);

	}

	// hides the search and display action items when alert tab is not selected

	public void toggleOptionItems(Boolean display) {

		if (viewCreated) {
			MenuItem searchB = menu.findItem(R.id.menu_search);
			MenuItem refreshB = menu.findItem(R.id.menu_refresh);
			if (!display) {
				searchB.setVisible(false);
				refreshB.setVisible(false);
			} else {
				searchB.setVisible(true);
				refreshB.setVisible(true);
			}
		}

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		this.menu = menu;
		getMenuInflater().inflate(R.menu.options, menu);
		SearchView searchView = (SearchView) menu.findItem(R.id.menu_search)
				.getActionView();

		searchView.setIconifiedByDefault(false);
		searchView.setOnQueryTextListener(this);
		searchView.setSubmitButtonEnabled(false);
		searchView.setFocusable(false);

		return super.onCreateOptionsMenu(menu);
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.menu_refresh: {
			eventsfragment = (EventsFragment) getFragmentManager()
					.findFragmentByTag("events");
			eventsfragment.manualRefresh();
			break;
		}
		}
		return true;

	}

	@Override
	public boolean onQueryTextSubmit(String query) {
		return false;
	}

	@Override
	public boolean onQueryTextChange(String newText) {

		eventsfragment = (EventsFragment) getFragmentManager()
				.findFragmentByTag("events");

		eventsfragment.getAdapter().getFilter().filter(newText);
		return true;
	}

	public void showMarker(int pos) {

		eventTooltipNum = pos;
		//switch to the Maps tab
		actionBar.setSelectedNavigationItem(1);

	}
}
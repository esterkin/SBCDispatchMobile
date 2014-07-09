package com.edwardsterkin;

import java.util.ArrayList;
import android.app.Fragment;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


//Parts of the code from http://stackoverflow.com/a/20971284/1291886
public class MapsFragment extends Fragment {
	
	private MapView mapView;
	private GoogleMap map;
	private ArrayList<Marker> markers; 

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		
		View v = inflater.inflate(R.layout.map, container, false);
		// Gets the MapView from the XML layout and creates it

		MapsInitializer.initialize(getActivity());

		switch (GooglePlayServicesUtil
				.isGooglePlayServicesAvailable(getActivity())) {
		case ConnectionResult.SUCCESS:
			
			mapView = (MapView) v.findViewById(R.id.map);
			mapView.onCreate(savedInstanceState);
			markers = new ArrayList<Marker>();
			// Gets to GoogleMap from the MapView and does initialization stuff
			if (mapView != null) {
				map = mapView.getMap();
							
				CameraUpdate cameraUpdate = CameraUpdateFactory.newLatLngZoom(
						new LatLng(34.4134582,-119.8616822), 15);
				map.moveCamera(cameraUpdate);
				
				//populate the map with events
				if(EventsFragment.events != null){
					for(Event e: EventsFragment.events){
						float lat = e.getLat();
						float lng = e.getLng();
						Marker marker = map.addMarker(new MarkerOptions()
	                     .position(new LatLng(lat,lng))
	                      .title(e.getDescription()));
						markers.add(marker);
					}
					
					//determine if tooltip for an event needs to be shown 
					if(EventsActivity.eventTooltipNum != -1){
						Marker marker = markers.get(EventsActivity.eventTooltipNum);
						LatLng coords = marker.getPosition();
						marker.showInfoWindow();
						cameraUpdate = CameraUpdateFactory.newLatLngZoom(
								coords, 15);
						map.moveCamera(cameraUpdate);
						//restore state
						EventsActivity.eventTooltipNum = -1;
					}
				}
				
			}
			break;
			
		case ConnectionResult.SERVICE_MISSING:
			Toast.makeText(getActivity(), "SERVICE MISSING", Toast.LENGTH_SHORT)
					.show();
			break;
		case ConnectionResult.SERVICE_VERSION_UPDATE_REQUIRED:
			Toast.makeText(getActivity(), "UPDATE REQUIRED", Toast.LENGTH_SHORT)
					.show();
			break;
		}

		return v;
	}

	@Override
	public void onResume() {
		mapView.onResume();
		super.onResume();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		mapView.onDestroy();
	}

	@Override
	public void onLowMemory() {
		super.onLowMemory();
		mapView.onLowMemory();
	}
	
	public void showMarker(int marker){
		
		if(map != null){
			Marker m = markers.get(marker);
			m.showInfoWindow();			
		}
		
		else{
			System.err.println("Map is null");
		}
	}
}

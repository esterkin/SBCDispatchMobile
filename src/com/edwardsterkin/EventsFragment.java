package com.edwardsterkin;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.SortedMap;
import java.util.TreeMap;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.params.BasicHttpParams;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.ListView;

public class EventsFragment extends ListFragment {

	private EventAdapter adapter;
	private String jsonString;

	public static ArrayList<Event> events = new ArrayList<Event>();

	@Override
	public void onListItemClick(ListView lv, View v, int position, long id) {

		super.onListItemClick(lv, v, position, id);
		Event thisEvent = (Event) lv.getItemAtPosition(position);	
		System.out.println("EventsFrag item id clicked " + thisEvent.getId());
		((EventsActivity) getActivity()).showMarker(thisEvent.getId());
		
	}

	public EventAdapter getAdapter() {
		return this.adapter;
	}

	public void onCreate(Bundle savedInstanceState) {

		super.onCreate(savedInstanceState);
		adapter = new EventAdapter(this.getActivity(), R.layout.event_item,
				events);
		setListAdapter(adapter);
		new loadEventsTask().execute();

	}

	public void manualRefresh() {
		new loadEventsTask().execute();
	}

	// fetch the JSON event data
	class loadEventsTask extends AsyncTask<Void, Void, Void> {

		@Override
		protected Void doInBackground(Void... params) {

			jsonString = getJSON("http://ivalertmap.appspot.com/api/events/past/7");
			return null;
		}

		@Override
		protected void onPostExecute(Void result) {

			if (events != null) {
				events.clear();
			}

			JSONObject jObject = null;
			try {
				jObject = new JSONObject(jsonString);
			} catch (JSONException e) {

				e.printStackTrace();
			}

			List<?> eventsJson = listFromJsonSorted(jObject);
			
			if (eventsJson != null) {
				int id = -1;
				for (Object o : eventsJson) {
					JSONArray eventValues = (JSONArray) o;
					Event event = new Event();
					try {
						event.setDescription(eventValues.getString(0));
						event.setDate(eventValues.getString(3));
						event.setLat(Float.parseFloat(eventValues.getString(1)));
						event.setLng(Float.parseFloat(eventValues.getString(2)));
						event.setId(++id);
					} catch (JSONException jsonException) {
						jsonException.printStackTrace();
					}
					events.add(event);
				}
			}

			adapter.notifyDataSetChanged();
		}

	}

	// http://stackoverflow.com/a/9606629
	// changed from POST to GET

	public String getJSON(String url) {
		DefaultHttpClient httpclient = new DefaultHttpClient(
				new BasicHttpParams());
		HttpGet httpget = new HttpGet(url);
		// Depends on your web service
		httpget.setHeader("Content-type", "application/json");

		InputStream inputStream = null;
		String result = null;
		try {
			HttpResponse response = httpclient.execute(httpget);

			HttpEntity entity = response.getEntity();

			inputStream = entity.getContent();
			// json is UTF-8 by default
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					inputStream, "UTF-8"), 8);
			StringBuilder sb = new StringBuilder();

			String line = null;
			while ((line = reader.readLine()) != null) {
				sb.append(line + "\n");
			}
			result = sb.toString();
			return result;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (inputStream != null)
					inputStream.close();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}
		return "Null";
	}

	// http://stackoverflow.com/a/8765539

	public static List<JSONArray> listFromJsonSorted(JSONObject json) {
		if (json == null)
			return null;
		SortedMap<Integer, JSONArray> map = new TreeMap<Integer, JSONArray>();
		Iterator<?> i = json.keys();
		while (i.hasNext()) {
			try {
				String keyString = i.next().toString();
				int key = Integer.parseInt(keyString);
				JSONArray j = json.getJSONArray(keyString);
				map.put(key, j);
			} catch (JSONException e) {
				e.printStackTrace();
			}
		}

		return new LinkedList<JSONArray>(map.values());
	}

}

package com.edwardsterkin;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.Filterable;
import android.widget.ImageView;
import android.widget.TextView;

public class EventAdapter extends ArrayAdapter<Event> implements Filterable {

	private ArrayList<Event> events;
	private ArrayList<Event> originalEvents;
	private Activity activity;

	public EventAdapter(Activity act, int resource, ArrayList<Event> events) {
		super(act, resource, events);
		this.events = events;
		this.activity = act;
	}

	public static class ViewHolder {
		public TextView desc;
		public TextView date;
		public ImageView icon;
	}
	
    @Override
    public int getCount() {
        return events.size();
    }
    
    @Override
    public Event getItem(int position) {
        return events.get(position);
    }
    
    @Override
    public long getItemId(int position) {
        return position;
    }


	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		View v = convertView;
		ViewHolder holder;
		if (v == null) {
			LayoutInflater vi = (LayoutInflater) activity
					.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
			v = vi.inflate(R.layout.event_item, null);
			holder = new ViewHolder();
			holder.desc = (TextView) v.findViewById(R.id.eventDesc);
			holder.date = (TextView) v.findViewById(R.id.eventDate);
			holder.icon = (ImageView) v.findViewById(R.id.icon);

			v.setTag(holder);
		} else
			holder = (ViewHolder) v.getTag();

		final Event event = events.get(position);
		if (event != null) {
			holder.desc.setText(event.getDescription());
			holder.date.setText(event.getDate());
		}
		return v;
	}
	
	//modified http://stackoverflow.com/questions/8678163/list-filter-custom-adapter-dont-give-result
	@Override
    public Filter getFilter() {
        Filter filter = new Filter() {

     
            @SuppressWarnings("unchecked")
			@Override
            protected void publishResults(CharSequence constraint,FilterResults results) {

                events = (ArrayList<Event>) results.values; // has the filtered values
                notifyDataSetChanged();  // notifies the data with new filtered values
            }

            @Override
            protected FilterResults performFiltering(CharSequence constraint) {
                FilterResults results = new FilterResults();        // Holds the results of a filtering operation in values
                List<Event> FilteredArrList = new ArrayList<Event>();

                if (originalEvents == null) {
                	originalEvents = new ArrayList<Event>(events); // saves the original data in originalEvents
                }

                if (constraint == null || constraint.length() == 0) {

                    // set the Original result to return  
                    results.count = originalEvents.size();
                    results.values = originalEvents;
                } else {
                    constraint = constraint.toString().toLowerCase(Locale.getDefault());
                    for (int i = 0; i < originalEvents.size(); i++) {
                        Event data = originalEvents.get(i);
                        if (data.toString().toLowerCase().contains(constraint.toString())) {
                            FilteredArrList.add(data);
                        }
                    }
                    // set the Filtered result to return
                    results.count = FilteredArrList.size();
                    results.values = FilteredArrList;
                }
                return results;
            }
        };
        return filter;
    }
}




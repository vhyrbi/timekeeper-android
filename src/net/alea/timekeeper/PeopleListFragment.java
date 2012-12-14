/*
TimeKeeper - Software to monitor how much time each person speaks
Copyright (C) 2012 Vincent Hiribarren

This program is free software: you can redistribute it and/or modify
it under the terms of the GNU General Public License as published by
the Free Software Foundation, either version 3 of the License, or
(at your option) any later version.

This program is distributed in the hope that it will be useful,
but WITHOUT ANY WARRANTY; without even the implied warranty of
MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
GNU General Public License for more details.

You should have received a copy of the GNU General Public License
along with this program.  If not, see <http://www.gnu.org/licenses/>.
*/

package net.alea.timekeeper;

import java.util.List;

import net.alea.timekeeper.model.Chrono;
import net.alea.timekeeper.model.TimedElement;
import android.app.Fragment;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;

public class PeopleListFragment extends Fragment {
	
	private final static int GUI_REFRESH_RATE = 250; // in ms
	
	private ListView _timedElementListView;
	private Button _chronoStopButton;

	private final Handler _handler = new Handler();
	
	
	private final Runnable _updateViewTask = new Runnable() {
		public void run() {
			if (_timedElementListView != null) {
				((TimedElementViewAdapter)_timedElementListView.getAdapter()).notifyDataSetChanged();
			}
			_handler.postDelayed(this, GUI_REFRESH_RATE);
		}
	};	
	
	private void startUIRefresh() {
		_handler.removeCallbacks(_updateViewTask);
		_handler.postDelayed(_updateViewTask, GUI_REFRESH_RATE);
	}
		
	private void stopUIRefresh() {
		_handler.removeCallbacks(_updateViewTask);
	}
	
	
	@Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_people_list, container, false);
    }
    
    
    @Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
    	_chronoStopButton = (Button)view.findViewById(R.id.chronoStopButton);
    	_chronoStopButton.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				int count = _timedElementListView.getCount();
				for (int i=0; i < count; i++) {
					/* The list is not supposed to have more than 20 elements, so
					 * keeping a set of "running chronos" instead of browsing the
					 * whole list seems an overhead.	 */
					TimedElement timedElement = (TimedElement)_timedElementListView.getItemAtPosition(i);
					if (timedElement.getChrono().isRunning()) {
						timedElement.getChrono().stop();
					}
				}
			}
		});
    	_timedElementListView = (ListView) view.findViewById(R.id.peopleListView);
    	_timedElementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TimedElement timedElement = (TimedElement)_timedElementListView.getAdapter().getItem(position);
				Chrono chrono = timedElement.getChrono();
				if (chrono.isRunning()) {
					chrono.stop();
				}
				else {
					chrono.start();
				}
			}
		});
	}

  
	@Override
	public void onResume() {
		super.onResume();
		startUIRefresh();
	}
	

	@Override
	public void onPause() {
		super.onPause();
		stopUIRefresh();
	}


	public void setTimedElementList(List<TimedElement> timedElements) {
		_timedElementListView.setAdapter(new TimedElementViewAdapter(getActivity(), timedElements));
    }
	
	
	class TimedElementViewAdapter extends ArrayAdapter<TimedElement> {
		public TimedElementViewAdapter(Context context, List<TimedElement> objects) {
			super(context, 0, objects);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			// TODO Dummy view, to replace with final view
			TextView textView = (TextView)convertView;
			if (textView == null) {
				textView = new TextView(getContext());
			}
			TimedElement timedElement = this.getItem(position);
			textView.setText(timedElement.getName() + " " + timedElement.getChrono().getElapsedTimeMillis());
			return textView;
		}	
	}
    
	
}

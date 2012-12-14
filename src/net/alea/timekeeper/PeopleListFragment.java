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
import android.graphics.Color;
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
	
	private boolean _multiChrono = false;
	private ListView _timedElementListView;
	private Button _chronoStopButton;

	private final Handler _handler = new Handler();
	
	
	private final Runnable _updateViewTask = new Runnable() {
		public void run() {
			refreshUI();
			_handler.postDelayed(this, GUI_REFRESH_RATE);
		}
	};	
	
	private void refreshUI() {
		if (_timedElementListView != null) {
			((TimedElementViewAdapter)_timedElementListView.getAdapter()).notifyDataSetChanged();
		}		
	}
	
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
				stopAllChronos();
				refreshUI();
			}
		});
    	_timedElementListView = (ListView) view.findViewById(R.id.peopleListView);
    	_timedElementListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
				TimedElement timedElement = (TimedElement)_timedElementListView.getAdapter().getItem(position);
				Chrono chrono = timedElement.getChrono();
				if (chrono.isRunning()) {
					if (!_multiChrono) stopAllChronos(); // Not sure if keeping a set of running chronos worth it (list should have less than 15 elements)
					else chrono.stop();
				}
				else {
					if (!_multiChrono) stopAllChronos(); // Not sure if keeping a set of running chronos worth it (list should have less than 15 elements)
					chrono.start();
				}
				refreshUI();
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


	private void stopAllChronos() {
		int count = _timedElementListView.getCount();
		for (int i=0; i < count; i++) {
			TimedElement timedElement = (TimedElement)_timedElementListView.getItemAtPosition(i);
			if (timedElement.getChrono().isRunning()) {
				timedElement.getChrono().stop();
			}
		}		
	}
	
	
	public void setTimedElementList(List<TimedElement> timedElements) {
		_timedElementListView.setAdapter(new TimedElementViewAdapter(getActivity(), timedElements));
    }
	
	
	public void setMultiChrono(boolean multi) {
		_multiChrono = multi;
	}
	
	
	class TimedElementViewAdapter extends ArrayAdapter<TimedElement> {
		public TimedElementViewAdapter(Context context, List<TimedElement> objects) {
			super(context, 0, objects);
		}
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			TimedElement timedElement = this.getItem(position);
			View timedElementView = convertView;
			if (timedElementView == null) {
				LayoutInflater inflater =  (LayoutInflater) getContext().getSystemService(Context.LAYOUT_INFLATER_SERVICE);
				timedElementView = inflater.inflate(R.layout.timed_element_view, parent, false);
			}
			// Build username
			final TextView nameTextView = (TextView)timedElementView.findViewById(R.id.nameLabel);
			nameTextView.setText(timedElement.getName());
			// Build chrono text
			final TextView chronoTextView = (TextView)timedElementView.findViewById(R.id.chronoLabel);
			int[] time = timedElement.getChrono().getElapsedTimeHourMinuteSecond();
			chronoTextView.setText(String.format("%02d:%02d:%02d", time[0], time[1], time[2]));
			// Build background
			if (timedElement.getChrono().isRunning()) {
				timedElementView.setBackgroundColor(getResources().getColor(R.color.chrono_running));
			}
			else {
				timedElementView.setBackgroundColor(Color.TRANSPARENT);
			}
			return timedElementView;
		}	
	}
    
	
}

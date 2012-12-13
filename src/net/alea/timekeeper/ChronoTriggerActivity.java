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

import java.util.ArrayList;
import java.util.List;

import net.alea.timekeeper.model.Chrono;
import net.alea.timekeeper.model.TimedElement;
import android.os.Bundle;
import android.app.Activity;
import android.view.Menu;


public class ChronoTriggerActivity extends Activity {

	private PeopleListFragment _peopleListFragment;
	private List<TimedElement> _timedElements;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chrono_trigger);
		_timedElements = createDummyData(); // TODO This is dummy data for test, replace with real code		
		_peopleListFragment = (PeopleListFragment)getFragmentManager().findFragmentById(R.id.peopleListFragment);
		_peopleListFragment.setTimedElementList(_timedElements);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chrono_trigger, menu);
		return true;
	}
	
	
	// TODO This is dummy data for test, replace with real code
	private List<TimedElement> createDummyData() {
		final List<TimedElement> data = new ArrayList<TimedElement>();
		for(int i=0; i < 40; i++) {
			data.add(new TimedElement("User "+i, new Chrono()));
		}
		return data;
	}


}

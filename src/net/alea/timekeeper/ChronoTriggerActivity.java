/*
TimeKeeper - Software to monitor how much time each person speaks
Copyright (C) 2012, 2013 Vincent Hiribarren

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

import java.util.LinkedList;
import java.util.List;

import net.alea.timekeeper.model.Chrono;
import net.alea.timekeeper.model.TimedElement;
import android.os.Bundle;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.DialogInterface;
import android.view.Menu;
import android.view.MenuItem;
import android.view.WindowManager;
import android.widget.EditText;


public class ChronoTriggerActivity extends Activity {

	private PeopleListFragment _peopleListFragment;
	private List<TimedElement> _timedElements;
	
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_chrono_trigger);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		_timedElements =  new LinkedList<TimedElement>();
		_peopleListFragment = (PeopleListFragment)getFragmentManager().findFragmentById(R.id.peopleListFragment);
		_peopleListFragment.setTimedElementList(_timedElements);
	}

	
	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.activity_chrono_trigger, menu);
		return true;
	}
	
	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
	    switch (item.getItemId()) {
	    	case R.id.menu_add_chrono:
	        	AddChronoDialogFragment addDialog = new AddChronoDialogFragment();
	        	addDialog.show(getFragmentManager(), "addDialog");
	    		return true;
	        case R.id.menu_multichrono:
	        	if (item.isChecked()) {
	        		item.setChecked(false);
	        		_peopleListFragment.setMultiChrono(false);
	        	}
	            else {
	            	item.setChecked(true);
	        		_peopleListFragment.setMultiChrono(true);
	            }
	            return true;
	        case R.id.menu_reset:
	        	ResetTimerDialogFragment resetDialog = new ResetTimerDialogFragment();
	        	resetDialog.show(getFragmentManager(), "resetDialog");
	        	return true;
	        default:
	            return super.onOptionsItemSelected(item);
	    }
	}

	
	@SuppressLint("ValidFragment")
	private class ResetTimerDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
        	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        	builder.setMessage(R.string.dlg_reset_message);
            builder.setTitle(R.string.dlg_reset_title);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                   for(TimedElement timedElement : _timedElements) {
                	   timedElement.getChrono().reset();
                   }
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Nothing to do
                }
            });
            return builder.create();
	    }
	}
	
	
	@SuppressLint("ValidFragment")
	private class AddChronoDialogFragment extends DialogFragment {
	    @Override
	    public Dialog onCreateDialog(Bundle savedInstanceState) {
	    	final EditText newElementName = new EditText(getActivity());
	    	newElementName.setHint(R.string.dlg_add_chrono_hint);
        	AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        	builder.setMessage(R.string.dlg_add_chrono_message);
            builder.setTitle(R.string.dlg_add_chrono_title);
            builder.setView(newElementName);
            builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                	final CharSequence speakerName = newElementName.length() > 0 ?
                			newElementName.getText() :
                			getResources().getString(R.string.dlg_add_chrono_hint);
                	_timedElements.add(new TimedElement(speakerName.toString(), new Chrono()));
                }
            });
            builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
                public void onClick(DialogInterface dialog, int id) {
                    // Nothing to do
                }
            });
            return builder.create();
	    }		
	}

}

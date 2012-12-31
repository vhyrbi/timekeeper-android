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

package net.alea.timekeeper.model;

import java.util.Calendar;

public class Chrono {

	private long _elapsedTime = 0;
	private long _chronoBase = 0;	
	private boolean _isRunning = false;
	
	
	public void start() {
		if (_isRunning) return;
		_isRunning = true;
		_chronoBase = System.currentTimeMillis();
	}
	
	public void stop() {
		if (! _isRunning) return;
		_isRunning = false;
		flipChronoBase();
	}
	
	public boolean isRunning() {
		return _isRunning;
	}
	
	public void reset(long timeMillis) {
		_elapsedTime = timeMillis > 0 ? timeMillis : 0;
		_isRunning = false;
	}
	
	public void reset(Calendar calendar) {
		reset(calendar.getTimeInMillis());
	}
	
	public void reset() {
		 reset(0);
	}
	
	public long getElapsedTimeMillis() {
		if (_isRunning) {
			flipChronoBase();			
		}
		return _elapsedTime;
	}
	
	public int[] getElapsedTimeHourMinuteSecond() {
		int[] result = new int[3];
		long elapsedTime = getElapsedTimeMillis();
		long elapsedTimeSeconds = elapsedTime / 1000;
		result[0] = (int) (elapsedTimeSeconds / 3600);
		result[1] = (int) ( (elapsedTimeSeconds % 3600) / 60 );
		result[2] = (int) elapsedTimeSeconds % 60;
		return result;
	}
	
	private void flipChronoBase() {
		long newChronoBase = System.currentTimeMillis();
		_elapsedTime += newChronoBase - _chronoBase;
		_chronoBase = newChronoBase;
	}
	
	
}

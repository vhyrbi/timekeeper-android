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

public class TimedElement {

	private String _name;
	private final Chrono _chrono;
	
	public TimedElement(String name, Chrono chrono) {
		_name = name;
		_chrono = chrono;
	}
	
	public String getName() {
		return _name;
	}
	
	public void setName(String name) {
		_name = name;
	}
	
	public Chrono getChrono() {
		return _chrono;
	}
	
}

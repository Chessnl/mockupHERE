/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package COMSETsystem;

/**
 *
 * @author s162879
 */
public abstract class Event implements Comparable<Event> {

		long time;

		Event (long time) {
			this.time = time;
		}

		/**
		* function called when the Event needs to be executed
		* @return new Event if needed
		*/
		abstract Event trigger() throws Exception;

		@Override
		public int compareTo(Event o) {
			return (int) (o.time - this.time);
		}
	}

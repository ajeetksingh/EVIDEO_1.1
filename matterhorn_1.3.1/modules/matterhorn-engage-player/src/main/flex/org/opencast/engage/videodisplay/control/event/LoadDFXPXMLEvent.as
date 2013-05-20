/**
 *  Copyright 2009 The Regents of the University of California
 *  Licensed under the Educational Community License, Version 2.0
 *  (the "License"); you may not use this file except in compliance
 *  with the License. You may obtain a copy of the License at
 *
 *  http://www.osedu.org/licenses/ECL-2.0
 *
 *  Unless required by applicable law or agreed to in writing,
 *  software distributed under the License is distributed on an "AS IS"
 *  BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express
 *  or implied. See the License for the specific language governing
 *  permissions and limitations under the License.
 *
 */
package org.opencast.engage.videodisplay.control.event
{
	import flash.events.Event;
	/**
	 *   LoadDFXPXMLEvent
	 */
	public class LoadDFXPXMLEvent extends Event
	{
		public static var EVENT_NAME:String='LoadDFXPXML';

		/**
		 * Constructor
		 */
		public function LoadDFXPXMLEvent(source:String)
		{
			super(EVENT_NAME);
			_source=source;
		}

		private var _source:String;

		/**
		 * clone
		 * Override the inherited clone() method.
		 * @return LoadDFXPXMLEvent
		 */
		override public function clone():Event
		{
			return new LoadDFXPXMLEvent(source);
		}

		/**
		 * source
		 * Get the source.
		 * @return _source
		 */
		public function get source():String
		{
			return _source;
		}
	}
}



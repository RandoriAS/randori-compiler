/***
 * Copyright 2013 LTN Consulting, Inc. /dba Digital Primates®
 * 
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * 
 * 
 * @author Michael Labriola <labriola@digitalprimates.net>
 */
package behaviors {
import behaviors.tabs.MenuItem;

import randori.behaviors.List;
import randori.dom.DomWalker;
import randori.jquery.JQuery;
import randori.signal.SimpleSignal;

public class VerticalTabs extends List {
		
		public var menuItemSelected:SimpleSignal;
		
		override public function renderList():void  {
			super.renderList();
			
			var children:JQuery = decoratedNode.children();
			var first:JQuery = children.eq( 0 );
			var last:JQuery = children.eq(children.length-1);
			
			first.addClass( "first" );
			last.addClass("last");
		}
		
		override protected function onRegister():void  {
			this.listChanged.add( listChangedHandler );
			super.onRegister();
		}
		
		private function listChangedHandler( index:int, data:Object ):void {
			if (data != null) {
				menuItemSelected.dispatch(data as MenuItem);
			}
		}
		
		public function VerticalTabs(walker:DomWalker) {
			super(walker);
			
			this.menuItemSelected = new SimpleSignal();
		}
	}
}
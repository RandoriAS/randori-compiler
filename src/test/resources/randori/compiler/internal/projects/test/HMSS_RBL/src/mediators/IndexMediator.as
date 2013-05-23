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
package mediators {
import behaviors.VerticalTabs;
import behaviors.tabs.MenuItem;

import randori.async.Promise;
import randori.behaviors.AbstractMediator;
import randori.behaviors.ViewStack;

public class IndexMediator extends AbstractMediator {
		
		[View(required="true")] 
		public var viewStack:ViewStack;
		
		[View(required="true")]
		public var menu:VerticalTabs;
		
		override protected function onRegister():void {
			var menuItems:Array = new Array(
				new MenuItem( "Targets", "views/targets.html" ),
				new MenuItem( "Labs", "views/labs.html" ),
				new MenuItem( "Intel", "views/intel.html"  )
			);
			
			menu.menuItemSelected.add( menuItemSelected );
			menu.data = menuItems;
		}

		private function menuItemSelected( menuData:MenuItem ):void  {
			viewStack.popView();
			var promise:Promise = viewStack.pushView(menuData.url);
			
			promise.then( function( result:AbstractMediator ):void {
				//do something here with the new view if you want
			} );
		}
		
		public function IndexMediator() {
			super();
		}
	}
}
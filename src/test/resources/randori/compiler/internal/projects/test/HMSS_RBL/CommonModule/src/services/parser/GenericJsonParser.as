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
package services.parser {
import randori.service.parser.AbstractParser;

public class GenericJsonParser extends AbstractParser {

		public function parseResult(result:Object):Array {
			//Here we are just going to parse the result into Json.
			//We are adding some extra data just to show the concept that transforming here is totally allowed and legit
			var json:Object = JSON.parse( result as String );
			
			return json as Array;
		}
		
		public function GenericJsonParser() {
			super();
		}
	}
}
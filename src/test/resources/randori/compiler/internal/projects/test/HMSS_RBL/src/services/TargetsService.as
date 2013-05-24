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
package services {
import randori.async.Promise;
import randori.service.AbstractService;
import randori.service.ServiceConfig;
import randori.webkit.xml.XMLHttpRequest;

import services.parser.GenericJsonParser;

public class TargetsService extends AbstractService {

    public function TargetsService(xmlHttpRequest:XMLHttpRequest, config:ServiceConfig, targets:GenericJsonParser) {
        super(xmlHttpRequest);

        this.config = config;
        this.targets = targets;
        this.path = "assets/data/targets.txt";
    }

    private var config:ServiceConfig;
    private var targets:GenericJsonParser;
    private var path:String;

    public function get():Promise {
        var promise:Promise = sendRequest("GET", path);
        var parserPromise:Promise = promise.then(targets.parseResult);

        return parserPromise;
    }
}
}
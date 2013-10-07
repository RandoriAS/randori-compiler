/***
 * Copyright 2013 Teoti Graphix, LLC.
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
 * @author Michael Schmalle <mschmalle@teotigraphix.com>
 */

package randori.compiler.internal.constants;

/*
 * Copy this file to TestConstants.java and replace with your system config.
 * 
 * TestConstants.java is GIT ignored.
 */
public class TestConstants_Template
{
	// this is a path where you are expected to have cloned the following repositories from github.com/RandoriAS
	// randori-sdk (latest compiled version of the SDK)
	// randori-framework (the source code for the randori framework)
	// randori-guice-framework (the source code for the AS3 port of Guice)
	// randori-compiler (this project)
	//
	// On windows paths are expected to look like this C:/Folder/Goes/Here
	
    public static final String RandoriASFramework = "{RANDORI_GIT_PATH}";
    
    public static final String RandoriSDKBuiltSWC = TestConstants.RandoriASFramework + "/randori-sdk/randori-framework/bin/swc";
}

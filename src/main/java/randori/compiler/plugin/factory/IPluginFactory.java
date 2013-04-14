/***
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
 * @author Roland Zwaga <roland@stackandheap.com>
 */
package randori.compiler.plugin.factory;

import java.util.List;

import randori.compiler.plugin.ICompilerPlugin;

/**
 * @author Roland Zwaga <roland@stackandheap.com>
 */
public interface IPluginFactory
{
    @SuppressWarnings("rawtypes")
    <E> List<E> getPluginInstances(Class interfaze);

    void registerPlugin(Class<? extends ICompilerPlugin> api,
            Class<?> implementation);

    boolean hasPlugin(Class<? extends ICompilerPlugin> api);
}

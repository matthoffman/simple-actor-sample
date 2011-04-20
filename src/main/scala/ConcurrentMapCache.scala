/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.mattthinksso

import java.io.Serializable
import java.util.concurrent.ConcurrentHashMap

class ConcurrentMapCache extends Cache {
  /**
   * We'll use a mutable Map here, and lock it.
   */
  private val cache = new ConcurrentHashMap[String,Serializable]

  def get(key: String): Serializable = {
    return cache.get(key)
  }

  def put(key: String, value: Serializable) {
    cache.put(key, value)
  }

  def remove(key: String) {
    cache.remove(key)
  }

  def keySet() = {
    cache.keySet().asInstanceOf[Set[String]]
  }
}
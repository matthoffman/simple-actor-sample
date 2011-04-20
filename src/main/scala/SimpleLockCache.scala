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

import akka.actor.Actor
import scala.collection.mutable.Map
import java.io.Serializable
import concurrent.Lock

trait Cache {
  /**
   * In Scala style, this should return an Option.  I forget why it doesn't; I feel like I tried that first and abandoned it.
   * Currently, it will return null if not found, Java-style.
   */
  def get(key: String): Serializable
  def put(key: String, value: Serializable)
  def remove(key: String)
  def keySet(): scala.collection.Set[String]
}


class SimpleLockCache extends Cache {
  /**
   * We'll use a mutable Map here, and lock it.
   */
  private val cache: Map[String, Serializable] = Map()

  private val lock: Lock = new Lock()

  def get(key: String): Serializable = {
    lock.acquire
    try {
      return cache.get(key).orNull
    } finally {
      lock.release
    }
  }

  def put(key: String, value: Serializable) {
    lock.acquire
    try {
      cache += (key -> value)
    } finally {
      lock.release
    }
  }

  def remove(key: String) {
    lock.acquire
    try {
      cache -= key
    } finally {
      lock.release
    }
  }

  def keySet() = {
    cache.keySet
  }
}
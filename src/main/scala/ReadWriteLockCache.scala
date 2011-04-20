package com.mattthinksso

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

import actors.threadpool.locks.{ReentrantReadWriteLock, ReadWriteLock}
import scala.collection.mutable.Map
import java.io.Serializable
import concurrent.Lock


class ReadWriteLockCache extends Cache {
  /**
   * We'll use a mutable Map here, and lock it.
   */
  private val cache = Map[String,Serializable]()

  private val lock: ReadWriteLock = new ReentrantReadWriteLock()

  def get(key: String): Serializable = {
    lock.readLock.lock()
    try {
      return cache.get(key).orNull

    } finally {
      lock.readLock.unlock()
    }
  }

  def put(key: String, value: Serializable) {
    lock.writeLock.lock()
    try {
      cache += (key -> value)
    } finally {
      lock.writeLock.unlock()
    }
  }

  def remove(key: String) {
    lock.writeLock.lock()
    try {
      cache -= key
    } finally {
      lock.writeLock.unlock()
    }
  }

  def keySet() = {
    cache.keySet
  }
}
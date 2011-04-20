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
import akka.actor.Actor._
import scala.collection.mutable.Map
import java.io.Serializable
/**
 * Borrowed largely from the Lift framework
 */
class CacheActor extends Actor {
  private var cache: Map[String, Serializable] = Map()

  def receive = {
    case GetValue(key) =>
      val value = cache.get(key);
      self.reply(value)

    case DeleteValue(key) =>
      cache -= key

    case EditValue(value, key) =>
      cache += (key -> value)

    case GetKeys() =>
      self.reply(cache.keySet)


    case _ =>
  }
}


case class EditValue(e: Serializable, key: String)

case class DeleteValue(key: String)

case class GetValue(key: String)

case class GetKeys()


object CacheActor {
  lazy val cache = actorOf[CacheActor].start
}




class ActorCache extends Cache {

  def keySet() = {
    val keyOption = CacheActor.cache !! (GetKeys, 1000)
    keyOption.asInstanceOf[Option[Set[String]]].orNull
  }

  def remove(key: String) {
    CacheActor.cache ! DeleteValue(key)
  }

  def put(key: String, value: Serializable) {
    CacheActor.cache ! EditValue(value, key)
  }

  def get(key: String) = {
    val value = CacheActor.cache !! (GetValue(key), 1000)
    value.asInstanceOf[Option[Serializable]].orNull
  }
}
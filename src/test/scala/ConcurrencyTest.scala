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

import org.scalatest.junit.AssertionsForJUnit
import org.junit.Assert._
import org.junit.Test
import org.junit.Before
import java.util.concurrent._

class ConcurrencyTest extends AssertionsForJUnit {


  @Test def testSomething() {


    var test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new SimpleLockCache())
    var duration = test.run()
    println("SimpleLockCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

    test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new ActorCache())
    duration = test.run()
    println("ActorCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

    test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new SynchronizedCache())
    duration = test.run()
    println("SynchronizedCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

    test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new SimpleLockCache())
    duration = test.run()
    println("SimpleLockCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

    test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new ReadWriteLockCache())
    duration = test.run()
    println("ReadWriteLockCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

    test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new ConcurrentMapCache())
    duration = test.run()
    println("ConcurrentMapCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

    test = new Tester(numberOfThreads = 20, maxCount = 10000, iterations = 20, new ActorCache())
    duration = test.run()
    println("ActorCache took " + duration + "ms on average (excluding max and min runtime)")
    println()

  }

  class Tester(val numberOfThreads: Int, val maxCount: Int, val iterations: Int, val cache: Cache) {
    val threadPool = Executors.newFixedThreadPool(numberOfThreads);

    def runIteration(): Long = {

      val tasks: List[Callable[Int]] = (1 to numberOfThreads).toList map (x => new Callable[Int] {
        def call: Int = {
          var count: Int = 0;
          do {
            if (count % 3 == 0) {
              cache.put(Thread.currentThread.getName, count)
            } else {
              cache.get(Thread.currentThread.getName)
            }
            count += 1
          } while (count < maxCount)
          count
        }
      })

      val start = System.currentTimeMillis

      val futures = tasks.map(task => threadPool.submit(task))
      assert(futures.forall(_.get == maxCount))


      val end = System.currentTimeMillis
      // for debugging
//            println("In cache now: ")
//            cache.keySet().foreach(s => println(s + "=" + cache.get(s)))
      end - start
    }

    def run(): Long = {
      var durations: List[Long] = (1 to iterations).toList map (x => {
        runIteration()
      })
      val min = durations.foldRight(Long.MaxValue)((min, current) => min.min(current))
      println("Min value is " + min)
      val max = durations.foldRight(0L)((max, current) => max.max(current))
      println("Max value is " + max)
      printf("Variability is %.2f%%", (max / min.asInstanceOf[Double]) * 100)
      println()
      // remove the max and min
      durations.filterNot(x => x == min || x == max)

      durations.sum / durations.length
    }
  }


}
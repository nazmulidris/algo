/*
 * Copyright 2018 Nazmul Idris All rights reserved.
 *
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

package sort

import com.importre.crayon.blue
import utils.RuntimeStats
import utils.heading

fun main(args: Array<String>) {

    // bubble sort
    with(RuntimeStats()) {
        println("bubble_sort O(n^2)".heading())
        val unsortedList = mutableListOf("x", "d", "c", "b", "a")
        bubble_sort(unsortedList, this)
        print("sorted list=$unsortedList")
        println(", $this")
    }

    // insertion sort
    with(RuntimeStats()) {
        println("insertion_sort O(n^2)".heading())
        val unsortedList = mutableListOf("x", "d", "c", "b", "a")
        insertion_sort(unsortedList, this)
        print("sorted list=$unsortedList")
        println(", $this")
    }

    // merge sort
    with(RuntimeStats()) {
        println("merge_sort O(n * log(n))".heading())
        val unsortedList = mutableListOf("123", "989", "000", "981", "778", "996", "993", "781")
        val sortedList = merge_sort(unsortedList, this)
        print("sorted list=$sortedList")
        println(", $this")
    }

    // quick sort
    with(RuntimeStats()) {
        println("quick_sort O(n * log(n))".heading())
        val list = mutableListOf(100, 200, 300, 20, 30, 10, 50)
        quick_sort(list = list, stats = this)
        print("sorted list=$list")
        println(", $this")
    }

    // counting sort
    with(RuntimeStats()) {
        println("counting_sort O(n)".heading())
        val list = mutableListOf(100, 200, 15, 30, 10, 50)
        counting_sort(list, this)
        print("sorted list=$list")
        println(", $this")
    }

}

/** O(n) */
fun counting_sort(list: MutableList<Int>, stats: RuntimeStats) {
    // Create temp array to count the # occurrences of each value in the list
    // - The index of the countingArray maps to values of items in the list
    // - countingArray[index] maps to # occurrences of that value
    val countingArray = IntArray(if (list.max() == null) 0 else list.max()!! + 1)
    for (item in list) {
        stats.insertions++
        countingArray[item]++
    }

    // Regenerate the list using the countingArray
    var cursor = 0
    for (index in 0 until countingArray.size) {
        val value = index
        val numberOfOccurrences = countingArray[index]
        if (numberOfOccurrences > 0)
            repeat(numberOfOccurrences) {
                stats.insertions++
                list[cursor++] = value
            }
    }
}

/** O(n * log(n)) */
fun quick_sort(list: MutableList<Int>,
               startIndex: Int = 0,
               endIndex: Int = list.size - 1,
               stats: RuntimeStats) {
    if (startIndex < endIndex) {
        val pivotIndex = partition(list, startIndex, endIndex, stats)
        quick_sort(list, startIndex, pivotIndex - 1, stats) // Before pivot index
        quick_sort(list, pivotIndex + 1, endIndex, stats) // After pivot index
    }
}

/**
 * This function takes last element as pivot, places the pivot element at its correct position in
 * sorted list, and places all smaller (smaller than pivot) to left of pivot and all greater
 * elements to right of pivot
 */
fun partition(list: MutableList<Int>,
              startIndex: Int = 0,
              endIndex: Int = list.size - 1,
              stats: RuntimeStats): Int {
    // Element to be placed at the correct position in the list
    val pivotValue = list[endIndex]

    // Index of smaller element
    var smallerElementIndex = startIndex

    // Make a single pass through the list
    for (index in startIndex until endIndex) {
        // If current element is smaller than equal to pivotValue then swap it w/
        // the element at smallerElementIndex
        val valueAtIndex = list[index]
        stats.comparisons++
        if (valueAtIndex < pivotValue) {
            list.swap(smallerElementIndex, index)
            smallerElementIndex++
            stats.swaps++
        }
    }

    // Finally move the pivotValue into the right place on the list
    list.swap(smallerElementIndex, endIndex)
    stats.swaps++

    // Return the index just after where the pivot value ended up
    return smallerElementIndex
}

fun <T> MutableList<T>.swap(index1: Int, index2: Int) {
    val tmp = this[index1] // 'this' corresponds to the list
    this[index1] = this[index2]
    this[index2] = tmp
}

/** O(n * log(n)) */
fun merge_sort(list: MutableList<String>, stats: RuntimeStats): MutableList<String> {
    stats.operations++
    // Can't split lists anymore, so stop recursion
    val length = list.size
    if (length <= 1) return list

    // Split the list into two and recurse (divide)
    val middleIndex = length / 2
    val leftList = merge_sort(list.subList(0, middleIndex), stats)
    val rightList = merge_sort(list.subList(middleIndex, length), stats)

    // Merge the left and right lists (conquer)
    return merge(leftList, rightList, stats)
}

fun merge(leftList: MutableList<String>, rightList: MutableList<String>, stats: RuntimeStats):
        MutableList<String> {
    val result = mutableListOf<String>()
    var leftIndex = 0
    var rightIndex = 0

    while (leftIndex < leftList.size && rightIndex < rightList.size) {
        stats.comparisons++
        val lhs = leftList[leftIndex]
        val rhs = rightList[rightIndex]
        if (lhs < rhs) {
            stats.insertions++
            result.add(lhs)
            leftIndex++
        } else {
            stats.insertions++
            result.add(rhs)
            rightIndex++
        }
    }

    // Copy remaining elements of leftList (if any) into the result
    while (leftIndex < leftList.size) {
        stats.insertions++
        result.add(leftList[leftIndex])
        leftIndex++
    }

    // Copy remaining elements of rightList (if any) into the result
    while (rightIndex < rightList.size) {
        stats.insertions++
        result.add(rightList[rightIndex])
        rightIndex++
    }

    return result
}

/** O(n^2) */
fun insertion_sort(list: MutableList<String>, stats: RuntimeStats) {
    val size = list.size
    var sortedUpToIndex = 0

    for (x in 0 until size) {

        for (y in 0 until sortedUpToIndex) {
            print("\tx=$x [${list[x]}], y=$y [${list[y]}], sortedUpToIndex=$sortedUpToIndex")
            stats.operations++
            stats.comparisons++
            // CAS
            if (list[x] < list[y]) {
                list.swap(x, y)
                stats.swaps++
            }
            println(" -> ${list.toString().blue()}")
        }

        sortedUpToIndex++
    }

}

/** O(n^2) */
fun bubble_sort(list: MutableList<String>, stats: RuntimeStats) {
    val size = list.size

    for (x in 0 until size) {

        for (y in x + 1 until size) {
            print("\tx=$x [${list[x]}], y=$y [${list[y]}]")

            stats.operations++
            stats.comparisons++
            if (list[y] < list[x]) {
                stats.swaps++
                list.swap(y, x)
            }

            println(" -> ${list.toString().blue()}")

        }

    }
}
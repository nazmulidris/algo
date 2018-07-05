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

package graphs

import utils.heading
import java.util.*

/**
 * [Image of the graph](https://cdncontribute.geeksforgeeks.org/wp-content/uploads/undirectedgraph.png).
 */
fun main(args: Array<String>) {
    println("graphs".heading())

    val graph = Graph<String>()
    graph.apply {
        // Node / vertex "0"
        addEdge("0", "1")
        addEdge("0", "4")
        // Node / vertex "1"
        addEdge("1", "2")
        addEdge("1", "3")
        addEdge("1", "4")
        // Node / vertex "2"
        addEdge("2", "3")
        // Node / vertex "3"
        addEdge("3", "4")
    }
    print(graph.toString())

    println("breadth first search traversal".heading())

    print("bfs_traversal(graph, '0', 5) = ")
    println(bfs_traversal(graph, "0", 5))

    print("bfs_traversal(graph, '0', 1) = ")
    println(bfs_traversal(graph, "0", 1))

    println("depth first search traversal".heading())
    println(dfs_traversal(graph, "0"))
}

/**
 * [More info](https://www.geeksforgeeks.org/graph-and-its-representations/).
 */
class Graph<T> {

    val adjacencyList: MutableMap<T, LinkedList<T>> = mutableMapOf()

    fun addEdge(src: T, dest: T) {
        adjacencyList[src] = adjacencyList[src] ?: LinkedList()
        adjacencyList[src]?.add(dest)
        adjacencyList[dest] = adjacencyList[dest] ?: LinkedList()
        adjacencyList[dest]?.add(src)
    }

    override fun toString(): String = StringBuffer().apply {
        for (key in adjacencyList.keys) {
            append("$key -> ")
            append(adjacencyList[key]?.joinToString(prefix = "[", postfix = "]\n"))
        }
    }.toString()

}

/**
 * Breadth first traversal leverages a [Queue] (FIFO).
 */
fun <T> bfs_traversal(graph: Graph<T>, startNode: T, maxDepth: Int): String {
    // Mark all the vertices / nodes as not visited
    val visitedMap = mutableMapOf<T, Boolean>().apply {
        graph.adjacencyList.keys.forEach { node -> put(node, false) }
    }
    // Keep track of the depth of each node, so that more than maxDepth nodes aren't visited
    val depthMap = mutableMapOf<T, Int>().apply {
        graph.adjacencyList.keys.forEach { node -> put(node, Int.MAX_VALUE) }
    }

    // Create a queue for BFS
    val queue: Queue<T> = LinkedList()

    // Init step - mark the current node as visited and enqueue it
    startNode.also { node ->
        queue.add(node)
        visitedMap[node] = true
        depthMap[node] = 0
    }

    // Store the sequence in which nodes are visited, for return value
    val result = mutableListOf<T>()

    // Traverse the graph
    while (queue.isNotEmpty()) {
        // Peek and remove the item at the head of the queue
        val currentNode = queue.poll()

        // Check to make sure maxDepth is respected
        if (depthMap[currentNode]!! <= maxDepth) {

            // Get all the adjacent vertices of the node. For each of them:
            // - If an adjacent has not been visited then mark it visited
            // - Add it to the back of the queue
            val adjacencyList = graph.adjacencyList[currentNode]
            adjacencyList?.forEach { adjacentNode ->
                val currentNodeHasBeenVisited = visitedMap[adjacentNode]!!
                if (!currentNodeHasBeenVisited) {
                    visitedMap[adjacentNode] = true
                    depthMap[adjacentNode] = depthMap[currentNode]!! + 1
                    // Add item to the tail of the queue
                    queue.add(adjacentNode)
                }
            }

            // Store this for the result
            result.add(currentNode)

        }

    }

    return result.joinToString()
}

/**
 * Depth first traversal leverages a [Stack] (LIFO).
 *
 * It's possible to use recursion instead of using this iterative implementation using a [Stack].
 * Also, this algorithm is almost the same above, except for [Stack] is LIFO and [Queue] is FIFO.
 *
 * [More info](https://stackoverflow.com/a/35031174/2085356).
 */
fun <T> dfs_traversal(graph: Graph<T>, startNode: T): String {
    // Mark all the vertices / nodes as not visited
    val visitedNodeMap = mutableMapOf<T, Boolean>().apply {
        graph.adjacencyList.keys.forEach { node -> put(node, false) }
    }

    // Create a queue for DFS
    val stack: Stack<T> = Stack()

    // Init step - mark the current node as visited and enqueue it
    startNode.also { node ->
        stack.push(node)
        visitedNodeMap[node] = true
    }

    // Store the sequence in which nodes are visited, for return value
    val result = mutableListOf<T>()

    // Traverse the graph
    while (stack.isNotEmpty()) {
        // Get the top of the stack
        val currentNode = stack.pop()

        // Get all the adjacent vertices of the node. For each of them:
        // - If an adjacent has not been visited then mark it visited
        // - Add it to the top of the stack (push it to the top)
        val adjacencyList = graph.adjacencyList[currentNode]
        adjacencyList?.forEach { adjacentNode ->
            val currentNodeHasBeenVisited = visitedNodeMap[adjacentNode]!!
            if (!currentNodeHasBeenVisited) {
                visitedNodeMap[adjacentNode] = true
                stack.push(adjacentNode)
            }
        }

        // Store this for the result
        result.add(currentNode)
    }

    return result.joinToString()
}
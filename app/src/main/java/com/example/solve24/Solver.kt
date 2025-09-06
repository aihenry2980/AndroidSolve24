package com.example.solve24

import kotlin.math.abs

private data class Node(val value: Double, val expr: String, val pattern: String)

fun solve24(numbers: List<Int>): Map<String, Set<String>> {
    val placeholders = listOf("a", "b", "c", "d")
    val results = mutableMapOf<String, MutableSet<String>>()

    fun search(nodes: List<Node>) {
        if (nodes.size == 1) {
            if (abs(nodes[0].value - 24.0) < 1e-6) {
                results.getOrPut(nodes[0].pattern) { mutableSetOf() }.add(nodes[0].expr)
            }
            return
        }
        for (i in nodes.indices) {
            for (j in i + 1 until nodes.size) {
                val a = nodes[i]
                val b = nodes[j]
                val rest = nodes.filterIndexed { index, _ -> index != i && index != j }
                fun combine(value: Double, expr: String, pattern: String) {
                    search(rest + Node(value, expr, pattern))
                }
                combine(a.value + b.value, "(${a.expr}+${b.expr})", "(${a.pattern}+${b.pattern})")
                combine(a.value - b.value, "(${a.expr}-${b.expr})", "(${a.pattern}-${b.pattern})")
                combine(b.value - a.value, "(${b.expr}-${a.expr})", "(${b.pattern}-${a.pattern})")
                combine(a.value * b.value, "(${a.expr}*${b.expr})", "(${a.pattern}*${b.pattern})")
                if (abs(b.value) > 1e-6) {
                    combine(a.value / b.value, "(${a.expr}/${b.expr})", "(${a.pattern}/${b.pattern})")
                }
                if (abs(a.value) > 1e-6) {
                    combine(b.value / a.value, "(${b.expr}/${a.expr})", "(${b.pattern}/${a.pattern})")
                }
            }
        }
    }

    val initial = numbers.mapIndexed { index, n -> Node(n.toDouble(), n.toString(), placeholders[index]) }
    search(initial)
    return results
}

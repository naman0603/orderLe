package com.example.orderleapp.util

import java.util.Formatter

fun escapeUnicodeText(input: String): String {
    val b = StringBuilder(input.length)
    val f = Formatter(b)
    for (c in input.toCharArray()) {
        if (c < 128.toChar()) {
            b.append(c)
        } else {
            f.format("\\u%04x", c.toInt())
        }
    }
    return b.toString()
}
package com.x256n.desktop.crputil

data class Host(
    val host: String,
    val port: Int,
) {
    val correct: String get() = "$host:$port"

    val printable
        get() = host.padEnd(15) + ":" + port.toString().padEnd(7)

}
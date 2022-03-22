package com.x256n.desktop.crputil.model

data class Host(
    val host: String,
    val port: Int,
) {
    val printable
        get() = host.padEnd(15) + ":" + port.toString().padEnd(7)

}
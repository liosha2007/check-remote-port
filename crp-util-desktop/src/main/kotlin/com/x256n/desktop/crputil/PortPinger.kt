package com.x256n.desktop.crputil

import com.x256n.desktop.crputil.model.Host
import java.io.IOException
import java.net.InetSocketAddress
import java.net.Socket
import java.net.SocketAddress
import java.net.UnknownHostException
import java.util.regex.Pattern

class PortPinger(
    val logger: ScreenLogger,
    val onStart: suspend () -> Unit,
    val onProgress: suspend (progress: Float) -> Unit,
    val onFinish: suspend () -> Unit,
) {
    suspend fun analize(source: String, timeout: Int): List<DisplayResultItem>? {
        if (timeout < 5) {
            logger.clear()
            logger.error("Таймаут має бути не менше 5 мс")
            return null
        }

        val hosts = mutableListOf<Host>()

        val inputLines = source.trim().split('\n').filterNot { it.isBlank() || it.isEmpty() }
        if (inputLines.isEmpty()) {
            logger.clear()
            logger.error("Введіть вхідні дані")
            return null
        }

        onStart()
        onProgress(0.01f)

        val hostsWithDublicates = mutableListOf<Host>()
        inputLines.forEach { line ->
            parseHostList(line) { ip, port ->
                hostsWithDublicates.add(Host(ip, port))
            }
        }
        // Remove duplicates
        hosts.addAll(hostsWithDublicates.toSet().toMutableList())

        logger.clear()
        onProgress(1f / hosts.size)

        val pingedHosts = mutableListOf<PingedHost>()
        hosts.forEachIndexed { index, host ->
            logger.info(host.printable)
            val time = pingHost(host, timeout = timeout, logger)
            onProgress((index + 1f) / hosts.size)
            pingedHosts.add(PingedHost(host, time, time == timeout))
            if (time == timeout) {
                val printableTimeout = if (timeout % 1000 == 0) {
                    "${timeout / 1000}sec"
                } else {
                    "${timeout}ms"
                }
                logger.info("n/a (>${printableTimeout})")
            } else {
                logger.info("${time}ms")
            }
            logger.newLine()
        }

        val result: List<DisplayResultItem> = pingedHosts.sortedBy { it.time }.map { host ->
            if (host.isNotAnswered) {
                DisplayResultItem.AnswerDown(
                    ip = host.host.host,
                    port = host.host.port,
                    timeout = timeout
                )
            } else {
                DisplayResultItem.AnswerUp(
                    ip = host.host.host,
                    port = host.host.port,
                    time = host.time
                )
            }
        }
        onFinish()
        return result
    }

    private fun parseHostList(input: String, forEach: (ip: String, port: Int) -> Unit) {
        if (!input.contains(ipAndPortPattern.toRegex())) {
            return
        }
        val notIpContent = input.split(ipPattern)
        val matcherIps = ipPattern.matcher(input)
        for (index in 1..notIpContent.lastIndex) {
            if (matcherIps.find()) {
                val ip = matcherIps.group()
                val portsContent = notIpContent[index]
                val portMatcher = portPattern.matcher(portsContent)
                while (portMatcher.find()) {
                    val port = portMatcher.group().toInt()
                    if (port > 65535) {
                        continue
                    }
                    forEach(ip, port)
                }
            }
        }
    }

    data class PingedHost(val host: Host, val time: Int, val isNotAnswered: Boolean)

    private fun pingHost(host: Host, timeout: Int, logger: ScreenLogger): Int {
        var socket: Socket? = null
        var answerTime: Int
        try {
            socket = Socket()
            socket.reuseAddress = true
            val sa: SocketAddress = InetSocketAddress(host.host, host.port)
            val startTime = System.currentTimeMillis()
            socket.connect(sa, timeout)
            val endTime = System.currentTimeMillis()
            answerTime = (endTime - startTime).toInt()
        } catch (e: IOException) {
            answerTime = timeout
            if (e.message == "Connection refused") {
                logger.warning("Port ${host.port} on ${host.host} is closed.")
            }
            if (e is UnknownHostException) {
                logger.warning("node ${host.host} is unresolved.")
            }
//            if (e is SocketTimeoutException) {
//                logger.warning("Timeout while attempting to reach node ${host.host} on port ${host.port}")
//            }
        } finally {
            if (socket != null) {
                try {
//                    if (!socket.isConnected) {
//                        logger.warning("Port ${host.port} on ${host.host} is not reachable")
//                    }
                    socket.close()
                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
        }
        return answerTime
    }

    private val ipRex = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}"
    private val portRex = "(\\d{1,5})"
    private val ipAndPortPattern = Pattern.compile("$ipRex.*?$portRex")
    private val ipPattern = Pattern.compile(ipRex)
    private val portPattern = Pattern.compile(portRex)

}
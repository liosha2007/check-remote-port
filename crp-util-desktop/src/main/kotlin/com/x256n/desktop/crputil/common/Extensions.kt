package com.x256n.desktop.crputil.common

import java.util.regex.Pattern

val digitPattern = Pattern.compile("\\d+")
fun String.isDigit() = this.matches(digitPattern.toRegex())
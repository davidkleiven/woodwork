package com.github.davidkleiven.woodwork

import kotlinx.cli.ArgParser
import kotlinx.cli.ExperimentalCli

@OptIn(ExperimentalCli::class)
fun main(args: Array<String>) {
    val parser = ArgParser("woodwork")
    val circleApprox = Circle()
    parser.subcommands(circleApprox)
    parser.parse(args)
}
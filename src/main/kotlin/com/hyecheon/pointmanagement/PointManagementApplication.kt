package com.hyecheon.pointmanagement

import mu.KotlinLogging
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class PointManagementApplication

private val log = KotlinLogging.logger {}

fun main(args: Array<String>) {
	log.info { "application arguments ${args.joinToString(",")}" }
	runApplication<PointManagementApplication>(*args)
}

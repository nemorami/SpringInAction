package com.example.springinaction

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
import org.springframework.context.annotation.ComponentScan

@SpringBootApplication
class SpringInActionApplication

fun main(args: Array<String>) {
    runApplication<SpringInActionApplication>(*args)
}

package com.kahoot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class RealtimeWsApplication
fun main(args: Array<String>) {
  runApplication<RealtimeWsApplication>(*args)
}

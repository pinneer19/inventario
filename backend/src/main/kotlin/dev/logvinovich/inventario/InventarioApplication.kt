package dev.logvinovich.inventario

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class InventarioApplication

fun main(args: Array<String>) {
	runApplication<InventarioApplication>(*args)
}

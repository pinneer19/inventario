package dev.logvinovich.inventario.util

import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Component
import org.springframework.web.multipart.MultipartFile
import org.springframework.web.servlet.support.ServletUriComponentsBuilder
import java.nio.file.Files
import java.nio.file.Paths
import java.util.UUID

@Component
class ImageUtil(
    @Value("\${upload-directory}") private val uploadDir: String,
) {
    fun saveImage(file: MultipartFile): String {
        val uploadPath = Paths.get(uploadDir)
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath)
        }

        val fileName = StringBuilder(UUID.randomUUID().toString())
        file.originalFilename?.let { fileName.append("_$it") }

        val filePath = uploadPath.resolve(fileName.toString())
        Files.write(filePath, file.bytes)

        return ServletUriComponentsBuilder.fromCurrentContextPath()
            .path("/products/image/")
            .path(fileName.toString())
            .toUriString()
    }

    fun getImage(filename: String): ByteArray? {
        val filePath = Paths.get(uploadDir).resolve(filename)

        return if (!Files.exists(filePath)) {
            null
        } else {
            Files.readAllBytes(filePath)
        }
    }
}
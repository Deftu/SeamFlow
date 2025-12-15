package dev.deftu.seamflow.desktop.config

import dev.deftu.seamflow.core.DeviceId
import io.ktor.utils.io.readText
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import kotlinx.io.buffered
import kotlinx.io.files.Path
import kotlinx.io.files.SystemFileSystem
import kotlinx.io.writeString
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

@Serializable
data class AppConfig(
    val isDarkTheme: Boolean? = null,
    val persistentDeviceId: DeviceId? = null,
) {
    companion object {
        suspend fun readFrom(json: Json, path: Path): AppConfig = withContext(Dispatchers.IO) {
            val startTime = System.currentTimeMillis()
            path.parent?.let { parentPath ->
                if (!SystemFileSystem.exists(parentPath)) {
                    SystemFileSystem.createDirectories(parentPath)
                }
            }

            if (!SystemFileSystem.exists(path)) {
                writeDefault(json, path)
            }

            val content = SystemFileSystem.source(path).buffered().readText()
            val config = json.decodeFromString(serializer(), content)
            write(json, path, config) // Re-write to ensure all fields are present
            println("Loaded config in ${System.currentTimeMillis() - startTime} ms")
            config
        }

        suspend fun write(json: Json, path: Path, config: AppConfig) = withContext(Dispatchers.IO) {
            SystemFileSystem.sink(path).buffered().use { sink ->
                val content = json.encodeToString(serializer(), config)
                sink.writeString(content)
            }
        }

        suspend fun writeDefault(json: Json, path: Path) = withContext(Dispatchers.IO) {
            write(json, path, AppConfig())
        }
    }
}

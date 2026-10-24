package build.extensions.oss.gradle.plugin.examples

import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import kotlin.io.path.extension

class AsciiFilesTest {
    private val repositoryRoot = DirectoryLookup.repositoryRoot

    /**
     * Special test to fight against LLM habit of adding emoji.
     */
    @Test
    fun `Kotlin JSON and YAML files contain only ASCII symbols`() {
        val violations = Files.walk(repositoryRoot).use { paths ->
            paths
                .filter(Files::isRegularFile)
                .filter { it.extension.lowercase() in CHECKED_EXTENSIONS }
                .sorted()
                .toList()
                .flatMap(::findNonAsciiSymbols)
        }

        assertTrue(
            violations.isEmpty(),
            "Non-ASCII symbols found:\n${violations.joinToString("\n")}",
        )
    }

    private fun findNonAsciiSymbols(file: Path): List<String> = buildList {
        val relativePath = repositoryRoot.relativize(file)
        Files.readAllLines(file).forEachIndexed { lineIndex, line ->
            var columnIndex = 0
            line.codePoints().forEach { codePoint ->
                if (codePoint > ASCII_MAX_CODE_POINT) {
                    add(
                        "$relativePath:${lineIndex + 1}:${columnIndex + 1} " +
                            "(U+${codePoint.toString(16).uppercase().padStart(4, '0')})",
                    )
                }
                columnIndex++
            }
        }
    }

    private companion object {
        const val ASCII_MAX_CODE_POINT = 0x7F
        val CHECKED_EXTENSIONS = setOf("kt", "kts", "json", "yaml")
    }
}

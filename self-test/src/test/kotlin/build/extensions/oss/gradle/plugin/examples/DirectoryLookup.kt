package build.extensions.oss.gradle.plugin.examples

import java.nio.file.Files
import java.nio.file.Path

internal object DirectoryLookup {
    val repositoryRoot: Path = findRepositoryRoot(Path.of("").toAbsolutePath().normalize())

    private fun findRepositoryRoot(currentDirectory: Path): Path =
        generateSequence(currentDirectory, Path::getParent)
            .firstOrNull(::isRepositoryRoot)
            ?: error("Could not find the repository root from $currentDirectory")

    private fun isRepositoryRoot(directory: Path): Boolean =
        Files.isRegularFile(directory.resolve("renovate.json")) &&
            Files.isDirectory(directory.resolve("self-test"))
}

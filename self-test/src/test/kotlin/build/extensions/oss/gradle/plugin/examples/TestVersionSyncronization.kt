package build.extensions.oss.gradle.plugin.examples

import org.junit.jupiter.api.Assertions.assertEquals
import org.junit.jupiter.api.Assertions.assertTrue
import org.junit.jupiter.api.Test
import java.nio.file.Files
import java.nio.file.Path
import java.util.Properties
import kotlin.io.path.name
import kotlin.io.path.readText

class TestVersionSyncronization {
    private val repositoryRoot = DirectoryLookup.repositoryRoot
    private val selfTestProjectRoot = Path.of(requireNotNull(System.getProperty("self-test.project.root"))).normalize()

    private val projects: List<Path> = Files.list(repositoryRoot).use { paths ->
        paths
            .filter(Files::isDirectory)
            .filter { Files.isRegularFile(it.resolve(GRADLE_WRAPPER_PROPERTIES)) }
            .sorted()
            .toList()
    }

    @Test
    fun `all projects use the same Gradle version`() {
        assertTrue(projects.isNotEmpty(), "No Gradle projects found in $repositoryRoot")

        val versionsByProject = projects.associate { project ->
            project.name to gradleVersion(project.resolve(GRADLE_WRAPPER_PROPERTIES))
        }

        assertEquals(
            1,
            versionsByProject.values.toSet().size,
            "Projects use different Gradle versions: $versionsByProject",
        )
    }

    @Test
    fun `all examples use the same Helm plugin version`() {
        val examples = projects.filterNot { it == selfTestProjectRoot }
        assertTrue(examples.isNotEmpty(), "No example projects found in $repositoryRoot")

        val versionsByProject = examples.associate { project ->
            project.name to helmPluginVersions(project)
        }
        val projectsWithoutVersion = versionsByProject.filterValues(Set<String>::isEmpty).keys

        assertTrue(
            projectsWithoutVersion.isEmpty(),
            "Helm plugin version was not found in: $projectsWithoutVersion",
        )
        assertEquals(
            1,
            versionsByProject.values.flatten().toSet().size,
            "Projects use different Helm plugin versions: $versionsByProject",
        )
    }

    private fun gradleVersion(propertiesFile: Path): String {
        val properties = Properties().apply {
            Files.newBufferedReader(propertiesFile).use(::load)
        }
        val distributionUrl = requireNotNull(properties.getProperty("distributionUrl")) {
            "distributionUrl is missing from $propertiesFile"
        }

        return requireNotNull(GRADLE_VERSION.find(distributionUrl)?.groupValues?.get(1)) {
            "Could not parse the Gradle version from $distributionUrl"
        }
    }

    private fun helmPluginVersions(project: Path): Set<String> {
        val buildFiles = Files.walk(project).use { paths ->
            paths
                .filter(Files::isRegularFile)
                .filter { it.name == "build.gradle" || it.name == "build.gradle.kts" }
                .toList()
        }

        return buildFiles
            .flatMap { buildFile -> HELM_PLUGIN_VERSION.findAll(buildFile.readText()).map { it.groupValues[1] } }
            .toSet()
    }

    private companion object {
        const val GRADLE_WRAPPER_PROPERTIES = "gradle/wrapper/gradle-wrapper.properties"
        val GRADLE_VERSION = Regex("""gradle-(.+)-(?:all|bin)\.zip$""")
        val HELM_PLUGIN_VERSION = Regex(
            """id\s*(?:\(\s*)?["']io\.github\.build-extensions-oss\.helm(?:-[^"']+)?["']\s*\)?\s*version\s*["']([^"']+)["']""",
        )
    }
}

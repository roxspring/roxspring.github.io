import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.time.LocalDateTime

open class BuildSiteTask : DefaultTask() {

    @TaskAction
    fun run() {
        val siteDir = project.buildDir.resolve("site")
        siteDir.mkdirs()

        val file = siteDir.resolve("index.html")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText(
            """
            <html>
            <p>Generated: ${LocalDateTime.now()}</p>
            </html>
            """.trimIndent()
        )
    }
}

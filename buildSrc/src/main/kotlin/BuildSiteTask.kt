import org.gradle.api.DefaultTask
import org.gradle.api.tasks.TaskAction
import java.time.LocalDateTime

open class BuildSiteTask : DefaultTask() {

    @TaskAction
    fun run() {
        val mdDir = project.projectDir.resolve("src/main/md")
        val siteDir = project.buildDir.resolve("site")

        pagesOf(mdDir)
            .forEach { page ->
                println("Writing: ${page.id}.html")

                val htmlFile = siteDir.resolve(page.id + ".html")
                htmlFile.parentFile.mkdirs()
                htmlFile.writeText(
                    """
                    <html>
                    <article>
                    ${page.toHTML()}
                    </article>
                    <p>Generated: ${LocalDateTime.now()}</p>
                    </html>
                    """.trimIndent()
                )
            }

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

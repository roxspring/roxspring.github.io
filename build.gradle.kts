import java.time.LocalDateTime

task("build") {
    doLast {



        val dir = buildDir.resolve("site")
        dir.mkdirs()

        val file = dir.resolve("index.html")
        file.parentFile.mkdirs()
        file.createNewFile()
        file.writeText("""
            <html>
            <p>Generated: ${LocalDateTime.now()}</p>
            </html>
            """.trimIndent()
        )
    }
}

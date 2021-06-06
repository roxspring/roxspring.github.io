import com.vladsch.flexmark.ext.anchorlink.AnchorLinkExtension
import com.vladsch.flexmark.ext.autolink.AutolinkExtension
import com.vladsch.flexmark.ext.gfm.strikethrough.StrikethroughExtension
import com.vladsch.flexmark.ext.tables.TablesExtension
import com.vladsch.flexmark.ext.yaml.front.matter.AbstractYamlFrontMatterVisitor
import com.vladsch.flexmark.ext.yaml.front.matter.YamlFrontMatterExtension
import com.vladsch.flexmark.ext.youtube.embedded.YouTubeLinkExtension
import com.vladsch.flexmark.html.HtmlRenderer
import com.vladsch.flexmark.parser.Parser
import com.vladsch.flexmark.util.ast.Document
import com.vladsch.flexmark.util.data.MutableDataSet
import java.io.File
import java.io.StringWriter

/** Parse the markdown pages within some directory */
fun pagesOf(dir: File): Sequence<Page> = dir
    .walkBottomUp()
    .filter { it.isFile }
    .filter { it.extension == "md" }
    .map { pageOf(dir, it) }

/** Parse the markdown file within the specified ancestor directory */
fun pageOf(dir: File, file: File): Page =
    Page(src = file, id = File(file.parent, file.nameWithoutExtension).toRelativeString(dir))

/**
 * Represents a parsed markdown page.
 */
data class Page(
    val src: File,
    val id: String,
) {
    private val yamlFrontMatterExtension = YamlFrontMatterExtension.create()
    private val options = MutableDataSet()
        .set(
            Parser.EXTENSIONS,
            listOf(
                yamlFrontMatterExtension,
                YouTubeLinkExtension.create(),
                AnchorLinkExtension.create(),
                AutolinkExtension.create(),
                TablesExtension.create(),
                StrikethroughExtension.create(),
            )
        )
    private val doc: Document

    init {
        val parser: Parser = Parser.builder(options).build()
        doc = src.reader().use {
            parser.parseReader(it)
        }

        val frontMatterVisitor = AbstractYamlFrontMatterVisitor()
        frontMatterVisitor.visit(doc)
    }

    fun toHTML(): String = StringWriter()
        .also { writer ->
            writer.use {
                HtmlRenderer.builder(options).build().render(doc, it)
            }
        }
        .toString()
}

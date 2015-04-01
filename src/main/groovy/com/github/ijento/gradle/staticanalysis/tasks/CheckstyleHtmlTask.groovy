package com.github.ijento.gradle.staticanalysis.tasks

import com.github.ijento.gradle.staticanalysis.internal.HtmlReportsContainer
import com.github.ijento.gradle.staticanalysis.internal.HtmlReportsImpl
import org.gradle.api.DefaultTask
import org.gradle.api.reporting.Reporting
import org.gradle.api.tasks.*
import org.gradle.internal.reflect.Instantiator

import javax.inject.Inject
import javax.xml.transform.Transformer
import javax.xml.transform.TransformerFactory
import javax.xml.transform.stream.StreamResult
import javax.xml.transform.stream.StreamSource

/**
 * Task which converts a checkstyle XML report to a HTML report using the defined XSL file.
 *
 * The XSL file can be overriden by providing it as an input
 * The XML report MUST be provided to the task as a file, commonly this would be the XML report from the checkstyleYask
 * <p>
 * Sample:
 *
 * <pre autoTested=''>
 *     checkstyleHtmlTask {*         xmlReport = checkstyleTask.reports.xml.destination
 *         sxlCheckstyleFile = file(<a new file>) // Optional
 *}* </pre>
 *
 * @author: Ollie Freeman
 * @date: 21/03/15
 */
class CheckstyleHtmlTask extends DefaultTask implements Reporting<HtmlReportsContainer> {

    @Input
    File xmlReport

    private static final String DEFAULT_XSL_CHECKSTYLE = '/checkstyle-noframes-sorted.xsl'

    @InputFile
    @Optional
    File xslCheckstyleFile

    protected Transformer transformer

    @Inject
    protected Instantiator getInstantiator() {
        throw new UnsupportedOperationException()
    }

    CheckstyleHtmlTask() {
        reports = instantiator.newInstance(HtmlReportsImpl, this)

    }

    @Nested
    private final HtmlReportsImpl reports

    @Override
    HtmlReportsContainer getReports() {
        reports
    }

    @Override
    HtmlReportsContainer reports(Closure closure) {
        reports.configure(closure)
    }

    @TaskAction
    void convertXmlToHtml() {
        if (reports.html.destination) {

            InputStream is
            try {
                if (xslCheckstyleFile) {
                    logger.info("Using XSL file {}", xslCheckstyleFile)
                    is = new FileInputStream(xslCheckstyleFile)
                } else {
                    logger.info("Using XSL resource")
                    is = CheckstyleHtmlTask.class.getResourceAsStream(DEFAULT_XSL_CHECKSTYLE)
                }

                if (is) {
                    transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(is))
                } else {
                    logger.error("Could not load transformer")
                    state.skipped("SKIPPED: No transformer loaded")
                }
            } finally {
                if (is) is.close()
            }

            logger.info("Transforming {} to {}", xmlReport, reports.html.destination)

            reports.html.destination.parentFile.mkdirs()
            if (xmlReport && xmlReport.exists()) {
                transformer.transform(new StreamSource(new FileInputStream(xmlReport)), new StreamResult(reports.html.destination))
            } else {
                this.state.skipped("SKIPPED: No report to transform")
            }
        } else {
            state.skipped("SKIPPED: HTML report destination is undefined")
        }

    }
}

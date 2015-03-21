package com.github.ijento.gradle.staticanalysis.tasks

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
 * @author: Ollie
 * @date: 21/03/15
 */
class CheckstyleHtmlTask extends DefaultTask implements Reporting<HtmlReportsContainer>{

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

    CheckstyleHtmlTask(){
        reports = instantiator.newInstance(HtmlReportsImpl,this)

        InputStream is
        try {
            if(xslCheckstyleFile){
                logger.trace("Using file {}",xslCheckstyleFile)
                is = new FileInputStream(xslCheckstyleFile)
            }else {
                logger.trace("Using resource")
                is = CheckstyleHtmlTask.class.getResourceAsStream(DEFAULT_XSL_CHECKSTYLE)
            }

            if(is) {
                transformer = TransformerFactory.newInstance().newTransformer(new StreamSource(is))
            }else {
                logger.error("Could not load transformer")
                state.skipped("SKIPPED: No transformer loaded")
            }
        }finally {
            if(is) is.close()
        }
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
    void convertXmlToHtml(){

        logger.trace("Transforming {} to {}", xmlReport, reports.html.destination)

        if (xmlReport.exists()) {
            transformer.transform(new StreamSource(new FileInputStream(xmlReport)), new StreamResult(reports.html.destination))
        }else{
            this.state.skipped("SKIPPED: No report to transform")
        }

    }
}

package com.github.ijento.gradle.staticanalysis.tasks

import org.gradle.api.DomainObjectCollection
import org.gradle.api.reporting.ReportContainer
import org.gradle.api.reporting.SingleFileReport
import org.gradle.api.reporting.internal.TaskGeneratedSingleFileReport

/**
 * @author: Ollie
 * @date: 21/03/15
 */
interface HtmlReportsContainer extends DomainObjectCollection<TaskGeneratedSingleFileReport> {
    SingleFileReport getHtml();
}

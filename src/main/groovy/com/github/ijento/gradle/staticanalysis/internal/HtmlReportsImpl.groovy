package com.github.ijento.gradle.staticanalysis.internal

import org.gradle.api.NamedDomainObjectSet
import org.gradle.api.Task
import org.gradle.api.reporting.SingleFileReport
import org.gradle.api.reporting.internal.TaskGeneratedSingleFileReport
import org.gradle.api.reporting.internal.TaskReportContainer

/**
 * Implementation of the Html Reports container.
 *
 * @author: Ollie Freeman
 * @date: 21/03/15
 */
class HtmlReportsImpl extends TaskReportContainer<TaskGeneratedSingleFileReport> implements HtmlReportsContainer {


    HtmlReportsImpl(Task task) {
        super(SingleFileReport.class, task)
        add(TaskGeneratedSingleFileReport.class, "html", task);
    }

    @Override
    SingleFileReport getHtml() {
        getByName('html')
    }
}

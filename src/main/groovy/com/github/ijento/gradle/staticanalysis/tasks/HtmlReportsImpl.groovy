package com.github.ijento.gradle.staticanalysis.tasks

import org.gradle.api.DomainObjectCollection
import org.gradle.api.Task
import org.gradle.api.reporting.SingleFileReport
import org.gradle.api.reporting.internal.TaskGeneratedSingleDirectoryReport
import org.gradle.api.reporting.internal.TaskGeneratedSingleFileReport
import org.gradle.api.reporting.internal.TaskReportContainer

/**
 * @author: Ollie
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

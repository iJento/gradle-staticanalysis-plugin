package com.github.ijento.gradle.staticanalysis.internal

import org.gradle.api.DomainObjectCollection
import org.gradle.api.reporting.ReportContainer
import org.gradle.api.reporting.SingleFileReport
import org.gradle.api.reporting.internal.TaskGeneratedSingleFileReport

/**
 * Html Reports Container for holding the HTML report.
 *
 * This should extend {@link ReportContainer} however if it does the compilation fails with the following error
 * <pre autoTested=''>
 * /Users/Ollie/Coding/github/gradle-staticanalysis-plugin/src/main/groovy/com/github/ijento/gradle/staticanalysis/internal/HtmlReportsImpl.groovy: -1: The return type of org.gradle.api.NamedDomainObjectCollection matching(org.gradle.api.specs.Spec) in org.gradle.api.internal.DefaultNamedDomainObjectCollection is incompatible with org.gradle.api.NamedDomainObjectSet in org.gradle.api.NamedDomainObjectSet
 * . At [-1:-1]  @ line -1, column -1.
 * </pre>
 * To prevent this we instead extend {@link DomainObjectCollection}.
 *
 * @author: Ollie Freeman
 * @date: 21/03/15
 */
interface HtmlReportsContainer extends DomainObjectCollection<TaskGeneratedSingleFileReport> {
    SingleFileReport getHtml();
}

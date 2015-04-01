package com.github.ijento.gradle.staticanalysis.step_definitions

import com.github.ijento.gradle.staticanalysis.tasks.CheckstyleHtmlTask
import cucumber.api.PendingException
import cucumber.api.groovy.EN
import cucumber.api.groovy.Hooks
import org.gradle.api.Project
import org.gradle.api.Task
import org.gradle.testfixtures.ProjectBuilder
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import static org.junit.Assert.assertEquals
import static org.junit.Assert.fail

/**
 * @author: Ollie Freeman
 * @date: 31/03/15
 */

this.metaClass.mixin(Hooks)
this.metaClass.mixin(EN)

Before(){
    project = ProjectBuilder.builder().build()
}


Given(~/^I initialise the task with the (.+) file$/){ String xslFile ->
    def file = new File("src/cucumber/resources/$xslFile")

    testTask = project.task([type: CheckstyleHtmlTask], 'testTask' ){
        if (xslFile == 'anotherXSLFile.xsl') {
            project.logger.quiet("Using {}",xslFile)
            xslCheckstyleFile = file
        }
    }
}

Given(~/^I load the XML file (.+)$/) { String xmlReport ->
    testTask.xmlReport = new File("src/cucumber/resources/$xmlReport")

}

When(~/^I run the task$/) { ->
    testTask.convertXmlToHtml()
}

Then(~/^The resulting file should equal (.+)$/) { String expected ->
    def result = new File("${testTask.reports.html.destination}")

    def expectedFile = new File("src/cucumber/resources/$expected")

    assertEquals(expectedFile.text, result.text)

}

Given(~/^I set the HTML report file to (.+)$/){String report ->
    testTask.reports.html.destination = new File("build/cucumber/results/$report").absolutePath
}

Then(~/^The state should be (.+) with message '(.+)'$/){ String expectedState, String expectedMessage ->
    switch (expectedState) {
    case 'skipped':
        assert testTask.state.skipped
        break
    case 'didWork':
        assert testTask.state.didWork
        break
    case 'executed':
       assert testTask.state.executed
    }
    assertEquals("Message should equal", expectedMessage, testTask.state.skipMessage)
}


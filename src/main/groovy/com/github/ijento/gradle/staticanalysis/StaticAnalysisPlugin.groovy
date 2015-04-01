package com.github.ijento.gradle.staticanalysis

import com.github.ijento.gradle.staticanalysis.tasks.CheckstyleHtmlTask
import org.gradle.api.Plugin
import org.gradle.api.Project
import org.gradle.api.plugins.quality.Checkstyle
import org.gradle.api.plugins.quality.FindBugs
import org.gradle.api.plugins.quality.Pmd
import org.gradle.api.reporting.Reporting
import org.gradle.logging.ConsoleRenderer

/**
 * The static analysis tools do not generate 1 task which can be run from gradle but 1 per sourceset, these tasks
 * add the ability to run all of the individual SA tools in one go without running the others.
 * We can also have the checkstyle task performing the html conversion rather than the check task.
 *
 * @author: Ollie Freeman
 * @date: 19/03/15
 */
public class StaticAnalysisPlugin implements Plugin<Project> {


    @Override
    void apply(Project pluginProject) {

        pluginProject.extensions.add("staticAnalysis", StaticAnalysisExtension)
        pluginProject.apply(plugin: 'base')
        pluginProject.rootProject.apply(plugin: 'build-dashboard')

        pluginProject.task('staticAnalysis', group: 'Verification',
                description: 'Runs the checkstyle, findbugs, pmd and cpdCheck static analysis'
        ) {
            pluginProject.tasks.check.dependsOn it
            doLast {
                File dashboard = project.file("${project.buildDashboard.reports.html.destination}/index.html")
                String reportUrl = new ConsoleRenderer().asClickableFileUrl(dashboard)
                pluginProject.logger.quiet("Report dashboard available at $reportUrl. (Ctrl+click the URL to open)")
            }
        }

        pluginProject.afterEvaluate {

            if (pluginProject.staticAnalysis.applyAnalysisToMain) {

                pluginProject.logger.quiet("Applying full analysis to plugin project")
                applyAnalysisTasks(pluginProject, null, pluginProject.staticAnalysis)

            } else {

                if (pluginProject.staticAnalysis.addCheckstyle) {
                    pluginProject.task('checkstyle', group: 'Verification',
                            description: 'Runs the checkstyle static analysis'
                    ) { pluginProject.tasks.staticAnalysis.dependsOn it }
                }

                if (pluginProject.staticAnalysis.addFindbugs) {
                    pluginProject.task('findbugs', group: 'Verification',
                            description: 'Runs the findbugs static analysis'
                    ) { pluginProject.tasks.staticAnalysis.dependsOn it }
                }

                if (pluginProject.staticAnalysis.addPmd) {
                    pluginProject.task('pmd', group: 'Verification',
                            description: 'Runs the pmd static analysis'
                    ) { pluginProject.tasks.staticAnalysis.dependsOn it }
                }
            }

            if (pluginProject.staticAnalysis.addCpd) {
                pluginProject.apply(plugin: 'cpd')
                pluginProject.tasks.cpdCheck {
                    group 'verification'
                    pluginProject.subprojects.findAll { p -> p.hasProperty('sourceSets') }.each { p ->
                        source = p.sourceSets.main.allJava
                    }
                    ignoreFailures = true
                    mustRunAfter pluginProject.tasks.assemble
                    pluginProject.tasks.staticAnalysis.dependsOn it
                }
            }

            pluginProject.subprojects {
                applyAnalysisTasks(it, pluginProject, pluginProject.staticAnalysis)
            }

        }
    }

    void applyAnalysisTasks(Project project, Project pluginProject, StaticAnalysisExtension staticAnalysisExt){
        project.apply(plugin: 'java')

        if(pluginProject) {
            project.task('staticAnalysis', group: 'verification',
                    description: 'Runs the checkstyle, findbugs, pmd and cpdCheck static analysis') {
                pluginProject.tasks.staticAnalysis.dependsOn it
                project.tasks.check.dependsOn it
            }
        }

        /**
         * Checkstyle task
         */
        if(staticAnalysisExt.addCheckstyle) {

            project.apply(plugin: 'checkstyle')

            project.checkstyle {
                toolVersion staticAnalysisExt.checkstyleToolVersion
                ignoreFailures true
                showViolations false
                if(staticAnalysisExt.baseReportingDir) reportsDir project.file("$staticAnalysisExt.baseReportingDir/checkstyle/${project.name}")
                if(staticAnalysisExt.checkstyleConfigFile) configFile staticAnalysisExt.checkstyleConfigFile
            }

            if(staticAnalysisExt.checkstyleOverrides) project.checkstyle staticAnalysisExt.checkstyleOverrides

            project.tasks.withType(Checkstyle){checkstyleTask ->
                project.task("${checkstyleTask.name}Html", type: CheckstyleHtmlTask){
                    xmlReport checkstyleTask.reports.xml.destination
                    reports.html{
                        enabled true
                        destination pluginProject.file(checkstyleTask.reports.xml.destination.absolutePath.replace('xml', 'html'))
                    }
                    outputs.upToDateWhen{false}
                    checkstyleTask.finalizedBy it
                }
            }

            project.task('checkstyle',group: 'verification',dependsOn: project.tasks.withType(Checkstyle),
                    description: 'Runs the checkstyle static analysis'){
                if(pluginProject) pluginProject.tasks.checkstyle.dependsOn it
                project.tasks.staticAnalysis.dependsOn it
            }
        }

        /**
         * Findbugs task
         */
        if(staticAnalysisExt.addFindbugs) {

            project.apply(plugin: 'findbugs')

            project.findbugs {
                ignoreFailures true
                reportLevel "low"
                if(staticAnalysisExt.baseReportingDir) reportsDir project.file("$staticAnalysisExt.baseReportingDir/findbugs/${project.name}")
                if(staticAnalysisExt.findbugsExcludeFilter) excludeFilter staticAnalysisExt.findbugsExcludeFilter
            }

            if(staticAnalysisExt.findbugsOverrides) project.findbugs staticAnalysisExt.findbugsOverrides

            project.dependencies.add('compile',[group: 'net.sourceforge.findbugs', name:'annotations', version: '1+'])

            if (staticAnalysisExt.findbugsHtmlReport) {
                project.tasks.withType(FindBugs) {
                    reports {
                        xml.enabled false
                        html.enabled true
                    }
                }
            }

            project.task('findbugs',group: 'verification', dependsOn: project.tasks.withType(FindBugs),
                    description: 'Runs the findbugs static analysis'){
                if(pluginProject) pluginProject.tasks.findbugs.dependsOn it
                project.tasks.staticAnalysis.dependsOn it
            }
        }

        /**
         * Pmd task
         */
        if(staticAnalysisExt.addPmd) {

            project.apply(plugin: 'pmd')

            project.pmd {
                ignoreFailures true
                toolVersion = staticAnalysisExt.pmdToolVersion
                if(staticAnalysisExt.baseReportingDir) reportsDir project.file("$staticAnalysisExt.baseReportingDir/pmd/${project.name}")
                if(staticAnalysisExt.pmdRuleSetFiles) ruleSetFiles = staticAnalysisExt.pmdRuleSetFiles
            }

            if(staticAnalysisExt.pmdOverrides) project.pmd staticAnalysisExt.pmdOverrides

            project.dependencies.add('pmd', [group: 'net.sourceforge.pmd', name:'pmd-core', version: staticAnalysisExt.pmdToolVersion])
            project.dependencies.add('pmd', [group: 'net.sourceforge.pmd', name:'pmd-java', version: staticAnalysisExt.pmdToolVersion])

            project.task('pmd',group: 'verification',dependsOn: project.tasks.withType(Pmd),
                    description: 'Runs the pmd static analysis'){
                if(pluginProject) pluginProject.tasks.pmd.dependsOn it
                project.tasks.staticAnalysis.dependsOn it
            }
        }
    }
}

package com.github.ijento.gradle.staticanalysis

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection

/**
 * The static analysis extension which is added by the plugin.
 * All values can be overridden using the standard Gradle DSL for setting extension properties
 * <p>
 * Sample:
 *
 * <pre>
 * staticAnalysis {
 *     addCheckstyle = true
 *     addPmd = true
 *     addFindbugs = true
 *     addCpd = true
 *
 *     applyAnalysisToMain = false
 * }
 * </pre>
 *
 * @author: Ollie Freeman
 * @date: 19/03/15
 */
class StaticAnalysisExtension {

    /**
     * Add checkstyle tasks
     */
    boolean addCheckstyle = true

    /**
     * Add pmd tasks
     */
    boolean addPmd = true

    /**
     * Add findbugs tasks
     */
    boolean addFindbugs = true

    /**
     * Add cpd tasks
     */
    boolean addCpd = true

    /**
     * Apply ststic analysis tasks to the root project
     */
    boolean applyAnalysisToMain = false

    /**
     * Output findbugs report as HTML rather than XML
     */
    boolean findbugsHtmlReport = true

    /**
     * Pmd tool version to use
     */
    String pmdToolVersion = '5.+'

    /**
     * Checkstyle tool version to use
     */
    String checkstyleToolVersion = '6.+'

    /**
     * Checkstyle configuration file
     */
    File checkstyleConfigFile

    /**
     * Findbugs exclusion filter file
     */
    File findbugsExcludeFilter

    /**
     * Pmd rule set file collection
     */
    ConfigurableFileCollection pmdRuleSetFiles

    /**
     * Base reporting directory to save static analysis reports to
     */
    File baseReportingDir

    /**
     * List of project names to ignore when adding static analysis
     */
    List ignoreProjects = []

    /**
     * Closure to pass to checkstyle configuration
     */
    Closure checkstyleOverrides

    /**
     * Closure to pass to pmd configuration
     */
    Closure pmdOverrides

    /**
     * Closure to pass to findbugs configurations
     */
    Closure findbugsOverrides
}

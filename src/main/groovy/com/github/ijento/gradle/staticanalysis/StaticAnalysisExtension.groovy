package com.github.ijento.gradle.staticanalysis

import org.gradle.api.file.ConfigurableFileCollection
import org.gradle.api.file.FileCollection

/**
 * @author: Ollie Freeman
 * @date: 19/03/15
 */
class StaticAnalysisExtension {

    boolean addCheckstyle = true
    boolean addPmd = true
    boolean addFindbugs = true
    boolean addCpd = true
    boolean applyAnalysisToMain = false
    boolean findbugsHtmlReport = true

    String pmdToolVersion = '5.+'
    String checkstyleToolVersion = '6.+'

    File checkstyleConfigFile
    File findbugsExcludeFilter
    ConfigurableFileCollection pmdRuleSetFiles
    File baseReportingDir

    Closure checkstyleOverrides
    Closure pmdOverrides
    Closure findbugsOverrides
}

/*
 * This file is part of findbugs4sbt.
 * 
 * Copyright (c) 2010, 2011 Joachim Hofer
 * All rights reserved.
 *
 * This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 */
package de.johoop.findbugs4sbt

import sbt._
import Keys._
import Project.Initialize

import ReportType._
import Effort._

import scala.xml.Node
import java.io.File

case class PathSettings(targetPath: File, reportName: String, analyzedPath: File)

case class FilterSettings(includeFilters: Option[Node], excludeFilter: Option[Node])

case class MiscSettings(reportType: ReportType, effort: Effort, onlyAnalyze: Option[Seq[String]], maxMemory: Int, 
  analyzeNestedArchives: Boolean, sortReportByClassNames: Boolean)

private[findbugs4sbt] trait Settings extends Plugin {

  val findbugs = TaskKey[Unit]("findbugs")
  
  val findbugsPathSettings = TaskKey[PathSettings]("findbugs-path-settings")
  val findbugsFilterSettings = TaskKey[FilterSettings]("findbugs-filter-settings")
  val findbugsMiscSettings = TaskKey[MiscSettings]("findbugs-misc-settings")
  
  /** Output path for FindBugs reports. Defaults to <code>target / "findBugs"</code>. */
  val findbugsTargetPath = SettingKey[File]("findbugs-target-path")
  
  /** Name of the report file to generate. Defaults to <code>"findbugs.xml"</code> */
  val findbugsReportName = SettingKey[String]("findbugs-report-name")

  /** The path to the classes to be analyzed. Defaults to <code>mainCompilePath</code>. */
  val findbugsAnalyzedPath = SettingKey[File]("findbugs-analyzed-path")

  /** Type of report to create. Defaults to <code>ReportType.Xml</code>. */
  val findbugsReportType = SettingKey[ReportType]("findbugs-report-type")
  
  /** Effort to put into the static analysis. Defaults to <code>ReportType.Medium</code>.*/
  val findbugsEffort = SettingKey[Effort]("findbugs-effort")
  
  /** Optionally, define which packages/classes should be analyzed (<code>None</code> by default) */
  val findbugsOnlyAnalyze = SettingKey[Option[Seq[String]]]("findbugs-only-analyze")

  /** Maximum amount of memory to allow for FindBugs (in MB). */
  val findbugsMaxMemory = SettingKey[Int]("findbugs-max-memory")

  /** Whether FindBugs should analyze nested archives or not. Defaults to <code>true</code>. */
  val findbugsAnalyzeNestedArchives = SettingKey[Boolean]("findbugs-analyze-nested-archives")

  /** Whether the reported bug instances should be sorted by class name or not. Defaults to <code>false</code>.*/
  val findbugsSortReportByClassNames = SettingKey[Boolean]("findbugs-sort-report-by-class-names")

  /** Optional filter file XML content defining which bug instances to include in the static analysis. 
    * <code>None</code> by default. */ 
  val findbugsIncludeFilters = SettingKey[Option[Node]]("findbugs-include-filter")

  /** Optional filter file XML content defining which bug instances to exclude in the static analysis. 
    * <code>None</code> by default. */ 
  val findbugsExcludeFilters = SettingKey[Option[Node]]("findbugs-exclude-filter")

  def filterSettingsTask: Initialize[Task[FilterSettings]] = ( findbugsIncludeFilters, findbugsExcludeFilters) map {
    (include, exclude) => FilterSettings(include, exclude)
  }
  
  def pathSettingsTask: Initialize[Task[PathSettings]] = (findbugsTargetPath, findbugsReportName, findbugsAnalyzedPath) map {
    (targetPath, reportName, analyzedPath) => PathSettings(targetPath, reportName, analyzedPath)
  }
  
  def miscSettingsTask: Initialize[Task[MiscSettings]] = (findbugsReportType, findbugsEffort, findbugsOnlyAnalyze, findbugsMaxMemory, findbugsAnalyzeNestedArchives, findbugsSortReportByClassNames) map { 
    (p1, p2, p3, p4, p5, p6) => MiscSettings(p1, p2, p3, p4, p5, p6)
  }
}

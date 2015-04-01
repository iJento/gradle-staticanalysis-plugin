<xsl:stylesheet xmlns:xsl="http://www.w3.org/1999/XSL/Transform" version="1.0">
  <!--
     Licensed to the Apache Software Foundation (ASF) under one or more
     contributor license agreements.  See the NOTICE file distributed with
     this work for additional information regarding copyright ownership.
     The ASF licenses this file to You under the Apache License, Version 2.0
     (the "License"); you may not use this file except in compliance with
     the License.  You may obtain a copy of the License at

         http://www.apache.org/licenses/LICENSE-2.0

     Unless required by applicable law or agreed to in writing, software
     distributed under the License is distributed on an "AS IS" BASIS,
     WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
     See the License for the specific language governing permissions and
     limitations under the License.
  -->

  <xsl:output method="html" indent="yes"/>
  <xsl:decimal-format decimal-separator="." grouping-separator=","/>

  <xsl:key name="files" match="file" use="@name"/>

  <!-- Checkstyle XML Style Sheet by Stephane Bailliez <sbailliez@apache.org>         -->
  <!-- Part of the Checkstyle distribution found at http://checkstyle.sourceforge.net -->
  <!-- Usage (generates checkstyle_report.html):                                      -->
  <!--    <checkstyle failonviolation="false" config="${check.config}">               -->
  <!--      <fileset dir="${src.dir}" includes="**/*.java"/>                          -->
  <!--      <formatter type="xml" toFile="${doc.dir}/checkstyle_report.xml"/>         -->
  <!--    </checkstyle>                                                               -->
  <!--    <style basedir="${doc.dir}" destdir="${doc.dir}"                            -->
  <!--            includes="checkstyle_report.xml"                                    -->
  <!--            style="${doc.dir}/checkstyle-noframes-sorted.xsl"/>                 -->

  <xsl:template match="checkstyle">
    <html>
      <head>
        <title>A bad XSL</title>
      </head>
      <body>
        <!-- Summary part -->
        <xsl:apply-templates select="." mode="summary"/>
        <hr size="1" width="100%" align="left"/>
      </body>
    </html>
  </xsl:template>

  <xsl:template match="checkstyle" mode="summary">
    <h3>Summary</h3>
    <xsl:variable name="fileCount"
                  select="count(file[@name and generate-id(.) = generate-id(key('files', @name))])"/>
    <xsl:variable name="errorCount" select="count(file/error)"/>
    <table class="log" border="0" cellpadding="5" cellspacing="2" width="100%">
      <tr>
        <th>Files</th>
        <th>Errors</th>
      </tr>
      <tr>
        <xsl:call-template name="alternated-row"/>
        <td>
          <xsl:value-of select="$fileCount"/>
        </td>
        <td>
          <xsl:value-of select="$errorCount"/>
        </td>
      </tr>
    </table>
  </xsl:template>

  <xsl:template name="alternated-row">
    <xsl:attribute name="class">
      <xsl:if test="position() mod 2 = 1">a</xsl:if>
      <xsl:if test="position() mod 2 = 0">b</xsl:if>
    </xsl:attribute>
  </xsl:template>
</xsl:stylesheet>



/**
 * Copyright (c) 2015, 2024 itemis AG (http://www.itemis.eu) and others.
 * This program and the accompanying materials are made available under the
 * terms of the Eclipse Public License 2.0 which is available at
 * http://www.eclipse.org/legal/epl-2.0.
 * 
 * SPDX-License-Identifier: EPL-2.0
 */
package org.eclipse.xtext.xtext.wizard;

import com.google.common.base.Charsets;
import com.google.common.collect.Iterables;
import com.google.common.io.Resources;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Objects;
import java.util.Set;
import org.eclipse.xtend.lib.annotations.FinalFieldsConstructor;
import org.eclipse.xtend2.lib.StringConcatenation;
import org.eclipse.xtext.util.JavaVersion;
import org.eclipse.xtext.util.XtextVersion;
import org.eclipse.xtext.xbase.lib.CollectionLiterals;
import org.eclipse.xtext.xbase.lib.Exceptions;
import org.eclipse.xtext.xbase.lib.Functions.Function1;
import org.eclipse.xtext.xbase.lib.IterableExtensions;
import org.eclipse.xtext.xbase.lib.ListExtensions;
import org.eclipse.xtext.xbase.lib.ObjectExtensions;
import org.eclipse.xtext.xbase.lib.Procedures.Procedure1;

@FinalFieldsConstructor
@SuppressWarnings("all")
public class ParentProjectDescriptor extends ProjectDescriptor {
  private String nameQualifier = ".parent";

  @Override
  public String getNameQualifier() {
    return this.nameQualifier;
  }

  public void setNameQualifier(final String nameQualifier) {
    this.nameQualifier = nameQualifier;
  }

  @Override
  public boolean isEnabled() {
    return ((this.getConfig().needsGradleBuild() || this.getConfig().needsMavenBuild()) || Objects.equals(this.getConfig().getProjectLayout(), ProjectLayout.HIERARCHICAL));
  }

  @Override
  public void setEnabled(final boolean enabled) {
    throw new UnsupportedOperationException("The parent project is automatically enabled depending on the build system");
  }

  @Override
  public String getLocation() {
    String _rootLocation = this.getConfig().getRootLocation();
    String _plus = (_rootLocation + "/");
    String _name = this.getName();
    return (_plus + _name);
  }

  @Override
  public boolean isEclipsePluginProject() {
    return false;
  }

  @Override
  public boolean isPartOfGradleBuild() {
    return true;
  }

  @Override
  public boolean isPartOfMavenBuild() {
    return true;
  }

  @Override
  public Iterable<? extends AbstractFile> getFiles() {
    final ArrayList<AbstractFile> files = CollectionLiterals.<AbstractFile>newArrayList();
    Iterable<? extends AbstractFile> _files = super.getFiles();
    Iterables.<AbstractFile>addAll(files, _files);
    boolean _needsGradleBuild = this.getConfig().needsGradleBuild();
    if (_needsGradleBuild) {
      PlainTextFile _file = this.file(Outlet.ROOT, "settings.gradle", this.settingsGradle());
      files.add(_file);
      PlainTextFile _file_1 = this.file(Outlet.ROOT, "gradle/source-layout.gradle", this.sourceLayoutGradle());
      files.add(_file_1);
      boolean _isNeedsGradleWrapper = this.getConfig().isNeedsGradleWrapper();
      if (_isNeedsGradleWrapper) {
        PlainTextFile _file_2 = this.file(Outlet.ROOT, "gradlew", this.loadResource("gradlew/gradlew"), true);
        files.add(_file_2);
        PlainTextFile _file_3 = this.file(Outlet.ROOT, "gradlew.bat", this.loadResource("gradlew/gradlew.bat"));
        files.add(_file_3);
        PlainTextFile _file_4 = this.file(Outlet.ROOT, "gradle/wrapper/gradle-wrapper.properties", this.loadResource("gradlew/gradle-wrapper.properties"));
        files.add(_file_4);
        BinaryFile _binaryFile = this.binaryFile(Outlet.ROOT, "gradle/wrapper/gradle-wrapper.jar", this.getClass().getClassLoader().getResource("gradlew/gradle-wrapper.jar"));
        files.add(_binaryFile);
      }
    }
    return files;
  }

  public String getJavaVersion() {
    return this.getConfig().getJavaVersion().getQualifier();
  }

  public String getTychoVersion() {
    return "4.0.9";
  }

  public String getTychoVersionJ11() {
    return "2.7.5";
  }

  private CharSequence loadResource(final String resourcePath) {
    try {
      return Resources.toString(this.getClass().getClassLoader().getResource(resourcePath), Charsets.ISO_8859_1);
    } catch (Throwable _e) {
      throw Exceptions.sneakyThrow(_e);
    }
  }

  @Override
  public GradleBuildFile buildGradle() {
    GradleBuildFile _buildGradle = super.buildGradle();
    final Procedure1<GradleBuildFile> _function = (GradleBuildFile it) -> {
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("buildscript {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("repositories {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("mavenCentral()");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("gradlePluginPortal()");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependencies {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("classpath \'org.xtext:xtext-gradle-plugin:");
      String _xtextGradlePluginVersion = this.getConfig().getXtextVersion().getXtextGradlePluginVersion();
      _builder.append(_xtextGradlePluginVersion, "\t\t");
      _builder.append("\'");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("subprojects {");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("ext.xtextVersion = \'");
      XtextVersion _xtextVersion = this.getConfig().getXtextVersion();
      _builder.append(_xtextVersion, "\t");
      _builder.append("\'");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("repositories {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("mavenCentral()");
      _builder.newLine();
      {
        boolean _isSnapshot = this.getConfig().getXtextVersion().isSnapshot();
        if (_isSnapshot) {
          _builder.append("\t\t");
          _builder.append("maven {");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("\t");
          _builder.append("url \'https://oss.sonatype.org/content/repositories/snapshots\'");
          _builder.newLine();
          _builder.append("\t\t");
          _builder.append("}");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("apply plugin: \'java-library\'");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("dependencies {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("api platform(\"org.eclipse.xtext:xtext-dev-bom:${xtextVersion}\")");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("apply plugin: \'org.xtext.xtend\'");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("apply from: \"${rootDir}/gradle/source-layout.gradle\"");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("apply plugin: \'eclipse\'");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("group = \'");
      String _baseName = this.getConfig().getBaseName();
      _builder.append(_baseName, "\t");
      _builder.append("\'");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("version = \'1.0.0-SNAPSHOT\'");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("java {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("sourceCompatibility = JavaVersion.VERSION_");
      String _javaVersion = this.getJavaVersion();
      _builder.append(_javaVersion, "\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t");
      _builder.append("targetCompatibility = JavaVersion.VERSION_");
      String _javaVersion_1 = this.getJavaVersion();
      _builder.append(_javaVersion_1, "\t\t");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.newLine();
      _builder.append("\t");
      _builder.append("configurations.all {");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("exclude group: \'asm\'");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("}");
      _builder.newLine();
      _builder.append("}");
      _builder.newLine();
      it.setAdditionalContent(_builder.toString());
    };
    return ObjectExtensions.<GradleBuildFile>operator_doubleArrow(_buildGradle, _function);
  }

  public CharSequence settingsGradle() {
    StringConcatenation _builder = new StringConcatenation();
    {
      final Function1<ProjectDescriptor, Boolean> _function = (ProjectDescriptor it) -> {
        return Boolean.valueOf(((!Objects.equals(it, this)) && it.isPartOfGradleBuild()));
      };
      Iterable<ProjectDescriptor> _filter = IterableExtensions.<ProjectDescriptor>filter(this.getConfig().getEnabledProjects(), _function);
      for(final ProjectDescriptor p : _filter) {
        {
          ProjectLayout _projectLayout = this.getConfig().getProjectLayout();
          boolean _equals = Objects.equals(_projectLayout, ProjectLayout.FLAT);
          if (_equals) {
            _builder.append("includeFlat");
          } else {
            _builder.append("include");
          }
        }
        _builder.append(" \'");
        String _name = p.getName();
        _builder.append(_name);
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
      }
    }
    return _builder;
  }

  public CharSequence sourceLayoutGradle() {
    StringConcatenation _builder = new StringConcatenation();
    {
      SourceLayout _sourceLayout = this.getConfig().getSourceLayout();
      boolean _equals = Objects.equals(_sourceLayout, SourceLayout.PLAIN);
      if (_equals) {
        _builder.append("if (name.endsWith(\".tests\")) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("sourceSets {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("main {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("java.srcDirs = []");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("resources.srcDirs = []");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("test {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("java.srcDirs = [\'");
        String _sourceFolder = this.sourceFolder(Outlet.TEST_JAVA);
        _builder.append(_sourceFolder, "\t\t\t");
        _builder.append("\', \'");
        String _sourceFolder_1 = this.sourceFolder(Outlet.TEST_SRC_GEN);
        _builder.append(_sourceFolder_1, "\t\t\t");
        _builder.append("\', \'");
        String _sourceFolder_2 = this.sourceFolder(Outlet.MAIN_XTEND_GEN);
        _builder.append(_sourceFolder_2, "\t\t\t");
        _builder.append("\']");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("resources.srcDirs = [\'");
        String _sourceFolder_3 = this.sourceFolder(Outlet.TEST_RESOURCES);
        _builder.append(_sourceFolder_3, "\t\t\t");
        _builder.append("\', \'");
        String _sourceFolder_4 = this.sourceFolder(Outlet.TEST_SRC_GEN);
        _builder.append(_sourceFolder_4, "\t\t\t");
        _builder.append("\']");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("xtend.outputDir = \'");
        String _sourceFolder_5 = this.sourceFolder(Outlet.TEST_XTEND_GEN);
        _builder.append(_sourceFolder_5, "\t\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("} else {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("sourceSets {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("main {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("java.srcDirs = [\'");
        String _sourceFolder_6 = this.sourceFolder(Outlet.MAIN_JAVA);
        _builder.append(_sourceFolder_6, "\t\t\t");
        _builder.append("\', \'");
        String _sourceFolder_7 = this.sourceFolder(Outlet.MAIN_SRC_GEN);
        _builder.append(_sourceFolder_7, "\t\t\t");
        _builder.append("\', \'");
        String _sourceFolder_8 = this.sourceFolder(Outlet.TEST_XTEND_GEN);
        _builder.append(_sourceFolder_8, "\t\t\t");
        _builder.append("\']");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("resources.srcDirs = [\'");
        String _sourceFolder_9 = this.sourceFolder(Outlet.MAIN_RESOURCES);
        _builder.append(_sourceFolder_9, "\t\t\t");
        _builder.append("\', \'");
        String _sourceFolder_10 = this.sourceFolder(Outlet.MAIN_SRC_GEN);
        _builder.append(_sourceFolder_10, "\t\t\t");
        _builder.append("\']");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t\t");
        _builder.append("xtend.outputDir = \'");
        String _sourceFolder_11 = this.sourceFolder(Outlet.MAIN_XTEND_GEN);
        _builder.append(_sourceFolder_11, "\t\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("test {");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("java.srcDirs = []");
        _builder.newLine();
        _builder.append("\t\t\t");
        _builder.append("resources.srcDirs = []");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
        _builder.newLine();
        _builder.append("sourceSets.all {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("resources.exclude \'**/*.g\', \'**/*.mwe2\', \'**/*.xtend\', \'**/*._trace\'");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      } else {
        _builder.append("sourceSets {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("main {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("java.srcDir \'");
        String _sourceFolder_12 = this.sourceFolder(Outlet.MAIN_SRC_GEN);
        _builder.append(_sourceFolder_12, "\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("resources.srcDir \'");
        String _sourceFolder_13 = this.sourceFolder(Outlet.MAIN_SRC_GEN);
        _builder.append(_sourceFolder_13, "\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("xtend.outputDir = \'");
        String _sourceFolder_14 = this.sourceFolder(Outlet.MAIN_XTEND_GEN);
        _builder.append(_sourceFolder_14, "\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("test {");
        _builder.newLine();
        _builder.append("\t\t");
        _builder.append("java.srcDir \'");
        String _sourceFolder_15 = this.sourceFolder(Outlet.TEST_SRC_GEN);
        _builder.append(_sourceFolder_15, "\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("resources.srcDir \'");
        String _sourceFolder_16 = this.sourceFolder(Outlet.TEST_SRC_GEN);
        _builder.append(_sourceFolder_16, "\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t\t");
        _builder.append("xtend.outputDir = \'");
        String _sourceFolder_17 = this.sourceFolder(Outlet.TEST_XTEND_GEN);
        _builder.append(_sourceFolder_17, "\t\t");
        _builder.append("\'");
        _builder.newLineIfNotEmpty();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.newLine();
    _builder.append("jar {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("from(\'model\') {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("into(\'model\')");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    {
      SourceLayout _sourceLayout_1 = this.getConfig().getSourceLayout();
      boolean _notEquals = (!Objects.equals(_sourceLayout_1, SourceLayout.PLAIN));
      if (_notEquals) {
        _builder.append("\t");
        _builder.append("from(sourceSets.main.allSource) {");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("\t");
        _builder.append("include \'**/*.xtext\'");
        _builder.newLine();
        _builder.append("\t");
        _builder.append("}");
        _builder.newLine();
      }
    }
    _builder.append("\t");
    _builder.append("manifest {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("attributes \'Bundle-SymbolicName\': project.name");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    _builder.newLine();
    _builder.append("plugins.withId(\'war\') {");
    _builder.newLine();
    _builder.append("\t");
    _builder.append("war {");
    _builder.newLine();
    _builder.append("\t\t");
    _builder.append("webAppDirectory = file(\"");
    String _sourceFolder_18 = this.sourceFolder(Outlet.WEBAPP);
    _builder.append(_sourceFolder_18, "\t\t");
    _builder.append("\")");
    _builder.newLineIfNotEmpty();
    _builder.append("\t");
    _builder.append("}");
    _builder.newLine();
    _builder.append("}");
    _builder.newLine();
    return _builder;
  }

  @Deprecated
  public CharSequence mavenDeploymentGradle() {
    throw new UnsupportedOperationException("Removed with 2.17");
  }

  @Override
  public PomFile pom() {
    PomFile _pom = super.pom();
    final Procedure1<PomFile> _function = (PomFile it) -> {
      it.setPackaging("pom");
      StringConcatenation _builder = new StringConcatenation();
      _builder.append("<properties>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<xtextVersion>");
      XtextVersion _xtextVersion = this.getConfig().getXtextVersion();
      _builder.append(_xtextVersion, "\t");
      _builder.append("</xtextVersion>");
      _builder.newLineIfNotEmpty();
      {
        boolean _needsTychoBuild = this.getConfig().needsTychoBuild();
        if (_needsTychoBuild) {
          _builder.append("\t");
          _builder.append("<mwe2Version>");
          String _mweVersion = this.getConfig().getXtextVersion().getMweVersion();
          _builder.append(_mweVersion, "\t");
          _builder.append("</mwe2Version>");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("\t");
      _builder.append("<project.build.sourceEncoding>");
      Charset _encoding = this.getConfig().getEncoding();
      _builder.append(_encoding, "\t");
      _builder.append("</project.build.sourceEncoding>");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("<maven.compiler.source>");
      String _javaVersion = this.getJavaVersion();
      _builder.append(_javaVersion, "\t");
      _builder.append("</maven.compiler.source>");
      _builder.newLineIfNotEmpty();
      _builder.append("\t");
      _builder.append("<maven.compiler.target>");
      String _javaVersion_1 = this.getJavaVersion();
      _builder.append(_javaVersion_1, "\t");
      _builder.append("</maven.compiler.target>");
      _builder.newLineIfNotEmpty();
      {
        boolean _needsTychoBuild_1 = this.getConfig().needsTychoBuild();
        if (_needsTychoBuild_1) {
          _builder.append("\t");
          _builder.append("<!-- Tycho settings -->");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<tycho-version>");
          {
            boolean _isAtLeast = this.getConfig().getJavaVersion().isAtLeast(JavaVersion.JAVA17);
            if (_isAtLeast) {
              String _tychoVersion = this.getTychoVersion();
              _builder.append(_tychoVersion, "\t");
            } else {
              String _tychoVersionJ11 = this.getTychoVersionJ11();
              _builder.append(_tychoVersionJ11, "\t");
            }
          }
          _builder.append("</tycho-version>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("<!-- Define overridable properties for tycho-surefire-plugin -->");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<platformSystemProperties></platformSystemProperties>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<moduleProperties></moduleProperties>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<systemProperties></systemProperties>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<additionalTestArguments></additionalTestArguments>");
          _builder.newLine();
        }
      }
      _builder.append("</properties>");
      _builder.newLine();
      _builder.append("<modules>");
      _builder.newLine();
      {
        final Function1<ProjectDescriptor, Boolean> _function_1 = (ProjectDescriptor it_1) -> {
          return Boolean.valueOf(((!Objects.equals(it_1, this)) && it_1.isPartOfMavenBuild()));
        };
        Iterable<ProjectDescriptor> _filter = IterableExtensions.<ProjectDescriptor>filter(this.getConfig().getEnabledProjects(), _function_1);
        for(final ProjectDescriptor p : _filter) {
          _builder.append("\t");
          _builder.append("<module>");
          {
            ProjectLayout _projectLayout = this.getConfig().getProjectLayout();
            boolean _equals = Objects.equals(_projectLayout, ProjectLayout.FLAT);
            if (_equals) {
              _builder.append("../");
            }
          }
          String _name = p.getName();
          _builder.append(_name, "\t");
          _builder.append("</module>");
          _builder.newLineIfNotEmpty();
        }
      }
      _builder.append("</modules>");
      _builder.newLine();
      _builder.append("<dependencyManagement>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<dependencies>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<dependency>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<groupId>org.eclipse.xtext</groupId>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<artifactId>xtext-dev-bom</artifactId>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<version>${xtextVersion}</version>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<type>pom</type>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<scope>import</scope>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</dependency>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</dependencies>");
      _builder.newLine();
      _builder.append("</dependencyManagement>");
      _builder.newLine();
      _builder.append("<build>");
      _builder.newLine();
      {
        boolean _needsTychoBuild_2 = this.getConfig().needsTychoBuild();
        if (_needsTychoBuild_2) {
          _builder.append("\t");
          _builder.append("<plugins>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<plugin>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<groupId>org.eclipse.tycho</groupId>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<artifactId>tycho-maven-plugin</artifactId>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<version>${tycho-version}</version>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<extensions>true</extensions>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</plugin>");
          _builder.newLine();
          {
            boolean _isEnabled = this.getConfig().getP2Project().isEnabled();
            if (_isEnabled) {
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<plugin>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<groupId>org.eclipse.tycho</groupId>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<artifactId>tycho-source-plugin</artifactId>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<version>${tycho-version}</version>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<executions>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t");
              _builder.append("<execution>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<id>plugin-source</id>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<goals>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t\t");
              _builder.append("<goal>plugin-source</goal>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("</goals>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t");
              _builder.append("</execution>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t");
              _builder.append("<execution>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<id>feature-source</id>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<goals>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t\t");
              _builder.append("<goal>feature-source</goal>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("</goals>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t");
              _builder.append("</execution>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("</executions>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("</plugin>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<plugin>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<groupId>org.eclipse.tycho</groupId>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<artifactId>tycho-p2-plugin</artifactId>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<version>${tycho-version}</version>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("<executions>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t");
              _builder.append("<execution>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<id>attach-p2-metadata</id>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<phase>package</phase>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("<goals>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t\t");
              _builder.append("<goal>p2-metadata</goal>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t\t");
              _builder.append("</goals>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t\t");
              _builder.append("</execution>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("</executions>");
              _builder.newLine();
              _builder.append("\t");
              _builder.append("\t");
              _builder.append("</plugin>");
              _builder.newLine();
            }
          }
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<plugin>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<groupId>org.eclipse.tycho</groupId>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<artifactId>target-platform-configuration</artifactId>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<version>${tycho-version}</version>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<configuration>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t");
          _builder.append("<target>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("<artifact>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<groupId>");
          String _baseName = this.getConfig().getBaseName();
          _builder.append(_baseName, "\t\t\t\t\t\t");
          _builder.append("</groupId>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<artifactId>");
          String _name_1 = this.getConfig().getTargetPlatformProject().getName();
          _builder.append(_name_1, "\t\t\t\t\t\t");
          _builder.append("</artifactId>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<version>${project.version}</version>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("</artifact>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t");
          _builder.append("</target>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t");
          _builder.append("<environments>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("<environment>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<os>macosx</os>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<ws>cocoa</ws>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<arch>x86_64</arch>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("</environment>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("<environment>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<os>win32</os>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<ws>win32</ws>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<arch>x86_64</arch>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("</environment>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("<environment>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<os>linux</os>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<ws>gtk</ws>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<arch>x86_64</arch>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("</environment>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t");
          _builder.append("</environments>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t");
          _builder.append("<dependency-resolution>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("<extraRequirements>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<requirement>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<type>eclipse-plugin</type>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<id>org.eclipse.xtext.logging</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<versionRange>0.0.0</versionRange>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("</requirement>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<!-- to get the org.eclipse.osgi.compatibility.state plugin if the target");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("platform is Luna or later. (backward compatible with kepler and previous");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("versions) see https://bugs.eclipse.org/bugs/show_bug.cgi?id=492149 -->");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("<requirement>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<type>eclipse-feature</type>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<id>org.eclipse.rcp</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<versionRange>0.0.0</versionRange>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t\t");
          _builder.append("</requirement>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t\t");
          _builder.append("</extraRequirements>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t\t");
          _builder.append("</dependency-resolution>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("</configuration>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</plugin>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</plugins>");
          _builder.newLine();
        }
      }
      _builder.append("\t");
      _builder.append("<pluginManagement>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<plugins>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<plugin>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<groupId>org.eclipse.xtend</groupId>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<artifactId>xtend-maven-plugin</artifactId>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<version>${xtextVersion}</version>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<executions>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t");
      _builder.append("<execution>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t\t");
      _builder.append("<goals>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t\t\t");
      _builder.append("<goal>compile</goal>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t\t\t");
      _builder.append("<goal>xtend-install-debug-info</goal>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t\t\t");
      _builder.append("<goal>testCompile</goal>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t\t\t");
      _builder.append("<goal>xtend-test-install-debug-info</goal>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t\t");
      _builder.append("</goals>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t");
      _builder.append("</execution>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("</executions>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<configuration>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t");
      _builder.append("<outputDirectory>${basedir}/");
      String _sourceFolder = this.sourceFolder(Outlet.MAIN_XTEND_GEN);
      _builder.append(_sourceFolder, "\t\t\t\t\t");
      _builder.append("</outputDirectory>");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t\t\t");
      _builder.append("<testOutputDirectory>${basedir}/");
      String _sourceFolder_1 = this.sourceFolder(Outlet.TEST_XTEND_GEN);
      _builder.append(_sourceFolder_1, "\t\t\t\t\t");
      _builder.append("</testOutputDirectory>");
      _builder.newLineIfNotEmpty();
      _builder.append("\t\t\t\t");
      _builder.append("</configuration>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("</plugin>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<plugin>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<groupId>org.apache.maven.plugins</groupId>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<artifactId>maven-clean-plugin</artifactId>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<version>3.2.0</version>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<configuration>");
      _builder.newLine();
      _builder.append("\t\t\t\t\t");
      _builder.append("<filesets>");
      _builder.newLine();
      {
        final Function1<Outlet, String> _function_2 = (Outlet it_1) -> {
          return this.sourceFolder(it_1);
        };
        Set<String> _set = IterableExtensions.<String>toSet(ListExtensions.<Outlet, String>map(Collections.<Outlet>unmodifiableList(CollectionLiterals.<Outlet>newArrayList(Outlet.MAIN_XTEND_GEN, Outlet.TEST_XTEND_GEN)), _function_2));
        for(final String dir : _set) {
          _builder.append("\t\t\t\t\t\t");
          _builder.append("<fileset>");
          _builder.newLine();
          _builder.append("\t\t\t\t\t\t");
          _builder.append("\t");
          _builder.append("<directory>${basedir}/");
          _builder.append(dir, "\t\t\t\t\t\t\t");
          _builder.append("</directory>");
          _builder.newLineIfNotEmpty();
          _builder.append("\t\t\t\t\t\t");
          _builder.append("\t");
          _builder.append("<includes>");
          _builder.newLine();
          _builder.append("\t\t\t\t\t\t");
          _builder.append("\t\t");
          _builder.append("<include>**/*</include>");
          _builder.newLine();
          _builder.append("\t\t\t\t\t\t");
          _builder.append("\t");
          _builder.append("</includes>");
          _builder.newLine();
          _builder.append("\t\t\t\t\t\t");
          _builder.append("</fileset>");
          _builder.newLine();
        }
      }
      _builder.append("\t\t\t\t\t");
      _builder.append("</filesets>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("</configuration>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("</plugin>");
      _builder.newLine();
      {
        boolean _needsTychoBuild_3 = this.getConfig().needsTychoBuild();
        boolean _not = (!_needsTychoBuild_3);
        if (_not) {
          _builder.append("\t\t\t");
          _builder.append("<plugin>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<groupId>org.apache.maven.plugins</groupId>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<artifactId>maven-surefire-plugin</artifactId>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<version>3.0.0</version>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("</plugin>");
          _builder.newLine();
        }
      }
      {
        boolean _needsTychoBuild_4 = this.getConfig().needsTychoBuild();
        if (_needsTychoBuild_4) {
          _builder.append("\t\t\t");
          _builder.append("<plugin>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<!--");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("Can be removed after first generator execution");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("https://bugs.eclipse.org/bugs/show_bug.cgi?id=480097");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("-->");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<groupId>org.eclipse.tycho</groupId>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<artifactId>tycho-compiler-plugin</artifactId>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<version>${tycho-version}</version>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<configuration>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("<compilerArgument>-err:-forbidden</compilerArgument>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("<useProjectSettings>false</useProjectSettings>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("</configuration>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("</plugin>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("<!-- to skip running (and compiling) tests use commandline flag: -Dmaven.test.skip");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("To skip tests, but still compile them, use: -DskipTests");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("To allow all tests in a pom to pass/fail, use commandline flag: -fae (fail");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("at end) -->");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("<plugin>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<groupId>org.eclipse.tycho</groupId>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<artifactId>tycho-surefire-plugin</artifactId>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<version>${tycho-version}</version>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("<configuration>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("<!-- THE FOLLOWING LINE MUST NOT BE BROKEN BY AUTOFORMATTING -->");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("<argLine>${platformSystemProperties} ${systemProperties} ${moduleProperties} ${additionalTestArguments}</argLine>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("<failIfNoTests>false</failIfNoTests>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t\t");
          _builder.append("<useUIHarness>false</useUIHarness>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("\t");
          _builder.append("</configuration>");
          _builder.newLine();
          _builder.append("\t\t\t");
          _builder.append("</plugin>");
          _builder.newLine();
        }
      }
      _builder.append("\t\t");
      _builder.append("</plugins>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</pluginManagement>");
      _builder.newLine();
      _builder.append("</build>");
      _builder.newLine();
      _builder.append("<repositories>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<repository>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<id>codehaus-snapshots</id>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<name>disable dead \'Codehaus Snapshots\' repository, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=481478</name>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<url>http://nexus.codehaus.org/snapshots/</url>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<releases>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<enabled>false</enabled>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</releases>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<snapshots>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<enabled>false</enabled>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</snapshots>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</repository>");
      _builder.newLine();
      {
        boolean _needsTychoBuild_5 = this.getConfig().needsTychoBuild();
        if (_needsTychoBuild_5) {
          _builder.append("\t");
          _builder.append("<!-- This must be disabled explicitly, otherwise it is enabled by https://github.com/mojohaus/mojo-parent ");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("which is taken from exec-maven-plugin from at least version 1.6.0 -->");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<repository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>ossrh-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<name>ossrh-snapshots</name>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>http://oss.sonatype.org/content/repositories/snapshots</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</repository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<!-- This is enabled by /org/sonatype/oss/oss-parent/7 used as parent by ");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("org/xtext/antlr-generator/3.2.1 -->");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<repository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>sonatype-nexus-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<name>Sonatype Nexus Snapshots</name>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>https://oss.sonatype.org/content/repositories/snapshots</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</repository>");
          _builder.newLine();
        }
      }
      {
        boolean _isSnapshot = this.getConfig().getXtextVersion().isSnapshot();
        if (_isSnapshot) {
          _builder.append("\t");
          _builder.append("<repository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>sonatype-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>https://oss.sonatype.org/content/repositories/snapshots</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>true</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</repository>");
          _builder.newLine();
        }
      }
      _builder.append("</repositories>");
      _builder.newLine();
      _builder.append("<pluginRepositories>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<pluginRepository>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<id>codehaus-snapshots</id>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<name>disable dead \'Codehaus Snapshots\' repository, see https://bugs.eclipse.org/bugs/show_bug.cgi?id=481478</name>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<url>http://nexus.codehaus.org/snapshots/</url>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<releases>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<enabled>false</enabled>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</releases>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<snapshots>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<enabled>false</enabled>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</snapshots>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</pluginRepository>");
      _builder.newLine();
      {
        boolean _needsTychoBuild_6 = this.getConfig().needsTychoBuild();
        if (_needsTychoBuild_6) {
          _builder.append("\t");
          _builder.append("<pluginRepository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>ossrh-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<name>ossrh-snapshots</name>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>http://oss.sonatype.org/content/repositories/snapshots</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</pluginRepository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("<pluginRepository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>sonatype-nexus-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<name>Sonatype Nexus Snapshots</name>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>https://oss.sonatype.org/content/repositories/snapshots</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</pluginRepository>");
          _builder.newLine();
        }
      }
      {
        boolean _isSnapshot_1 = this.getConfig().getXtextVersion().isSnapshot();
        if (_isSnapshot_1) {
          _builder.append("\t");
          _builder.append("<pluginRepository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>sonatype-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>https://oss.sonatype.org/content/repositories/snapshots</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>false</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</releases>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t\t");
          _builder.append("<enabled>true</enabled>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("</snapshots>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</pluginRepository>");
          _builder.newLine();
        }
      }
      {
        if ((this.getConfig().needsTychoBuild() && this.getTychoVersion().endsWith("-SNAPSHOT"))) {
          _builder.append("\t");
          _builder.append("<pluginRepository>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<id>tycho-snapshots</id>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("\t");
          _builder.append("<url>https://repo.eclipse.org/content/repositories/tycho-snapshots/</url>");
          _builder.newLine();
          _builder.append("\t");
          _builder.append("</pluginRepository>");
          _builder.newLine();
        }
      }
      _builder.append("</pluginRepositories>");
      _builder.newLine();
      _builder.append("<profiles>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<profile>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<id>macos</id>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<activation>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<os>");
      _builder.newLine();
      _builder.append("\t\t\t\t");
      _builder.append("<family>mac</family>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("</os>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</activation>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<properties>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<!-- THE FOLLOWING LINE MUST NOT BE BROKEN BY AUTOFORMATTING -->");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<platformSystemProperties>-XstartOnFirstThread</platformSystemProperties>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</properties>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</profile>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("<profile>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<id>jdk9-or-newer</id>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<activation>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<jdk>[9,)</jdk>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</activation>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("<properties>");
      _builder.newLine();
      _builder.append("\t\t\t");
      _builder.append("<moduleProperties>--add-modules=ALL-SYSTEM</moduleProperties>");
      _builder.newLine();
      _builder.append("\t\t");
      _builder.append("</properties>");
      _builder.newLine();
      _builder.append("\t");
      _builder.append("</profile>");
      _builder.newLine();
      _builder.append("</profiles>");
      _builder.newLine();
      it.setBuildSection(_builder.toString());
    };
    return ObjectExtensions.<PomFile>operator_doubleArrow(_pom, _function);
  }

  @Override
  public Set<SourceFolderDescriptor> getSourceFolders() {
    return Collections.<SourceFolderDescriptor>unmodifiableSet(CollectionLiterals.<SourceFolderDescriptor>newHashSet());
  }

  public ParentProjectDescriptor(final WizardConfiguration config) {
    super(config);
  }
}

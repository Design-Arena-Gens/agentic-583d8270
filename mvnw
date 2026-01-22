#!/bin/sh
# ----------------------------------------------------------------------------
# Licensed to the Apache Software Foundation (ASF) under one
# or more contributor license agreements.  See the NOTICE file
# distributed with this work for additional information
# regarding copyright ownership.  The ASF licenses this file
# to you under the Apache License, Version 2.0 (the
# "License"); you may not use this file except in compliance
# with the License.  You may obtain a copy of the License at
#
#    https://www.apache.org/licenses/LICENSE-2.0
#
# Unless required by applicable law or agreed to in writing,
# software distributed under the License is distributed on an
# "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY
# KIND, either express or implied.  See the License for the
# specific language governing permissions and limitations
# under the License.
# ----------------------------------------------------------------------------

# ----------------------------------------------------------------------------
# Maven Start Up Batch script
#
# Required ENV vars:
# ------------------
#   JAVA_HOME - location of a JDK home dir
#
# Optional ENV vars
# -----------------
#   M2_HOME - location of maven2's installed home dir
#   MAVEN_OPTS - parameters passed to the Java VM when running Maven
#     e.g. to debug Maven itself, use
#       set MAVEN_OPTS=-Xdebug -Xrunjdwp:transport=dt_socket,server=y,suspend=y,address=8000
#   MAVEN_SKIP_RC - flag to disable loading of mavenrc files
# ----------------------------------------------------------------------------

if [ -z "$MAVEN_SKIP_RC" ] ; then

  if [ -f /usr/local/etc/mavenrc ] ; then
    . /usr/local/etc/mavenrc
  fi

  if [ -f /etc/mavenrc ] ; then
    . /etc/mavenrc
  fi

  if [ -f "$HOME/.mavenrc" ] ; then
    . "$HOME/.mavenrc"
  fi

fi

MVNW_HOME="$(cd "$(dirname "$0")" && pwd)"

JAVA_EXE="$JAVA_HOME/bin/java"
if [ -z "$JAVA_HOME" ] ; then
  JAVA_EXE="$(command -v java)"
fi

if [ ! -x "$JAVA_EXE" ] ; then
  echo "Error: JAVA_HOME is not defined correctly." >&2
  echo "  We cannot execute $JAVA_EXE" >&2
  exit 1
fi

MAVEN_PROJECTBASEDIR="${MAVEN_BASEDIR:-"$MVNW_HOME"}"
if [ "$MAVEN_PROJECTBASEDIR" = "" ] ; then
  MAVEN_PROJECTBASEDIR="$MVNW_HOME"
fi

if [ -r "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config" ] ; then
  JVM_CONFIG="$(tr '\n' ' ' < "$MAVEN_PROJECTBASEDIR/.mvn/jvm.config")"
fi

WRAPPER_JAR="$MAVEN_PROJECTBASEDIR/.mvn/wrapper/maven-wrapper.jar"
WRAPPER_LAUNCHER=org.apache.maven.wrapper.MavenWrapperMain

WRAPPER_ARGS="-Dmaven.multiModuleProjectDirectory=${MAVEN_PROJECTBASEDIR}"

# shellcheck disable=SC2046
exec "$JAVA_EXE" $JVM_CONFIG $MAVEN_OPTS $WRAPPER_ARGS -classpath "$WRAPPER_JAR" $WRAPPER_LAUNCHER "$@"

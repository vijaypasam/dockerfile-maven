/*-
 * -\-\-
 * Dockerfile Maven Plugin
 * --
 * Copyright (C) 2019 Simon Woodward
 * --
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * 
 *      http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 * -/-/-
 */

package com.spotify.plugin.dockerfile;

import static org.junit.Assert.assertTrue;

import org.apache.maven.plugin.MojoFailureException;
import org.junit.Test;
import org.junit.rules.ExpectedException;

public class TestRepoNameValidation {
  public ExpectedException thrown = ExpectedException.none();

  @Test
  public void testSuccess() throws MojoFailureException {
    BuildMojo.validateRepository("alllowercase");
    BuildMojo.validateRepository("with000numbers");
    BuildMojo.validateRepository("00withnumbers");
    BuildMojo.validateRepository("00757383");
    BuildMojo.validateRepository("withnumbers34343");
    BuildMojo.validateRepository("with-hyphens");
    BuildMojo.validateRepository("with_underscores");
    BuildMojo.validateRepository("with.dots");
    BuildMojo.validateRepository("______");
    BuildMojo.validateRepository("------");
    BuildMojo.validateRepository("......");
    BuildMojo.validateRepository("example.com/okay./.path");
    BuildMojo.validateRepository(".start.and.end.");
    BuildMojo.validateRepository("-start-and-end-");
    BuildMojo.validateRepository("_start_and_end_");
    // Forward slash delimits the repo user from the repo name; strictly speaking,
    // you're allowed only one slash, somewhere in the middle.
    BuildMojo.validateRepository("with/forwardslash");
    BuildMojo.validateRepository("with/multiple/forwardslash");
  }

  private boolean throwsMojoFailure(String repoName) {
    boolean threw = false;
    try {
      BuildMojo.validateRepository(repoName);
    } catch (MojoFailureException e) {
      threw = true;
    }
    return threw;
  }

  @Test
  public void testFailCases() {
    assertTrue("Mixed case didn't fail", throwsMojoFailure("ddddddDddddd"));
    assertTrue("Symbols didn't fail", throwsMojoFailure("ddddddDd+dddd"));
    assertTrue("Starting slash didn't fail", throwsMojoFailure("/atstart"));
    assertTrue("Ending slash didn't fail", throwsMojoFailure("atend/"));
  }
}

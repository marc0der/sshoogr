/*
 * Copyright (C) 2011-2016 Aestas/IT
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.aestasit.infrastructure.ssh

import com.aestasit.infrastructure.ssh.launcher.Sshoogr
import org.junit.Rule
import org.junit.Test
import org.junit.contrib.java.lang.system.ExpectedSystemExit

class LauncherTest extends BaseSshTest {

  @Rule
  public final ExpectedSystemExit exit = ExpectedSystemExit.none();

  static final String script = '''
    remoteSession {
      exec 'whoami'
      exec 'du -s'
      exec 'rm -rf /tmp/test.file'
      remoteFile('/etc/init.conf').text = 'content'
    }
    '''

  @Test
  void scriptWithConnectionParameters() throws Exception {
    Sshoogr.main([

      "--user",
      "user2",

      "--password",
      "654321",

      "--host",
      "localhost",

      "--trust",

      "--port",
      "2233",

      temporaryScript.absolutePath

    ] as String[])
  }

  @Test
  void defaultSshoogrScript() throws Exception {
    Sshoogr.main()
  }

  @Test
  void defaultSshoogrScriptWithLogger() throws Exception {
    Sshoogr.main(['--logger', 'color'] as String[])
  }

  @Test
  void helpMessage() throws Exception {
    String output = captureOutput {
      Sshoogr.main(['--help'] as String[])
    }
    assert output.contains('Usage:')
  }

  @Test
  void notExistingScript() throws Exception {
    exit.expectSystemExitWithStatus(127)
    Sshoogr.main(['gg.sshoogr'] as String[])
  }

  static File getTemporaryScript() {
    File scriptFile = File.createTempFile("default", ".sshoogr")
    scriptFile.text = script
    scriptFile
  }

}

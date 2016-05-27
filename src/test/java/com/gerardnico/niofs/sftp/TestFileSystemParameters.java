package com.gerardnico.niofs.sftp;

import java.nio.file.Paths;
import java.util.Map;

/**
 * Created by gerard on 23-05-2016.
 * The URI parameters for the test
 * They are fixed in the class but can be changed through environment parameters
 *
 *   * NIOFS_SFTP_USER
 *   * NIOFS_SFTP_PWD
 *   * NIOFS_SFTP_HOST
 *   * NIOFS_SFTP_PORT
 *   * NIOFS_SFTP_WORKING_DIR
 *   * NIOFS_SFTP_HOME_USER_DIR
 */
public class TestFileSystemParameters {

    protected static String USER = "user";
    protected static String PWD = "pwd";
    protected static String HOST = "localhost";
    protected static Integer PORT = MockSshSftpServer.PORT;
    protected static String WORKING_DIR = null;
    protected static String HOME_USER_DIR = null;
    protected static String URL = "sftp://" + TestFileSystemParameters.USER + ":" + TestFileSystemParameters.PWD + "@" + TestFileSystemParameters.HOST + ":" + TestFileSystemParameters.PORT;

    static {
        Map<String, String> environments = System.getenv();
        if (environments.get("NIOFS_SFTP_USER") != null) {
            USER = environments.get("NIOFS_SFTP_USER");
            PWD = environments.get("NIOFS_SFTP_PWD") != null ? environments.get("NIOFS_SFTP_PWD") : PWD;
            HOST = environments.get("NIOFS_SFTP_HOST") != null ? environments.get("NIOFS_SFTP_HOST") : HOST;
            PORT = environments.get("NIOFS_SFTP_PORT") != null ? Integer.valueOf(environments.get("NIOFS_SFTP_PORT")) : PORT;
            WORKING_DIR = environments.get("NIOFS_SFTP_WORKING_DIR") != null ? environments.get("NIOFS_SFTP_WORKING_DIR") : null;
            // The home user dir can be found dynamically but to test the Paths operations, we need to set an absolute path
            // and therefore we need to known the home user directory before making a connection.
            HOME_USER_DIR = environments.get("NIOFS_SFTP_HOME_USER_DIR") != null ? environments.get("NIOFS_SFTP_HOME_USER_DIR") : "/home/gerardni-niosftp/";
            URL = "sftp://" + USER + ":" + PWD + "@" + HOST + ":" + PORT;
        } else {
            HOME_USER_DIR = Paths.get("").toString();
        }
    }

}

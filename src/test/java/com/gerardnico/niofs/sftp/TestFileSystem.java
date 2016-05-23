package com.gerardnico.niofs.sftp;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by gerard on 18-05-2016.
 *
 * Create a file system for the test.
 *
 * Return the sftp mock server or a ssh connection if the NIOFS_SFTP_USER environment parameters is set.
 * Other environment parameters can be set:
 *   * NIOFS_SFTP_USER
 *   * NIOFS_SFTP_PWD
 *   * NIOFS_SFTP_HOST
 *   * NIOFS_SFTP_PORT
 *   * NIOFS_SFTP_WORKING_DIR (Use only when
 */
public class TestFileSystem {

    // The static parameters
    private static MockSshSftpServer mockSftpServer;
    private static FileSystemProvider sftpFileSystemProvider;



    // A cache
    private static TestFileSystem testFileSystem;

    // A member
    private FileSystem sftpFileSystem;


    public TestFileSystem(TestFileSystemBuilder testFileSystemBuilder) {

            // Get the SFTP File System Provider
            // See http://docs.oracle.com/javase/8/docs/api/java/util/ServiceLoader.html
            // To know how the file system is loaded
            for (FileSystemProvider fileSystemProvider : FileSystemProvider.installedProviders()) {
                if (SftpFileSystemProvider.SFTP_SCHEME.equals(fileSystemProvider.getScheme())) {
                    sftpFileSystemProvider = fileSystemProvider;
                }
            }
            if (sftpFileSystemProvider == null) {
                // It's installed but yeah a snippet :)
                throw new ProviderNotFoundException("Unable to get a " + SftpFileSystemProvider.SFTP_SCHEME + " file system provider");
            }


            if (UriParameters.HOST.equals("localhost") && UriParameters.PORT == 22999 ) {
                // Start the server
                mockSftpServer = new MockSshSftpServer();
                mockSftpServer.start();
            }


            try {
                Map<String, String> env = null;
                if (UriParameters.WORKING_DIR != null && testFileSystemBuilder.useWorkingDirectory) {
                    env = new HashMap<>();
                    env.put(SftpFileSystem.KEY_WORKING_DIRECTORY,UriParameters.WORKING_DIR);
                }
                sftpFileSystem = sftpFileSystemProvider.newFileSystem(URI.create(UriParameters.URL), env);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }


    }


    FileSystem get() {

        return sftpFileSystem;

    }

    boolean isWindows() {
        if (mockSftpServer != null ) {
            if (System.getProperty("os.name").contains("Windows")) {
                return true;
            } else {
                return false;
            }
        } else {
            // If this is not the mock server, we don't know.
            // TODO: Send OS command to determine it.
            return false;
        }
    }

    public void close() {
        try {
            sftpFileSystem.close();
            sftpFileSystem = null;
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        if (mockSftpServer!=null) {
            mockSftpServer.stop();
            mockSftpServer=null;
        }
    }


    public static class TestFileSystemBuilder {


        private boolean useWorkingDirectory = false;

        public TestFileSystemBuilder() {}

        // By default we use the home user directory for the test
        // but if we want another, we need to set it to true
        public TestFileSystemBuilder useWorkingDirectory(boolean useWorkingDirectory) {
            this.useWorkingDirectory = useWorkingDirectory;
            return this;
        }


        public TestFileSystem build() {
            if (testFileSystem != null){
                if (testFileSystem.get() !=null)
                    return testFileSystem;
                else {
                    testFileSystem = new TestFileSystem(this);
                    return testFileSystem;
                }
            } else {
                testFileSystem = new TestFileSystem(this);
                return testFileSystem;
            }


        }


    }


}

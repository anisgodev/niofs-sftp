package com.gerardnico.niofs.sftp;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.FileSystem;
import java.nio.file.ProviderNotFoundException;
import java.nio.file.spi.FileSystemProvider;
import java.util.Map;

/**
 * Created by gerard on 18-05-2016.
 * Return the mock server or a ssh connection if the following  environment parameters are set:
 *   * NIOFS_SFTP_USER
 *   * NIOFS_SFTP_PWD
 *   * NIOFS_SFTP_HOST
 *   * NIOFS_SFTP_PORT
 */
public class FileSystemFactory {

    private static MockSshSftpServer mockSftpServer;
    private static FileSystemProvider sftpFileSystemProvider;
    private static String user = "user";
    private static String pwd = "pwd";
    private static String host = "localhost";
    private static Integer port = 22999;
    private static String url = "sftp://" + user + ":" + pwd + "@" + host + ":" + port;
    private static FileSystem sftpFileSystem;

    private static URI uri;


    static FileSystem get() {

        if (sftpFileSystem != null) {
            return sftpFileSystem;
        } else {
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

            Map<String, String> environments = System.getenv();
            if (environments.get("NIOFS_SFTP_USER") != null) {
                user = environments.get("NIOFS_SFTP_USER");
                pwd = environments.get("NIOFS_SFTP_PWD") != null ? environments.get("NIOFS_SFTP_PWD") : pwd;
                host = environments.get("NIOFS_SFTP_HOST") != null ? environments.get("NIOFS_SFTP_HOST") : host;
                port = environments.get("NIOFS_SFTP_PORT") != null ? Integer.valueOf(environments.get("NIOFS_SFTP_PORT")) : port;
                url = "sftp://" + user + ":" + pwd + "@" + host + ":" + port;
            } else {
                // Start the server
                mockSftpServer = new MockSshSftpServer();
                mockSftpServer.start();
            }

            try {
                uri = new URI(url);
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            }


            try {
                sftpFileSystem = sftpFileSystemProvider.newFileSystem(uri, null);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }

            return sftpFileSystem;

        }

    }

    static boolean isWindows() {
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

    public static void close() {
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
}


package com.gerardnico.niofs.sftp;

import com.jcraft.jsch.SftpException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.*;
import java.nio.file.spi.FileSystemProvider;
import java.util.List;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;


public class FileSystemsTest {

    private static MockSshSftpServer mockSftpServer;
    private static String user = "user";
    private static String pwd = "pwd";
    private static String host = "localhost";
    private static Integer port = 22999;
    private static String url = "sftp://" + user + ":" + pwd + "@" + host + ":" + port;
    private static FileSystemProvider sftpFileSystemProvider;
    private static FileSystem fileSystem;
    private static URI uri;

    // Two tests:
    // For a file and for a directory
    @BeforeClass
    static public void createResources() throws SftpException, URISyntaxException, IOException {

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
            throw new ProviderNotFoundException("Unable to get a SFTP file system provider");
        }

        // Start the server
        mockSftpServer = new MockSshSftpServer();
        mockSftpServer.start();

        uri = new URI(url);
        fileSystem = sftpFileSystemProvider.newFileSystem(uri, null);

    }

    @AfterClass
    static public void closeResources() throws IOException {

        fileSystem.close();
        mockSftpServer.stop();

    }


    @Test
    public void sftpFileSystemProviderIsNotNull() throws Exception {

        assertNotNull(sftpFileSystemProvider);

    }

    @Test
    public void sftpFileSystemIsNotNull() throws Exception {

        assertNotNull(fileSystem);

    }

    @Test
    public void sftpFileSystemIsOpen() throws Exception {

        assertEquals("The File System must be opened", true,fileSystem.isOpen());

    }

    @Test
    public void moveIn() throws IOException {

        Path src = Paths.get("./README.md");
        Path dst = fileSystem.getPath("./README.md");
        Files.move(src, dst);
    }

//    Path mf = fileSystem.getPath("testFileRead.txt");
//    InputStream in = mf.newInputStream();

}

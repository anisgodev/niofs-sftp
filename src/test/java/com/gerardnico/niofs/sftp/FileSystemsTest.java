
package com.gerardnico.niofs.sftp;

import com.jcraft.jsch.SftpException;
import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.charset.Charset;
import java.nio.file.*;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.PosixFilePermissions;
import java.nio.file.attribute.UserDefinedFileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import static org.junit.Assert.*;


public class FileSystemsTest {

    private static MockSshSftpServer mockSftpServer;
    private static String user = "user";
    private static String pwd = "pwd";
    private static String host = "localhost";
    private static Integer port = 22999;
    private static String url = "sftp://" + user + ":" + pwd + "@" + host + ":" + port;
    private static FileSystemProvider sftpFileSystemProvider;
    private static SftpFileSystem sftpFileSystem;
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
        sftpFileSystem = (SftpFileSystem) sftpFileSystemProvider.newFileSystem(uri, null);


    }

    @AfterClass
    static public void closeResources() throws IOException {

        sftpFileSystem.close();
        mockSftpServer.stop();

    }


    @Test
    public void sftpFileSystemProviderIsNotNull() throws Exception {

        assertNotNull(sftpFileSystemProvider);

    }

    @Test
    public void sftpFileSystemIsNotNull() throws Exception {

        assertNotNull(sftpFileSystem);

    }

    @Test
    public void sftpFileSystemIsOpen() throws Exception {

        assertEquals("The File System must be opened", true, sftpFileSystem.isOpen());

    }

    @Test
    public void sftpFilesCopyFromLocalToSftpMoveIn() throws IOException {

        Path src = Paths.get("./README.md");
        Path dst = sftpFileSystem.getPath("src", "test", "resources", "sftp", "README.md");
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);
//        assertFalse("Files doesn't exist",Files.exists(dst));
    }

    @Test
    public void sftpFilesCreateFileTest() throws IOException {

        Path dst = sftpFileSystem.getPath("target", "CreateFileTest.txt");
        Files.createFile(dst);

    }


    @Test
    public void posixFileAttribute() throws IOException {
        Path file = sftpFileSystem.getPath("src", "test", "resources", "sftp", "testFileRead.txt");
        SftpPosixFileAttributes attrs = Files.readAttributes(file, SftpPosixFileAttributes.class);
        assertNotNull("The file exist, we must get attributes", attrs);
        assertFalse("This is not a directory", attrs.isDirectory());
        assertTrue("This is a regular file", attrs.isRegularFile());
        assertFalse("This is not an symbolic link", attrs.isSymbolicLink());
        assertFalse("This is not an other file", attrs.isOther());
        assertEquals("The file size is", 38, attrs.size());
        // The date time of the file can be different between the local modification and the git commit :(
        // And then the local test may succeed while the continuous automation test may failed
        // Example:
        // Local: 2016-05-02T14:53:27Z
        // Server (Travis): 2016-05-02T14:56:10Z
        // Therefore we test the best case
        assertEquals("The last modified time is: ", "2016-05-02T14:5", attrs.lastModifiedTime().toString().substring(0,15));
        assertEquals("The last modified time is the creation time (Creation time doesn't exist in SFTP", attrs.creationTime(), attrs.lastModifiedTime());
        assertEquals("The last access time is ", "2016-05-02T13:53:27Z", attrs.lastAccessTime().toString());
        Set<PosixFilePermission> expectedPermission = new HashSet<java.nio.file.attribute.PosixFilePermission>();
        expectedPermission.add(PosixFilePermission.GROUP_EXECUTE);
        expectedPermission.add(PosixFilePermission.OTHERS_READ);
        expectedPermission.add(PosixFilePermission.OTHERS_WRITE);
        expectedPermission.add(PosixFilePermission.OWNER_READ);
        expectedPermission.add(PosixFilePermission.OTHERS_EXECUTE);
        expectedPermission.add(PosixFilePermission.GROUP_WRITE);
        expectedPermission.add(PosixFilePermission.OWNER_WRITE);
        expectedPermission.add(PosixFilePermission.OWNER_EXECUTE);
        expectedPermission.add(PosixFilePermission.GROUP_READ);
        assertEquals("The permissions are equal", expectedPermission, attrs.permissions());
    }


//    Path mf = sftpFileSystem.getPath("testFileRead.txt");
//    InputStream in = mf.newInputStream();

}

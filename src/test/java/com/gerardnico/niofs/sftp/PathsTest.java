package com.gerardnico.niofs.sftp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


/**
 * Created by gerard on 23-05-2016.
 * The path operations of the class {@link java.nio.file.Paths}
 */
public class PathsTest {


    private static FileSystem sftpFileSystem;
    private static TestFileSystem testFileSystem;


    @BeforeClass
    static public void createResources()  {


        testFileSystem = new TestFileSystem.TestFileSystemBuilder().build();
        sftpFileSystem = testFileSystem.get();

    }

    @AfterClass
    static public void closeResources() throws IOException {

        testFileSystem.close();

    }

    @Test
    public void getWithPath() throws IOException {

        // The url must be absolute
        String url = TestFileSystemParameters.URL+ TestFileSystemParameters.HOME_USER_DIR+"/src/test/resources/sftp/README.md";
        Path path = Paths.get(URI.create(url));
        assertEquals("The file ("+url+") must exist",true, Files.exists(path));
        path.getFileSystem().close();


    }

    @Test
    public void getWithoutPath() throws IOException {

        // The url must be absolute
        String url = TestFileSystemParameters.URL;
        Path path = Paths.get(URI.create(url));
        assertEquals("Without path, it must take the user home directory", TestFileSystemParameters.HOME_USER_DIR, path.toAbsolutePath().toString());
        path.getFileSystem().close();


    }


}

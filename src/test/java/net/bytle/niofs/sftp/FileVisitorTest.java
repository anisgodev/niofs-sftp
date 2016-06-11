package net.bytle.niofs.sftp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.FileSystem;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Created by gerard on 18-05-2016.
 */
public class FileVisitorTest {


    private static FileSystem fileSystem;
    private static TestFileSystem testFileSystem;


    @BeforeClass
    static public void createResources()  {


        testFileSystem = new TestFileSystem.TestFileSystemBuilder().build();
        fileSystem = testFileSystem.get();


    }

    @AfterClass
    static public void closeResources() throws IOException {

        testFileSystem.close();

    }

    @Test
    public void visitFile() throws IOException {

        Path start = fileSystem.getPath(".");
        Files.walkFileTree(start,new FileVisitorSimple());

    }

}

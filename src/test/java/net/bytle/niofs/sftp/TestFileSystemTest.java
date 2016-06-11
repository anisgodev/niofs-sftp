
package net.bytle.niofs.sftp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.Assert.*;


/**
 * Test the factory {@link TestFileSystem}
 */

public class TestFileSystemTest {


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
    public void sftpFileSystemIsNotNull() throws Exception {

        assertNotNull(sftpFileSystem);

    }

    @Test
    public void sftpFileSystemIsOpen() throws Exception {

        assertEquals("The File System must be opened", true, sftpFileSystem.isOpen());

    }



}

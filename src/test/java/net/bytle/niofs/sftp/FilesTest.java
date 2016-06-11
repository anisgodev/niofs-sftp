package net.bytle.niofs.sftp;

import org.junit.AfterClass;
import org.junit.BeforeClass;
import org.junit.Test;

import java.io.IOException;
import java.nio.file.*;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerard on 23-05-2016.
 * Test of the java.nio.Files functions excepted for the function that modifies the attributes. See {@link FilesAttributesTest}
 */
public class FilesTest {


    private static FileSystem sftpFileSystem;
    private static TestFileSystem testFileSystem;


    @BeforeClass
    static public void createResources()  {


        testFileSystem = new TestFileSystem.TestFileSystemBuilder()
                .useWorkingDirectory(false)
                .build();
        sftpFileSystem = testFileSystem.get();


    }

    @AfterClass
    static public void closeResources() throws IOException {

        testFileSystem.close();

    }


    @Test
    public void CopyFromLocalToSftp() throws IOException {

        Path src = Paths.get("./README.md");
        Path dst = sftpFileSystem.getPath("src", "test", "resources", "sftp", "README.md");
        Files.copy(src, dst, StandardCopyOption.REPLACE_EXISTING);

    }

    @Test
    public void CreateFile() throws IOException {

        Path dst = sftpFileSystem.getPath("target", "CreateFileTest.txt");
        Files.createFile(dst);

    }

    /* The path of file system created with an URI without path parts
     * default to the working directory
     */
    @Test
    public void isDirectory() throws IOException {

        Path dst = sftpFileSystem.getPath("");
        boolean isDirectory = Files.isDirectory(dst);
        assertEquals("This is a directory",true, isDirectory);

    }


}

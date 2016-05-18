package com.gerardnico.niofs.sftp;

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



    @Test
    public void visitFile() throws IOException {

        FileSystem fileSystem = FileSystemFactory.get();
        Path start = fileSystem.getPath(".");
        Files.walkFileTree(start,new SimpleFileVisitor());
        FileSystemFactory.close();
    }

}

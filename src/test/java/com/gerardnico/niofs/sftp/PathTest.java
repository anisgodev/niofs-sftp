package com.gerardnico.niofs.sftp;

import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerard on 23-05-2016.
 * The path operations of the class {@link java.nio.file.Path}
 *
 * Most of the below test can be found in the tutorial with the Solaris Syntax
 * http://docs.oracle.com/javase/tutorial/essential/io/pathOps.html
 */
public class PathTest {




    @Test
    public void get() throws IOException {

        // Solaris syntax
        URI uri = URI.create("sftp:/home/joe/foo");
        Path path = Paths.get(uri);

        assertEquals("path.toString","/home/joe/foo",path.toString());
        assertEquals("path.getFileName","foo",path.getFileName());
        assertEquals("path.getName(0)","home",path.getName(0));
        assertEquals("path.getNameCount",3,path.getNameCount());
        assertEquals("path.subpath","home/joe",path.subpath(0,2));
        assertEquals("path.getParent()","/home/joe",path.getParent());
        assertEquals("path.getRoot","/",path.getRoot());

    }


}

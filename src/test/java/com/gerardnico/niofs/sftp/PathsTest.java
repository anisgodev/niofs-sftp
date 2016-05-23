package com.gerardnico.niofs.sftp;

import com.sun.jndi.toolkit.url.Uri;
import org.junit.Test;

import java.io.IOException;
import java.net.URI;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.Assert.assertEquals;


/**
 * Created by gerard on 23-05-2016.
 * The path operations of the class {@link java.nio.file.Paths}
 */
public class PathsTest {


    @Test
    public void get() throws IOException {

        String url = UriParameters.URL+UriParameters.HOME_USER_DIR+"/src/test/resources/sftp/README.md";
        Path path = Paths.get(URI.create(url));
        assertEquals("The file ("+url+") must exist",true, Files.exists(path));


    }


}

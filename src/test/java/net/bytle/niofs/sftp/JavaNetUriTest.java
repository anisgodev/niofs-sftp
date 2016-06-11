package net.bytle.niofs.sftp;

import org.junit.Test;
import org.junit.rules.ExpectedException;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;

import static org.junit.Assert.assertEquals;

/**
 * Created by gerard on 26-05-2016.
 * Test on the JDK URI
 * http://docs.oracle.com/javase/8/docs/api/java/net/URI.html
 */
public class JavaNetUriTest {


    /**
     * We got the error because the path must be absolute as a scheme is specified.
     *
     * @throws URISyntaxException: Relative path in absolute URI: sftp://hostrelativePath#fragment
     */
    @Test(expected = java.net.URISyntaxException.class)
    public void pathMustBeAbsoluteWithScheme() throws URISyntaxException {

        URI pathUri  = new URI("sftp", null, null, -1, "relativePath", null, null);
        // An absolute URI specifies a scheme
        assertEquals("This test must fail because the path is relative and that we have a scheme", "sftp://host:re", pathUri.toString());


    }

    /**
     * We can use a relative path without a scheme :)
     * Ps A relative path is a path that doesn't begin by a root (/ or C:\, ...)
     */
    @Test()
    public void pathCanBeRelativeWithoutScheme() throws URISyntaxException {

        String relativePath = "../relativePath";
        URI pathUri  = new URI(null, null, null, -1, relativePath, null, null);
        // An absolute URI specifies a scheme
        assertEquals("Relative to current directory, point to the parent, without scheme", relativePath, pathUri.toString());

        relativePath = "relativePath";
        pathUri  = new URI(null, null, null, -1, relativePath, null, null);
        // An absolute URI specifies a scheme
        assertEquals("Relative to current directory, point to the working dir, without scheme", relativePath, pathUri.toString());

        relativePath = "./relativePath";
        pathUri  = new URI(null, null, null, -1, relativePath, null, null);
        // An absolute URI specifies a scheme
        assertEquals("Relative to current directory, point to the working dir, without scheme", relativePath, pathUri.toString());

    }

    /**
     * A path with a scheme
     *
     */
    @Test()
    public void pathAbsoluteWithScheme() throws URISyntaxException {

        URI pathUri  = new URI("sftp", null, null, -1, "/relativePath", null, null);
        // An absolute URI specifies a scheme
        assertEquals("This test must fail because the path is relative and that we have a scheme", "sftp:/relativePath", pathUri.toString());


    }

    /**
     * A path with a scheme and a host
     *
     */
    @Test()
    public void pathAbsoluteWithSchemeAndHost() throws URISyntaxException {

        URI pathUri  = new URI("sftp", null, "localhost", -1, "/relativePath", null, null);
        // An absolute URI specifies a scheme
        assertEquals("This test must fail because the path is relative and that we have a scheme", "sftp://localhost/relativePath", pathUri.toString());


    }

}

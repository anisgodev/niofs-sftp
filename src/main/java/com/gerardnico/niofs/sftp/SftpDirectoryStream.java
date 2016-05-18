package com.gerardnico.niofs.sftp;

import java.io.IOException;
import java.nio.file.DirectoryStream;
import java.nio.file.NotDirectoryException;
import java.nio.file.Path;
import java.util.Iterator;

/**
 * Created by gerard on 18-05-2016.
 */
public class SftpDirectoryStream implements DirectoryStream<Path> {

    private final SftpPath path;
    private final Filter<? super Path> filter;
    private final SftpBasicFileAttributes fileAttribute;

    public SftpDirectoryStream(SftpPath path, Filter<? super Path> filter) throws IOException {
        this.path = path;
        this.filter = filter;

        // Sanity check: is it a directory
        this.fileAttribute = new SftpBasicFileAttributes(path);
        if (!fileAttribute.isDirectory()) throw new NotDirectoryException(this.path.toString());
    }

    @Override
    public Iterator<Path> iterator() {
        // To continue
        throw new UnsupportedOperationException();

    }

    @Override
    public void close() throws IOException {

        throw new UnsupportedOperationException();

    }
}

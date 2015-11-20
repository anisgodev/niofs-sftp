package com.gerardnico.niofs.sftp;

import java.io.IOException;
import java.net.URI;
import java.nio.channels.FileChannel;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.nio.file.attribute.FileAttribute;
import java.nio.file.attribute.FileAttributeView;
import java.nio.file.spi.FileSystemProvider;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;

/**
 * Created by gerard on 20-11-2015.
 * A factory for a Sftp file system
 */
public class SftpFileSystemProvider extends FileSystemProvider {

    static final String SFTP_SCHEME = "sftp";

    // The pool of Sftp Connection
    private static final Map<URI,SftpFileSystem> fileSystemPool = new HashMap<URI, SftpFileSystem>();

    @Override
    public String getScheme() {
        return SFTP_SCHEME;
    }

    /**
     * The newFileSystem method is used to create a file system
     */
    @Override
    public FileSystem newFileSystem(URI uri, Map<String, ?> env) throws IOException {

        // Build a Sftp File System object
        SftpFileSystem sftpFileSystem = new SftpFileSystem.SftpFileSystemBuilder(this,uri)
                .build();

        // Add the file system in the pool
        if (fileSystemPool.containsKey(uri)) {
            throw new FileSystemAlreadyExistsException();
        } else {
            fileSystemPool.put(uri, sftpFileSystem);
        }

        // Return it
        return sftpFileSystem;

    }

    /**
     * @param path
     * @param options
     * @param attrs
     * @return A FileChannel object that allows a file to be read or written in the file system.
     * @throws IOException
     */
    @Override
    public FileChannel newFileChannel(Path path,
                                      Set<? extends OpenOption> options,
                                      FileAttribute<?>... attrs)
            throws IOException
    {
        return new SftpFileChannel();
    }

    /**
     * The getFileSystem method is used to retrieve a reference to an existing file system
     * @param uri
     * @return a FileSystem
     */
    @Override
    public FileSystem getFileSystem(URI uri) {

        return fileSystemPool.get(uri);

    }

    @Override
    public Path getPath(URI uri) {

        return getFileSystem(uri).getPath(uri.getPath());

    }

    @Override
    public SeekableByteChannel newByteChannel(Path path, Set<? extends OpenOption> options, FileAttribute<?>... attrs) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public DirectoryStream<Path> newDirectoryStream(Path dir, DirectoryStream.Filter<? super Path> filter) throws IOException {
        throw new UnsupportedOperationException();
    }

    /**
     * Attempts to write to file stores by means of an object associated with a read-only file system throws ReadOnlyFileSystemException.
     * @param dir
     * @param attrs
     * @throws IOException
     */
    @Override
    public void createDirectory(Path dir, FileAttribute<?>... attrs) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void delete(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void copy(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void move(Path source, Path target, CopyOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isSameFile(Path path, Path path2) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public boolean isHidden(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public FileStore getFileStore(Path path) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void checkAccess(Path path, AccessMode... modes) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public <V extends FileAttributeView> V getFileAttributeView(Path path, Class<V> type, LinkOption... options) {
        throw new UnsupportedOperationException();
    }

    @Override
    public <A extends BasicFileAttributes> A readAttributes(Path path, Class<A> type, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public Map<String, Object> readAttributes(Path path, String attributes, LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    @Override
    public void setAttribute(Path path, String attribute, Object value, LinkOption... options) throws IOException {

        throw new UnsupportedOperationException();

    }
}

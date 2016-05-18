package com.gerardnico.niofs.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.JSch;
import com.jcraft.jsch.Session;

import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.attribute.UserPrincipalLookupService;
import java.nio.file.spi.FileSystemProvider;
import java.util.*;
import java.util.logging.Logger;

/**
 * Created by gerard on 20-11-2015.
 * A file system that you obtain with the factor {@link SftpFileSystemProvider}
 * <p/>
 * The default file system, obtained by invoking the FileSystems.getDefault method, provides access to the file system that is accessible to the Java virtual machine.
 */
public class SftpFileSystem extends FileSystem {

    private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());


    private final URI uri;

    private final Session session;

    /**
     * Is used in the system file provider
     * to check the access
     * @return ChannelSftp
     */
    protected ChannelSftp getChannelSftp() {
        return channelSftp;
    }

    private final ChannelSftp channelSftp;
    private final SftpFileSystemProvider sftpFileSystemProvider;

    /**
     * A file system is open upon creation
     *
     * @param sftpFileSystemBuilder
     */
    private SftpFileSystem(SftpFileSystemBuilder sftpFileSystemBuilder) {

        // Uri
        this.uri = sftpFileSystemBuilder.uri;
        this.sftpFileSystemProvider = sftpFileSystemBuilder.sftpFileSystemProvider;

        // Extract the user and the password
        String userInfo = this.uri.getUserInfo();
        String user = userInfo.substring(0, userInfo.indexOf(":"));
        String password = userInfo.substring(userInfo.indexOf(":")+1, userInfo.length());

        // Port
        int port;
        if (this.uri.getPort()==-1) {
            port = 22;
        }  else {
            port = this.uri.getPort();
        }

        try {

            LOGGER.info("Trying to connect to the sftp connection (Uri: " + uri + "' )");
            JSch jsch = new JSch();

            // SSH Session
            this.session = jsch.getSession(user, uri.getHost(), port);
            session.setPassword(password);
            java.util.Properties config = new java.util.Properties();
            config.put("StrictHostKeyChecking", "no");
            session.setConfig(config);
            session.connect();

            // Channel used (sftp, exec ....
            this.channelSftp = (ChannelSftp) session.openChannel("sftp");
            this.channelSftp.connect();


        } catch (Exception e) {

            throw new RuntimeException(e);

        }

    }

    @Override
    public FileSystemProvider provider() {

        return this.sftpFileSystemProvider;
    }

    /**
     * A file system is open upon creation and can be closed by invoking its close method. Once closed, any further attempt to access objects in the file system cause ClosedFileSystemException to be thrown.
     * Closing a file system causes all open channels, watch services, and other closeable objects associated with the file system to be closed.
     */
    @Override
    public void close() throws IOException {
        this.channelSftp.disconnect();
        this.session.disconnect();
    }

    /**
     * A file system is open upon creation and can be closed by invoking its close method. Once closed, any further attempt to access objects in the file system cause ClosedFileSystemException to be thrown.
     */
    @Override
    public boolean isOpen() {

        return !this.channelSftp.isClosed();
    }

    /**
     * Whether or not a file system provides read-only access is established when the FileSystem is created and can be tested by invoking its isReadOnly
     *
     * @return boolean if true
     */
    @Override
    public boolean isReadOnly() {

        throw new UnsupportedOperationException();

    }

    /**
     * The name separator is used to separate names in a path string.
     *
     * @return
     */
    @Override
    public String getSeparator() {

        return "/";
    }

    @Override
    public Iterable<Path> getRootDirectories() {
        ArrayList<Path> rootDirectories = new ArrayList<Path>();
        Path rootPath = SftpPath.get(this, SftpPath.ROOT_PREFIX);
        rootDirectories.add(rootPath);
        return rootDirectories;
    }

    @Override
    public Iterable<FileStore> getFileStores() {

        throw new UnsupportedOperationException();

    }

    @Override
    public Set<String> supportedFileAttributeViews() {
        return Collections.unmodifiableSet(
                new HashSet<>(Arrays.asList("basic", "posix")));
    }

    @Override
    public Path getPath(String first, String... more) {
        String path;
        if (more.length == 0) {
            path = first;
        } else {
            // Build the path from the list of directory
            StringBuilder sb = new StringBuilder();
            sb.append(first);
            for (String segment : more) {
                if (segment.length() > 0) {
                    if (sb.length() > 0)
                        sb.append('/');
                    sb.append(segment);
                }
            }
            path = sb.toString();
        }
        return SftpPath.get(this,path);
    }

    @Override
    public PathMatcher getPathMatcher(String syntaxAndPattern) {
        throw new UnsupportedOperationException();
    }

    @Override
    public UserPrincipalLookupService getUserPrincipalLookupService() {
        throw new UnsupportedOperationException();
    }

    @Override
    public WatchService newWatchService() throws IOException {
        throw new UnsupportedOperationException();
    }

    public static class SftpFileSystemBuilder {

        private final SftpFileSystemProvider sftpFileSystemProvider;
        private final URI uri;

        public SftpFileSystemBuilder(SftpFileSystemProvider sftpFileSystemProvider, URI uri) {
            this.sftpFileSystemProvider = sftpFileSystemProvider;
            this.uri = uri;
        }


        public SftpFileSystem build() {
            return new SftpFileSystem(this);
        }

    }
}

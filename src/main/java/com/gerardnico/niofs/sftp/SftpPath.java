/*
 Copyright 2012-2013 University of Stavanger, Norway

 Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

       http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and
   limitations under the License.
 */

package com.gerardnico.niofs.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;

/**
 * A Path implementation for SFTP.
 * An object that may be used to locate a file in a file system.
 * It will typically represent a system dependent file stringPath.
 *
 * Let op:
 * This sftp client has the concept of a current local directory and a current remote directory. These are not inherent to the protocol,
 * but are used implicitly for all path-based commands sent to the server (for the remote directory) or accessing the local file system
 * (for the local directory).
 * They can be queried by lpwd() and pwd(), and changed by cd(dir) and lcd(dir).
 */
class SftpPath implements Path {


    protected static final String ROOT_PREFIX = "/";
    protected final String PATH_SEPARATOR; // To avoid duplicate. It is initialized with the value of FileSystem.getSeparator();
    private final SftpFileSystem sftpFileSystem;
    private String stringPath;
    private String[] folders;


    private SftpPath(SftpFileSystem sftpFileSystem, String stringPath) {

        this.sftpFileSystem = sftpFileSystem;
        this.stringPath = stringPath;
        this.PATH_SEPARATOR = sftpFileSystem.getSeparator();
        this.folders = stringPath.split( PATH_SEPARATOR );

    }


    public FileSystem getFileSystem() {
        return this.sftpFileSystem;
    }

    public boolean isAbsolute() {
        if (this.stringPath.startsWith(ROOT_PREFIX)) {
            return true;
        } else {
            return false;
        }

    }

    public Path getRoot() {
        throw new UnsupportedOperationException();
    }

    public Path getFileName() {
        // FilenameUtils.getName(this.getPath()
        throw new UnsupportedOperationException();
    }

    public Path getParent() {
        String parent = "";
        for ( String folder : this.folders ) {
            if ( folder.length() > 0 ) {
                parent += folder + PATH_SEPARATOR;
            }
        }
        return new SftpPath(sftpFileSystem, parent);
    }

    public int getNameCount() {
        throw new UnsupportedOperationException();
    }

    public Path getName(int index) {
        throw new UnsupportedOperationException();
    }

    public Path subpath(int beginIndex, int endIndex) {
        throw new UnsupportedOperationException();
    }

    public boolean startsWith(Path other) {
        throw new UnsupportedOperationException();
    }

    public boolean startsWith(String other) {
        throw new UnsupportedOperationException();
    }

    public boolean endsWith(Path other) {
        throw new UnsupportedOperationException();
    }

    public boolean endsWith(String other) {
        throw new UnsupportedOperationException();
    }

    public Path normalize() {
        throw new UnsupportedOperationException();
    }

    public Path resolve(Path other) {
        throw new UnsupportedOperationException();
    }

    public Path resolve(String other) {
        throw new UnsupportedOperationException();
    }

    public Path resolveSibling(Path other) {
        throw new UnsupportedOperationException();
    }

    public Path resolveSibling(String other) {
        throw new UnsupportedOperationException();
    }

    public Path relativize(Path other) {
        throw new UnsupportedOperationException();
    }

    public URI toUri() {
        throw new UnsupportedOperationException();
    }

    public Path toAbsolutePath() {
        if (this.isAbsolute()) {
            return this;
        } else {
            try {
                return get(sftpFileSystem,sftpFileSystem.getChannelSftp().getHome()+sftpFileSystem.getSeparator()+this.stringPath);
            } catch (SftpException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * A static constructor
     * You canm also get a stringPath from a provider with an URI.
     * @param sftpFileSystem
     * @param path
     * @return
     */
    protected static Path get(SftpFileSystem sftpFileSystem, String path) {
        return new SftpPath(sftpFileSystem,path);
    }

    public Path toRealPath(LinkOption... options) throws IOException {
        throw new UnsupportedOperationException();
    }

    public File toFile() {
        throw new UnsupportedOperationException();
    }

    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        throw new UnsupportedOperationException();
    }

    public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
        throw new UnsupportedOperationException();
    }

    public Iterator<Path> iterator() {
        throw new UnsupportedOperationException();
    }

    public int compareTo(Path other) {
        throw new UnsupportedOperationException();
    }

    protected SftpPosixFileAttributes getFileAttributes() throws IOException {
        return new SftpPosixFileAttributes(this);
    }

    public String toString() {
        return this.stringPath;
    }

    /**
     * A shortcut to get the ChannelSftp saved in the file systen object
     * @return ChannelSftp
     */
    protected ChannelSftp getChannelSftp() {
        return this.sftpFileSystem.getChannelSftp();
    }

    /**
     * String Path representation used internally to make all Sftp operations
     * @return
     */
    protected String getStringPath() {
        return stringPath;
    }

    /**
     * Implementation of the createDirectory function of the FileSystemProvider
     * @throws SftpException
     */
    protected void createDirectory() throws SftpException {

        for ( String folder : this.folders ) {
            if ( folder.length() > 0 ) {
                try {
                    this.getChannelSftp().cd( folder );
                }
                catch ( SftpException e ) {
                    this.getChannelSftp().mkdir( folder );
                    this.getChannelSftp().cd( folder );
                }
            }
        }
    }
}
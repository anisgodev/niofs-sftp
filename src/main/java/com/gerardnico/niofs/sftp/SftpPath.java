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

import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import com.sun.nio.zipfs.ZipFileAttributes;

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
 * It will typically represent a system dependent file path.
 */
class SftpPath implements Path {


    protected static final String ROOT_PREFIX = "/";
    protected final String PATH_SEPARATOR; // To avoid duplicate. It is initialized with the value of FileSystem.getSeparator();
    private final String path;
    private final SftpFileSystem sftpFileSystem;


    private SftpPath(SftpFileSystem sftpFileSystem, String path) {

        this.sftpFileSystem = sftpFileSystem;
        this.path = path;
        this.PATH_SEPARATOR = sftpFileSystem.getSeparator();

    }


    public FileSystem getFileSystem() {
        return this.sftpFileSystem;
    }

    public boolean isAbsolute() {
        if (this.path.startsWith(ROOT_PREFIX)) {
            return true;
        } else {
            return false;
        }

    }

    public Path getRoot() {
        throw new UnsupportedOperationException();
    }

    public Path getFileName() {
        throw new UnsupportedOperationException();
    }

    public Path getParent() {
        throw new UnsupportedOperationException();
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
                return get(sftpFileSystem,sftpFileSystem.getChannelSftp().getHome()+sftpFileSystem.getSeparator()+this.path);
            } catch (SftpException e) {
                throw new RuntimeException(e);
            }

        }

    }

    /**
     * A static constructor
     * You canm also get a path from a provider with an URI.
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

    protected SftpPosixFileAttributes getFileAttributes() {
        return new SftpPosixFileAttributes(this);
    }

    public String toSring() {
        return this.path;
    }

    void checkAccess(AccessMode... modes) throws IOException {
        boolean w = false;
        boolean x = false;
        for (AccessMode mode : modes) {
            switch (mode) {
                case READ:
                    break;
                case WRITE:
                    w = true;
                    break;
                case EXECUTE:
                    x = true;
                    break;
                default:
                    throw new UnsupportedOperationException();
            }
        }
//        ZipFileAttributes attrs = this.sftpFileSystem.getChannelSftp().getFileAttributes(getResolvedPath());
//        if (attrs == null && (path.length != 1 || path[0] != '/'))
//            throw new NoSuchFileException(toString());
//        if (w) {
//            if (zfs.isReadOnly())
//                throw new AccessDeniedException(toString());
//        }
//        if (x)
//            throw new AccessDeniedException(toString());
    }
}
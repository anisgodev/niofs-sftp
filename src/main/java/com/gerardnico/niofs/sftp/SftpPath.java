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

import java.io.File;
import java.io.IOException;
import java.net.URI;
import java.nio.file.*;
import java.nio.file.WatchEvent.Kind;
import java.nio.file.WatchEvent.Modifier;
import java.util.Iterator;

/**
 * A Path implementation for SFTP.
 */
public class SftpPath implements Path {


    static final String ROOT_PATH = "/";
    private static final String PATH_SEP = "/";
    private final String path;
    private final SftpFileSystem sftpFileSystem;


    private SftpPath(SftpPathBuilder sftpPathBuilder) {
        this.sftpFileSystem = sftpPathBuilder.sftpFileSystem;
        this.path = sftpPathBuilder.path;

    }


    public FileSystem getFileSystem() {
        return this.sftpFileSystem;
    }


    public boolean isAbsolute() {

        return path.startsWith(PATH_SEP);
    }


    public Path getRoot() {
        if (path.equals(ROOT_PATH)) {
            return this;
        }
        return new SftpPath.SftpPathBuilder(this.sftpFileSystem, ROOT_PATH).build();
    }


    public Path getFileName() {
        // TODO Auto-generated method stub
        return null;
    }


    public Path getParent() {
        // TODO Auto-generated method stub
        return null;
    }


    public int getNameCount() {
        // TODO Auto-generated method stub
        return 0;
    }


    public Path getName(int index) {
        // TODO Auto-generated method stub
        return null;
    }


    public Path subpath(int beginIndex, int endIndex) {
        // TODO Auto-generated method stub
        return null;
    }


    public boolean startsWith(Path other) {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean startsWith(String other) {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean endsWith(Path other) {
        // TODO Auto-generated method stub
        return false;
    }


    public boolean endsWith(String other) {
        // TODO Auto-generated method stub
        return false;
    }


    public Path normalize() {
        // TODO Auto-generated method stub
        return null;
    }


    public Path resolve(Path other) {
        // TODO Auto-generated method stub
        return null;
    }


    public Path resolve(String other) {
        // TODO Auto-generated method stub
        return null;
    }

    public Path resolveSibling(Path other) {
        // TODO Auto-generated method stub
        return null;
    }

    public Path resolveSibling(String other) {
        // TODO Auto-generated method stub
        return null;
    }


    public Path relativize(Path other) {
        // TODO Auto-generated method stub
        return null;
    }


    public URI toUri() {
        // TODO Auto-generated method stub
        return null;
    }


    public Path toAbsolutePath() {
        // TODO Auto-generated method stub
        return null;
    }


    public Path toRealPath(LinkOption... options) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }


    public File toFile() {

        throw new UnsupportedOperationException();
    }


    public WatchKey register(WatchService watcher, Kind<?>[] events, Modifier... modifiers) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }


    public WatchKey register(WatchService watcher, Kind<?>... events) throws IOException {
        // TODO Auto-generated method stub
        return null;
    }


    public Iterator<Path> iterator() {
        // TODO Auto-generated method stub
        return null;
    }


    public int compareTo(Path other) {
        // TODO Auto-generated method stub
        return 0;
    }

    public String getPathString() {
        return this.path;
    }

    public static class SftpPathBuilder {
        private final SftpFileSystem sftpFileSystem;
        private final String path;

        public SftpPathBuilder(SftpFileSystem sftpFileSystem, String s) {
            this.sftpFileSystem = sftpFileSystem;
            this.path = s;
        }

        public SftpPath build(){
            return new SftpPath(this);
        }
    }
}
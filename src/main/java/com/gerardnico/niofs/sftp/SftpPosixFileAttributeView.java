package com.gerardnico.niofs.sftp;

import java.io.IOException;
import java.nio.file.Path;
import java.nio.file.attribute.*;
import java.util.Set;

/**
 * Created by gerard on 02-05-2016.
 * Manage the permissions
 *
 * For the dates, see {@link SftpBasicFileAttributes}
 */
public class SftpPosixFileAttributeView implements PosixFileAttributeView {

    private final SftpPath path;

    public SftpPosixFileAttributeView(SftpPath path) {
        this.path = path;
    }

    public String name() {
            return "Sftp Posix File Attribute View";
    }

    public UserPrincipal getOwner() throws IOException {
        return null;
    }

    public void setOwner(UserPrincipal owner) throws IOException {

    }

    public PosixFileAttributes readAttributes() throws IOException {
        return null;
    }

    public void setTimes(FileTime lastModifiedTime, FileTime lastAccessTime, FileTime createTime) throws IOException {

    }

    public void setPermissions(Set<PosixFilePermission> perms) throws IOException {

    }

    public void setGroup(GroupPrincipal group) throws IOException {

    }

    public static <V extends FileAttributeView> V get(Path path) {
        return (V) new SftpPosixFileAttributeView((SftpPath) path);
    }
}

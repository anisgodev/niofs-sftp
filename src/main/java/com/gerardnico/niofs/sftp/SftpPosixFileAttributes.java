package com.gerardnico.niofs.sftp;

import com.jcraft.jsch.SftpException;

import java.nio.file.attribute.GroupPrincipal;
import java.nio.file.attribute.PosixFileAttributes;
import java.nio.file.attribute.PosixFilePermission;
import java.nio.file.attribute.UserPrincipal;
import java.util.Set;

/**
 * Created by gerard on 21-11-2015.
 */
public class SftpPosixFileAttributes extends SftpBasicFileAttributes implements PosixFileAttributes  {

    protected SftpPosixFileAttributes(SftpPath path) {
        super(path);
    }

    public UserPrincipal owner() {
        return new SftpUserPrincipal(this.attrs.getUId());
    }

    public GroupPrincipal group() {
        return new SftpGroupPrincipal(this.attrs.getUId());
    }

    public Set<PosixFilePermission> permissions() {
        throw new UnsupportedOperationException();
    }

}

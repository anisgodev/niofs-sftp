package com.gerardnico.niofs.sftp;

import javax.security.auth.Subject;
import java.nio.file.attribute.UserPrincipal;

/**
 * Created by gerard on 21-11-2015.
 */
public class SftpUserPrincipal extends SftpPrincipal implements UserPrincipal {



    protected SftpUserPrincipal(int uId) {
        super(uId);
    }


}

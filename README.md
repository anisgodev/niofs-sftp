# NIOFS Sftp


Provides access to the files on an SFTP server (that is, an SSH or SCP server).

## URI Format
The format is borrowed from Apache Common VFS.

    sftp://[ username[: password]@] hostname[: port][ relative-path]

## Examples

    sftp://myusername:mypassword@somehost/pub/downloads/somefile.tgz

## Configuration

By default, the path is relative to the user's home directory. This can be changed with:

```java
FtpFileSystemConfigBuilder.getInstance().setUserDirIsRoot(options, false);
```
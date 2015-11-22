# NIOFS Sftp


Provides access to the files on an SFTP server (that is, an SSH or SCP server).

## URI Format
The format is borrowed from Apache Common VFS.

    sftp://[ username[: password]@] hostname[: port][ relative-path]

## Examples

    sftp://myusername:mypassword@somehost


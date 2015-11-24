# NIOFS Sftp


Provides access to the files on an SFTP server (that is, an SSH or SCP server).

## URI Format
The format is borrowed from Apache Common VFS.

    sftp://[ username[: password]@] hostname[: port][ relative-path]

where:

  * User and password must be encoded
  
## Examples

    sftp://myusername:mypassword@somehost


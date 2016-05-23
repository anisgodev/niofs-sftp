# NIOFS Sftp


Provides access to the files on an SFTP server (that is, an SSH or SCP server).

## URI Format

    sftp://[ username[: password]@] hostname[: port][ Path  ]

where:

  * User and password must be [encoded](https://en.wikipedia.org/wiki/Percent-encoding) as it's an URI format. 
  * Path is absolute if it begins with a [root directory](https://docs.oracle.com/javase/8/docs/api/java/nio/file/FileSystem.html#getRootDirectories--). Otherwise, it's relative to the [working directory](#Working Directory). 
  

  
## Examples

  * With [Paths](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html)

```java
Path myPath = Paths.get("sftp://myusername:mypassword@somehost:2222/tmp";
```

  * With the creation of file system, you can change the [working directory](http://gerardnico.com/wiki/file_system/working_directory) and gives relative path. When not define, it default normally to the user's home directory.

```java

URI uri = new URI("sftp://" + user + ":" + pwd + "@" + host + ":" + port);
Map<String, String> env = new HashMap<>();
env.put("working.directory",/myWorkingDirectory");

FileSystem sftpFileSystem = sftpFileSystemProvider.newFileSystem(uri, env);
Path path = sftpFileSystem.getPath("myRelativePath"); 

```
# NIOFS Sftp

## Introduction

Provides access to the files on an SFTP server (that is, an SSH or SCP server).
 

## Get an absolute path with a full URI Format

    sftp://[ username[: password]@] hostname[: port][ path ]

where:

  * User and password must be [encoded](https://en.wikipedia.org/wiki/Percent-encoding) as it's an URI format. 
  * Path is always absolute. See  The path of a hierarchical URI that specifies an authority is always absolute. See [java.net.URI](http://docs.oracle.com/javase/8/docs/api/java/net/URI.html)

With [Paths](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html)

```java
Path myPath = Paths.get("sftp://myusername:mypassword@somehost:2222/tmp";
```

Generally, the path in this form is an absolute path but if the path is relative, it will be relative to the user home directory.

## Get a relative path from a Working Directory

During the instantiation of the file system, you can set the [working directory](http://gerardnico.com/wiki/file_system/working_directory). 
When not define, the working directory default normally to the user's home directory.

```java

URI uri = new URI("sftp://" + user + ":" + pwd + "@" + host + ":" + port);
Map<String, String> env = new HashMap<>();
env.put("working.directory","/myWorkingDirectory");

FileSystem sftpFileSystem = sftpFileSystemProvider.newFileSystem(uri, env);
Path path = sftpFileSystem.getPath("myRelativePath"); 

```

## Implementation

  * Operating System: Actually, only a Linux Server is supported (ie the root begins with "/".
  * The file system is not fully developed and tested against concurrency.
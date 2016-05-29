# NIOFS Sftp

## Introduction

Provides access to the files on an SFTP server (that is, an SSH or SCP server).
 

## Get an absolute path with a absolute URI Format

The below URI is absolute because it specifies a scheme:

    sftp://[ username[: password]@] hostname[: port][ /absolute/path ]

where:

  * User and password must be [encoded](https://en.wikipedia.org/wiki/Percent-encoding) as it's an URI format. 
  * Path is always absolute with a scheme. It must then begin with a `/`

With [Paths](https://docs.oracle.com/javase/8/docs/api/java/nio/file/Paths.html):

```java
URI uri = URI.create("sftp://myusername:mypassword@somehost:2222/tmp");
Path myPath = Paths.get(uri);

// And don't forget to close the file system when you don't need it anymore
myPath.getFileSystem().close();
```

The id of a sftp file system is the combination of user, host, port and working directory. Then if you need a second file from the same file system, you can omit the password.
Example:
```java
URI uri = URI.create("sftp://myusername@somehost:2222/my/Other/Absolute/Path");
Path mySecondPath = Paths.get(uri);
```
You can also just ask the file system from the first path `myPath` and then create a new path (even a relative one). 
Example:
```java
Path mySecondPath = myPath.getFileSystem().getPath("myRelativePath");
```


## Get the Sftp File System

  * Through the installed provider
```java
for (FileSystemProvider fileSystemProvider : FileSystemProvider.installedProviders()) {
   if (SftpFileSystemProvider.SFTP_SCHEME.equals(fileSystemProvider.getScheme())) {
       sftpFileSystemProvider = fileSystemProvider;
   }
}
```
  * With a path
```java
FileSystem sftpFileSystem = Paths.get(URI.create("sftp:/")).getFileSystem();
```

Don't forget to close it when it's no more needed.
```java
sftpFileSystem.close();
```


## Get a relative path from a Working Directory

During the instantiation of the file system, you can set the [working directory](http://gerardnico.com/wiki/file_system/working_directory). 
When not defined, the working directory default normally to the user's home directory.

```java
URI uri = new URI("sftp://" + user + ":" + pwd + "@" + host + ":" + port);
Map<String, String> env = new HashMap<>();
env.put("working.directory","/myWorkingDirectory");

FileSystem sftpFileSystem = FileSystemProvider.newFileSystem(uri, env);
Path path = sftpFileSystem.getPath("myRelativePath"); 
```

## Implementation

  * Operating System: Actually, only a Linux/Solaris/Unix Server is supported (ie the root begins with "/").
  * The file system is not fully developed and tested against concurrency.
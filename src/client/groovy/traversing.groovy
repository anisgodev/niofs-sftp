import java.nio.file.Files
import java.nio.file.Paths

/**
 * Created by gerard on 07-06-2016.
 */

def env = System.getenv();
USER = env.get("NIOFS_SFTP_USER");
PWD = env.get("NIOFS_SFTP_PWD");
HOST = env.get("NIOFS_SFTP_HOST");
PORT = env.get("NIOFS_SFTP_PORT");

println(USER);

uri = URI.create("sftp://" + USER + ":" + PWD + "@" + HOST + ":" + PORT);
path = Paths.get(uri);

try {

    if (Files.isDirectory(path)) {
        println(path.toAbsolutePath().toString()+" is a directory")
    } else {
        println(path.toAbsolutePath().toString()+" is not a directory")
    }

} finally {

    path.getFileSystem().close();

}





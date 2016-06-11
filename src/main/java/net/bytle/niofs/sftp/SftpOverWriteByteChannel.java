package net.bytle.niofs.sftp;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.SftpException;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.*;

/**
 * Created by gerard on 22-11-2015.
 * A channel to (write|overwrite) a new File
 *
 * SFTP knows only three transfer mode RESUME, APPEND, OVERWRITE
 *
 */
public class SftpOverWriteByteChannel implements SeekableByteChannel {

    private final SftpFileProgressMonitor monitor;
    WritableByteChannel writableByteChannel;

    protected SftpOverWriteByteChannel(SftpPath path) {
        try {
            monitor = new SftpFileProgressMonitor();
            writableByteChannel = Channels.newChannel(path.getChannelSftp().put(path.getStringPath(), monitor, ChannelSftp.OVERWRITE));
        } catch (SftpException e) {
            throw new RuntimeException(e);
        }
    }

    public int read(ByteBuffer dst) throws IOException {
        throw new NonReadableChannelException();
    }

    public int write(ByteBuffer src) throws IOException {
        return writableByteChannel.write(src);
    }

    public long position() throws IOException {
        // Need to get a monitor from the channelsftp.put method
        // To implement it
        return monitor.getCount();
    }

    public SeekableByteChannel position(long newPosition) throws IOException {
        throw new UnsupportedOperationException("With SFTP you cannot change the position");
    }

    public long size() throws IOException {
        throw new UnsupportedOperationException();
    }

    public SeekableByteChannel truncate(long size) throws IOException {
        throw new UnsupportedOperationException();
    }

    public boolean isOpen() {
        return writableByteChannel.isOpen();
    }

    public void close() throws IOException {
        writableByteChannel.close();
    }
}

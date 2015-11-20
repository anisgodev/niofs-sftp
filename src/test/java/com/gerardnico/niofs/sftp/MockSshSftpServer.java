package com.gerardnico.niofs.sftp;

import org.apache.sshd.SshServer;
import org.apache.sshd.common.NamedFactory;
import org.apache.sshd.server.Command;
import org.apache.sshd.server.CommandFactory;
import org.apache.sshd.server.PasswordAuthenticator;
import org.apache.sshd.server.command.ScpCommandFactory;
import org.apache.sshd.server.keyprovider.SimpleGeneratorHostKeyProvider;
import org.apache.sshd.server.session.ServerSession;
import org.apache.sshd.server.sftp.SftpSubsystem;
import org.apache.sshd.server.shell.ProcessShellFactory;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

/**
 * Created by gerard on 9/17/2015.
 * Credit:
 * https://dzone.com/articles/spring-integration-mock-0
 * https://mina.apache.org/sshd-project/embedding_ssh.html
 */
public class MockSshSftpServer {
    static public final int PORT = 22999;
    private final String TIME_OUT = String.valueOf(5000.0);
    private SshServer sshd;

    private static final Logger LOGGER = Logger.getLogger(Thread.currentThread().getStackTrace()[0].getClassName());

    public MockSshSftpServer() {

        sshd = SshServer.setUpDefaultServer();

        sshd.setPort(PORT);

        sshd.setKeyPairProvider(new SimpleGeneratorHostKeyProvider("/src/test/resources/Sftp/hostkey.ser"));

        sshd.setPasswordAuthenticator(new PasswordAuthenticator() {

            public boolean authenticate(String username, String password, ServerSession session) {

                return true;

            }

        });

        // 5000 ms

        sshd.getProperties().put(SshServer.IDLE_TIMEOUT, TIME_OUT);

        CommandFactory myCommandFactory = new CommandFactory() {

            public Command createCommand(String command) {
                // to execute commands as processes on the server side
                // https://mina.apache.org/sshd-project/tips.html
                LOGGER.info("Command to execute received: " + command);
                Command command1 = new ProcessShellFactory(command.split(" ")).create();
                return command1;
            }

        };
        sshd.setCommandFactory(new ScpCommandFactory(myCommandFactory));

        sshd.setShellFactory(new ProcessShellFactory());


        List<NamedFactory<Command>> namedFactoryList = new ArrayList<NamedFactory<Command>>();
        namedFactoryList.add(new SftpSubsystem.Factory());
        sshd.setSubsystemFactories(namedFactoryList);


    }

    private PasswordAuthenticator PasswordAuthenticator() {
        return new PasswordAuthenticator() {
            //@Override
            public boolean authenticate(String arg0, String arg1, ServerSession arg2) {
                return true;
            }
        };
    }

    public void start() {
        try {
            sshd.start();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void stop() {
        try {
            sshd.stop();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        sshd = null;
    }
}
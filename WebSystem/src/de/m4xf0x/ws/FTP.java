package de.m4xf0x.ws;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.PrintWriter;

import org.apache.commons.net.PrintCommandListener;
import org.apache.commons.net.ftp.FTPClient;
import org.apache.commons.net.ftp.FTPReply;

public class FTP {

	static String server;
	static int port;
	static String user;
	static String password;
	static FTPClient ftp;

	static void open() throws IOException {
		ftp = new FTPClient();

		ftp.addProtocolCommandListener(new PrintCommandListener(new PrintWriter(System.out)));

		ftp.connect(server, port);
		int reply = ftp.getReplyCode();

		if (!FTPReply.isPositiveCompletion(reply)) {
			ftp.disconnect();
			System.err.println("[FTP] can not connect to ftp Server");

		}

		ftp.login(user, password);

		ftp.enterLocalPassiveMode();

	}

	static void close() throws IOException {

		ftp.disconnect();

	}

	static void upload(File file, String path) throws FileNotFoundException, IOException {

		ftp.storeFile(path, new FileInputStream(file));

	}

}

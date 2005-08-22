/**
 * Copyright (c) 2005 Domain Language, Inc. (http://domainlanguage.com) This
 * free software is distributed under the "MIT" licence. See file licence.txt.
 * For more information, see http://timeandmoney.sourceforge.net.
 */
package com.domainlanguage.timeutil;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketException;

/**
 * This class is to mimic the behavior of the National Institute of Standards
 * and Technology Internet time server. So, we don't have to access the internet
 * during our tests.
 * 
 */
public class NISTServerStandIn {
    private ServerSocket socket;
    private Thread processingThread;
    private boolean keepProcessing;

    private static String CANNED_RESPONSE = "\n53604 05-08-22 02:57:53 50 0 0 725.6 UTC(NIST) * \n";

    public NISTServerStandIn() throws IOException {
        super();
        ServerSocket newSocket = new ServerSocket();
        newSocket.bind(new InetSocketAddress(InetAddress.getLocalHost()
                .getHostName(), 0));
        setSocket(newSocket);
    }

    public void start() {
        Thread newThread = new Thread(getServerConnectionProcessor(),
                getClass().getSimpleName() + " processing thread");
        setProcessingThread(newThread);
        setKeepProcessing(true);
        newThread.start();
    }

    public void stop() throws IOException {
        getSocket().close();
        setKeepProcessing(false);
        try {
            getProcessingThread().join();
        } catch (InterruptedException ex) {
            // ignore it
        }
    }

    public String getHostName() {
        return getSocket().getInetAddress().getHostName();
    }

    public String getPort() {
        return String.valueOf(getSocket().getLocalPort());
    }

    protected Runnable getServerConnectionProcessor() {
        return new Runnable() {
            public void run() {
                try {
                    while (getKeepProcessing()) {
                        Socket client = getSocket().accept();
                        serveTimeOn(client);
                    }
                } catch (SocketException ex) {
                    // ignore it, we'll get it during the socket close
                } catch (Exception ex) {
                    throw new RuntimeException(ex);
                }
            }
        };
    }

    protected void serveTimeOn(Socket client) throws IOException {
        OutputStreamWriter writer = new OutputStreamWriter(client
                .getOutputStream());
        try {
            writer.write(CANNED_RESPONSE);
        } finally {
            writer.close();
        }
    }

    protected ServerSocket getSocket() {
        return socket;
    }

    protected void setSocket(ServerSocket socket) {
        this.socket = socket;
    }

    protected synchronized boolean getKeepProcessing() {
        return keepProcessing;
    }

    protected synchronized void setKeepProcessing(boolean keepProcessing) {
        this.keepProcessing = keepProcessing;
    }

    protected Thread getProcessingThread() {
        return processingThread;
    }

    protected void setProcessingThread(Thread processingThread) {
        this.processingThread = processingThread;
    }
}

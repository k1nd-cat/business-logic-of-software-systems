package io.blss.lab1.robokassa.jca;

import io.blss.lab1.robokassa.RobokassaClient;
import jakarta.resource.ResourceException;
import jakarta.resource.spi.ConnectionManager;
import jakarta.resource.spi.ConnectionRequestInfo;
import jakarta.resource.spi.ManagedConnection;
import jakarta.resource.spi.ManagedConnectionFactory;

import javax.security.auth.Subject;
import java.io.PrintWriter;
import java.util.Set;

public class RobokassaManagedConnectionFactory implements ManagedConnectionFactory {

    private RobokassaClient client;

    public RobokassaManagedConnectionFactory(RobokassaClient client) {
        this.client = client;
    }

    @Override
    public Object createConnectionFactory(ConnectionManager cxManager) throws ResourceException {
        return new RobokassaConnectionFactoryImpl(client);
    }

    @Override
    public Object createConnectionFactory() {
        return new RobokassaConnectionFactoryImpl(client);
    }

    @Override
    public jakarta.resource.spi.ManagedConnection createManagedConnection(javax.security.auth.Subject subject,
                                                                          jakarta.resource.spi.ConnectionRequestInfo cxRequestInfo) throws ResourceException {
        return null;
    }

    @Override
    public ManagedConnection matchManagedConnections(Set set, Subject subject, ConnectionRequestInfo connectionRequestInfo) throws ResourceException {
        return null;
    }

    @Override
    public void setLogWriter(PrintWriter out) {
    }

    @Override
    public PrintWriter getLogWriter() {
        return null;
    }

    @Override
    public boolean equals(Object other) {
        return this == other;
    }

    @Override
    public int hashCode() {
        return super.hashCode();
    }
}
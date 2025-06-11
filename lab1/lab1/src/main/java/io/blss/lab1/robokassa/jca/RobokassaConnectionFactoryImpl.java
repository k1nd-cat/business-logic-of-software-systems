package io.blss.lab1.robokassa.jca;

import io.blss.lab1.robokassa.RobokassaClient;
import jakarta.resource.ResourceException;
import jakarta.resource.cci.Connection;
import jakarta.resource.cci.ConnectionSpec;
import jakarta.resource.cci.RecordFactory;
import jakarta.resource.cci.ResourceAdapterMetaData;

import javax.naming.NamingException;
import javax.naming.Reference;

public class RobokassaConnectionFactoryImpl implements RobokassaConnectionFactory {
    private final RobokassaClient client;

    public RobokassaConnectionFactoryImpl(RobokassaClient client) {
        this.client = client;
    }

    @Override
    public RobokassaConnection getConnection() {
        return new RobokassaConnectionImpl(client);
    }

    @Override
    public Connection getConnection(ConnectionSpec connectionSpec) throws ResourceException {
        return getConnection();
    }

    @Override
    public RecordFactory getRecordFactory() throws ResourceException {
        return null;
    }

    @Override
    public ResourceAdapterMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    public void setReference(Reference reference) {

    }

    @Override
    public Reference getReference() throws NamingException {
        return null;
    }
}

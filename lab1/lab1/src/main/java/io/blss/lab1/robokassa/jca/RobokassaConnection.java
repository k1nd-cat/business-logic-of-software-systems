package io.blss.lab1.robokassa.jca;

import io.blss.lab1.robokassa.Payment;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.*;

import java.util.Map;

public interface RobokassaConnection extends Connection, AutoCloseable {
    String buildSubmitUrl(Payment payment) throws Exception;
    String checkPaymentUrl(Map<String, String> parameters) throws Exception;

    @Override
    default Interaction createInteraction() throws ResourceException {
        return null;
    }

    @Override
    default LocalTransaction getLocalTransaction() throws ResourceException {
        return null;
    }

    @Override
    default ConnectionMetaData getMetaData() throws ResourceException {
        return null;
    }

    @Override
    default ResultSetInfo getResultSetInfo() throws ResourceException {
        return null;
    }

    @Override
    default void close() throws ResourceException {

    }
}
package io.blss.lab1.robokassa.jca;

import io.blss.lab1.robokassa.Payment;
import io.blss.lab1.robokassa.RobokassaClient;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.*;
import java.util.Map;

public class RobokassaConnectionImpl implements RobokassaConnection {
    private final RobokassaClient client;

    public RobokassaConnectionImpl(RobokassaClient client) {
        this.client = client;
    }

    @Override
    public String buildSubmitUrl(Payment payment) throws Exception {
        return client.buildSubmitUrl(payment);
    }

    @Override
    public String checkPaymentUrl(Map<String, String> parameters) throws Exception {
        return client.checkPaymentUrl(
                parameters,
                client.getClass().getDeclaredField("checkPassword").get(client).toString()
        );
    }

    @Override
    public Interaction createInteraction() {
        return null;
    }

    @Override
    public LocalTransaction getLocalTransaction() {
        return null;
    }

    @Override
    public ConnectionMetaData getMetaData() {
        return null;
    }

    @Override
    public ResultSetInfo getResultSetInfo() {
        return null;
    }

    @Override
    public void close() {
    }
}
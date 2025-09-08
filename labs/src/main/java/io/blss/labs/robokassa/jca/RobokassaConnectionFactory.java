package io.blss.labs.robokassa.jca;

import jakarta.resource.ResourceException;
import jakarta.resource.cci.ConnectionFactory;

public interface RobokassaConnectionFactory extends ConnectionFactory {
    RobokassaConnection getConnection() throws ResourceException;
}

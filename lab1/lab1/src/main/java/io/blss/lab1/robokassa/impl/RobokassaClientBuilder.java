package io.blss.lab1.robokassa.impl;

import io.blss.lab1.robokassa.RobokassaClient;
import lombok.experimental.Accessors;


@Accessors(chain = true, fluent = true)
public class RobokassaClientBuilder {

    private String merchantLogin;

    private String merchantPassword;

    private String checkPassword;

    public RobokassaClient build() {
        return new RobokassaClientImpl(merchantLogin, merchantPassword, checkPassword);
    }

    public RobokassaClientBuilder merchantLogin(String merchantLogin) {
        this.merchantLogin = merchantLogin;
        return this;
    }

    public RobokassaClientBuilder merchantPassword(String merchantPassword) {
        this.merchantPassword = merchantPassword;
        return this;
    }

    public RobokassaClientBuilder checkPassword(String checkPassword) {
        this.checkPassword = checkPassword;
        return this;
    }

}
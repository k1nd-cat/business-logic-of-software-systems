package io.blss.lab1.robokassa;

import java.security.NoSuchAlgorithmException;
import java.util.Map;

public interface RobokassaClient {

    String getSignature(Payment payment, String merchantLogin, String merchantPassword) throws NoSuchAlgorithmException;

    String md5(String input) throws NoSuchAlgorithmException;

    String buildSubmitUrl(Payment payment) throws Exception;

    String buildSubmitUrl(Payment payment, String merchantLogin) throws Exception;

    String buildSubmitUrl(Payment payment, String merchantLogin, String merchantPassword, boolean isTest) throws Exception;

    String checkPaymentUrl(Map<String, String> parameters, String checkPassword) throws NoSuchAlgorithmException;

    String[] getCommentDataDeserialized(String commentData) throws Exception;

    Map<Integer, String> getCommentDataMap(String commentData) throws Exception;
}
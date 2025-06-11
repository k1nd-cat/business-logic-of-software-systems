package io.blss.lab1.robokassa.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import io.blss.lab1.robokassa.Payment;
import io.blss.lab1.robokassa.RobokassaClient;

import java.math.BigInteger;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.HashMap;
import java.util.Map;

public class RobokassaClientImpl implements RobokassaClient {

    // parameters: merchant login, out sum, invoice id, invoice description, signature crc, isTest
    public static final String urlPattern = "https://auth.robokassa.ru/Merchant/Index.aspx?MrchLogin=%s&OutSum=%s&InvId=%s&Desc=%s&SignatureValue=%s&isTest=%d";

    private String merchantLogin;

    private String merchantPassword;

    private String checkPassword;

    private ObjectMapper objectMapper = new ObjectMapper();

    public RobokassaClientImpl(String merchantLogin, String merchantPassword, String checkPassword) {
        this.merchantLogin = merchantLogin;
        this.merchantPassword = merchantPassword;
        this.checkPassword = checkPassword;
    }

    private String getSignature(Payment payment) throws NoSuchAlgorithmException {

        return this.getSignature(payment, this.merchantLogin, this.merchantPassword);
    }

    public String getSignature(Payment payment, String merchantLogin, String merchantPassword) throws NoSuchAlgorithmException {
        String crcHashString = "";

        String crcString = String.format("%s:%s:%s:%s",
                merchantLogin, String.valueOf(payment.getAmount()), payment.getInvoiceRef(), merchantPassword);
        crcHashString = this.md5(crcString);

        return crcHashString;
    }

    public String md5(String input) throws NoSuchAlgorithmException {
        String result = input;
        if(input != null) {
            MessageDigest md = MessageDigest.getInstance("MD5"); //or "SHA-1"
            md.update(input.getBytes());
            BigInteger hash = new BigInteger(1, md.digest());
            result = hash.toString(16);
            while(result.length() < 32) { //40 for SHA-1
                result = "0" + result;
            }
        }
        return result;
    }

    public String buildSubmitUrl(Payment payment) throws Exception {
        return buildSubmitUrl(payment, this.merchantLogin, this.merchantPassword, true);
    }

    public String buildSubmitUrl(Payment payment, String merchantLogin) throws Exception {
        return buildSubmitUrl(payment, merchantLogin, this.merchantPassword, true);
    }

    public String buildSubmitUrl(Payment payment, String merchantLogin, String merchantPassword, boolean isTest) throws Exception {
        String commentEncoded = "";

        commentEncoded = URLEncoder.encode(payment.getComment(), "UTF-8");

        return String.format(urlPattern, merchantLogin, payment.getAmount(), payment.getInvoiceRef(), commentEncoded, this.getSignature(payment, merchantLogin, merchantPassword), isTest ? 1 : 0);
    }

    public String checkPaymentUrl(Map<String, String> parameters, String checkPassword) throws NoSuchAlgorithmException {
        ObjectNode data = objectMapper.createObjectNode();
        data.put("OutSum", parameters.get("outSumm"));
        data.put("Shp_item", parameters.get("shpItem"));
        data.put("invoiceRef", parameters.get("invoiceRef"));
        data.put("crc", parameters.get("crc"));

        String myCrc = this.md5(String.format("%s:%s:%s:Shp_item=%s", parameters.get("outSumm"), parameters.get("invoiceRef"), checkPassword, parameters.get("shpItem")));

        if (! myCrc.equals(parameters.get("crc"))) {
            return "bad sign\n";
        } else {
            Map<String, String> renderData = new HashMap<String, String>() {{
                put("invoiceRef", parameters.get("invoiceRef"));
                put("amount", String.valueOf(parameters.get("amount")));
            }};

            return String.format("OK%s\n");
        }
    }

    public String[] getCommentDataDeserialized(String commentData) throws Exception {

        String[] commentDeserialized = objectMapper.readValue(commentData, String[].class);

        return commentDeserialized;
    }

    public Map<Integer, String> getCommentDataMap(String commentData) throws Exception {
        String[] commentDeserialized = this.getCommentDataDeserialized(commentData);
        Map<Integer, String> commentDataMap = new HashMap<Integer, String>();
        int i = 0;
        for (String commentDataEntry : commentDeserialized) {
            commentDataMap.put(i++, commentDataEntry);
        }
        return commentDataMap;
    }

}
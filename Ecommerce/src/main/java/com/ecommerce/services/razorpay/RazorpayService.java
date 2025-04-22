package com.ecommerce.services.razorpay;

import java.util.HashMap;
import java.util.Map;
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import org.json.JSONObject;
import org.springframework.stereotype.Service;
import com.razorpay.RazorpayClient;

@Service
public class RazorpayService {
	
	private final RazorpayClient razorpayClient;
	private static final String HMAC_SHA256 = "HmacSHA256";
	private static final String WEBHOOK_SECRET = "secureWebhookKey$98";

	
    public RazorpayService(RazorpayClient razorpayClient) {
		super();
		this.razorpayClient = razorpayClient;
	}


    public Map<String, String> createPaymentLink(double amountInRupees, String customerName, String customerEmail) throws Exception {
        JSONObject paymentLinkRequest = new JSONObject();
        paymentLinkRequest.put("amount", (int) (amountInRupees * 100));
        paymentLinkRequest.put("currency", "INR");
        paymentLinkRequest.put("description", "Confirming the Order of customer: " + customerName);

        JSONObject customer = new JSONObject();
        customer.put("name", customerName);
        customer.put("email", customerEmail);
        paymentLinkRequest.put("customer", customer);

        JSONObject notify = new JSONObject();
        notify.put("email", true);
        paymentLinkRequest.put("notify", notify);
        //paymentLinkRequest.put("reference_id","will be updated soon");
        long currentTimestamp = System.currentTimeMillis() / 1000;
        long expiryTimestamp = currentTimestamp + (20 * 60);
        paymentLinkRequest.put("expire_by", expiryTimestamp);
        com.razorpay.PaymentLink paymentLink = razorpayClient.paymentLink.create(paymentLinkRequest);
        String paymentLinkId = paymentLink.get("id");
        String shortUrl = paymentLink.get("short_url");

        Map<String, String> response = new HashMap<>();
        response.put("payment_link_id", paymentLinkId);
        response.put("short_url", shortUrl);
        return response;
    }
    
    public boolean verifyRazorpayWebhookSignature(String payloadBody, String providedSignature) {
        try {
            String generatedSignature = generateHmacSha256Hex(payloadBody);
            return generatedSignature.equals(providedSignature);
        } catch (Exception e) {
            throw new RuntimeException("Failed to verify Razorpay signature: " + e.getMessage(), e);
        }
    }

    private String generateHmacSha256Hex(String data) throws Exception {
        Mac sha256_HMAC = Mac.getInstance(HMAC_SHA256);
        SecretKeySpec secretKeySpec = new SecretKeySpec(WEBHOOK_SECRET.getBytes(), HMAC_SHA256);
        sha256_HMAC.init(secretKeySpec);
        byte[] hash = sha256_HMAC.doFinal(data.getBytes());
        return bytesToHex(hash);
    }

    private String bytesToHex(byte[] bytes) {
        StringBuilder result = new StringBuilder();
        for (byte b : bytes) {
            result.append(String.format("%02x", b));
        }
        return result.toString();
    }
}

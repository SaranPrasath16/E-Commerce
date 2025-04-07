package com.ecommerce.services.otp;

import java.time.LocalDateTime;
import java.util.Map;
import java.util.Random;
import java.util.concurrent.ConcurrentHashMap;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import com.ecommerce.dto.TempUserDTO;

@Service
public class OtpService {
	private final Map<String, Integer> otpStorage = new ConcurrentHashMap<>();
    private final Map<String, TempUserDTO> tempUserStorage = new ConcurrentHashMap<>();
    private final long OTP_EXPIRATION_MINUTES = 5;

    @Autowired
    private JavaMailSender mailSender;

    public int generateOtp(String email, TempUserDTO tempUser){
        String otp1 = String.format("%06d", new Random().nextInt(999999));
        int otp= Integer.parseInt(otp1);
        otpStorage.put(email, otp);
        tempUserStorage.put(email, tempUser);
        return otp;
    }

    public void sendOtpByEmail(String email, int otp, String userName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("E-Commerce <saranvenkatesan1620@gmail.com>");
        message.setSubject("Your One-Time Password (OTP) for Verification");
        String emailBody = "Dear "+ userName +",\n\n"
                + "Your One-Time Password (OTP) for verification is: "+ otp +"\n\n"
                + "This OTP is valid for " + OTP_EXPIRATION_MINUTES + " minutes. "
                + "Please do not share this OTP with anyone for security reasons.\n\n"
                + "If you did not request this OTP, please ignore this email.\n\n"
                + "Best regards,\n"
                + "E-Commerce Team";

        message.setText(emailBody);
        mailSender.send(message);
    }

    public boolean verifyOtp(String email, int otp){
        if (!otpStorage.containsKey(email) ||!(otp ==(otpStorage.get(email))) || isOtpExpired(email)) {
            return false;
        }
        otpStorage.remove(email);
        return true;
    }

    private boolean isOtpExpired(String email){
    	TempUserDTO tempUser = tempUserStorage.get(email);
        return tempUser.getOtpGeneratedTime().plusMinutes(OTP_EXPIRATION_MINUTES).isBefore(LocalDateTime.now());
    }

    public TempUserDTO getTempUserDetails(String email) {
        return tempUserStorage.remove(email);
        
    }
}

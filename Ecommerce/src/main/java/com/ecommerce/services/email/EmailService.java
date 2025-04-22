package com.ecommerce.services.email;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
	
    private final long OTP_EXPIRATION_MINUTES = 5;
	
    @Autowired
    private JavaMailSender mailSender;
	
    public void sendOtpByEmail(String email, int otp, String userName){
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("Your One-Time Password (OTP) for Verification");
        String emailBody = "Dear "+ userName +",\n\n"
                + "Your One-Time Password (OTP) for verification is: "+ otp +"\n\n"
                + "This OTP is valid for " + OTP_EXPIRATION_MINUTES + " minutes. "
                + "Please do not share this OTP with anyone for security reasons.\n\n"
                + "If you did not request this OTP, please ignore this email.\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void sendPaymentLink(String email, String userName, String paymentLink) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("Complete Your Payment for Your Order");

        String emailBody = "Dear " + userName + ",\n\n"
                + "Thank you for shopping with us!\n\n"
                + "To complete your purchase, please click the payment link below:\n"
                + paymentLink + "\n\n"
                + "Please complete the payment within 30 minutes to avoid order cancellation.\n"
                + "Do not share this payment link with anyone for security reasons.\n\n"
                + "If you did not place this order, please ignore this email.\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }
    
    public void sendAccountCreatedEmail(String email, String userName) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(email);
        message.setFrom("QuickPikk <quickpikk1@gmail.com>");
        message.setSubject("Welcome to QuickPikk - Your Account Is Ready!");

        String emailBody = "Hi " + userName + ",\n\n"
                + "Thank you for signing up at QuickPikk! 🛍️\n\n"
                + "Your account has been successfully created, and you're now ready to explore our latest collections.\n\n"
                + "Here's what you can do next:\n"
                + "- Browse new arrivals\n"
                + "- Track your orders\n"
                + "- Save your favorites to your wishlist\n"
                + "- Enjoy exclusive offers and deals\n\n"
                + "We’re excited to have you with us!\n"
                + "Happy shopping! 🛒\n\n"
                + "Best regards,\n"
                + "QuickPikk Team";

        message.setText(emailBody);
        mailSender.send(message);
    }



}

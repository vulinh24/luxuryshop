package com.luxuryshop.services;

import com.luxuryshop.entities.Order;
import com.luxuryshop.entities.OrderProduct;
import com.luxuryshop.repositories.DetailOrderRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.text.DecimalFormat;

import static com.luxuryshop.WebConstants.*;

@Service
public class SendMailService {
    @Autowired
    JavaMailSender sender;

    @Autowired
    DetailOrderRepository orderRepository;

    public void sendMailConfirmOrder(Order order) {
//        Order order = orderRepository.getById(id);
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = ORDER_EMAIL_1.replace("[ID]", order.getId().toString());
        for (OrderProduct product : order.getSaledOrder()) {
            DecimalFormat df = new DecimalFormat("###,###,###");
            String price = df.format(product.getProductPrice());
            String item = ORDER_EMAIL_2.replace("[TITLE]", product.getProductTitle());
            item = item.replace("[PRICE]", price);
            item = item.replace("[SL]", product.getQuantity().toString());
            htmlMsg = htmlMsg + item;
        }
        DecimalFormat df = new DecimalFormat("###,###,###");
        String total = df.format(order.getTotal());
        String end = ORDER_EMAIL_3.replace("[TOTAL]", total);
        end = end.replace("[ADDRESS]", order.getCustomerName() + "," + order.getCustomerAddress());
        end = end.replace("[PHONE]", order.getCustomerPhone());
        htmlMsg = htmlMsg + end;
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(order.getCustomerEmail());
            helper.setSubject("Luxury Shop - Confirm Order");
            helper.setFrom(MY_MAIL);
            sender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }

    public void sendMailResetPassword(String password, String mail) {
        MimeMessage mimeMessage = sender.createMimeMessage();
        MimeMessageHelper helper = new MimeMessageHelper(mimeMessage, "utf-8");
        String htmlMsg = "<h3>Mật khẩu đã được đổi thành công. Mật khẩu mới của bạn là: </h3>" + password;
        try {
            helper.setText(htmlMsg, true); // Use this or above line.
            helper.setTo(mail);
            helper.setSubject("Luxury Shop - Reset Password");
            helper.setFrom(MY_MAIL);
            sender.send(mimeMessage);
        } catch (MessagingException e) {
            throw new RuntimeException(e);
        }
    }
}

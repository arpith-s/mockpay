package org.arpith.mockpay.notificationservice.service.impl;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import lombok.RequiredArgsConstructor;
import org.arpith.mockpay.notificationservice.constant.Constant;
import org.arpith.mockpay.notificationservice.constant.DataFieldKeys;
import org.arpith.mockpay.notificationservice.constant.Topic;
import org.arpith.mockpay.notificationservice.enumeration.Status;
import org.arpith.mockpay.notificationservice.service.NotificationService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class NotificationServiceImpl implements NotificationService {
    private static final Logger LOG = LoggerFactory.getLogger(NotificationServiceImpl.class);
    private final SimpleMailMessage simpleMailMessage;
    private final JavaMailSender javaMailSender;
    private final Gson gson;

    @Value("${application.notification.mail.username}")
    String mailUsername;

    @Value("${application.notification.mail.subject}")
    String mailSubject;

    @Value("${application.notification.smallest-denomination-factor}")
    Integer smallestDenominationFactor;

    @Override
    @KafkaListener(topics = Topic.TRANSACTION_COMPLETED, groupId = Constant.CONSUMER_GROUP_ID)
    public void notifyTransactionCompleted(String message) {
        try {
            LOG.info("Received transaction completed message: {}", message);
            Map<String, Object> transactionMap = gson.fromJson(message, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            processTransactionCompleted(transactionMap);
        } catch (Exception e) {
            LOG.error("Error processing transaction completed message: {}", message, e);
        }
    }

    private void processTransactionCompleted(Map<String, Object> transactionMap) {
        String transactionStatus = (String) transactionMap.get(DataFieldKeys.STATUS);
        String transactionId = (String) transactionMap.get(DataFieldKeys.TRANSACTION_ID);
        Long amount = ((Double) transactionMap.get(DataFieldKeys.AMOUNT)).longValue() / smallestDenominationFactor;
        String senderEmail = (String) transactionMap.get(DataFieldKeys.SENDER_ID);
        String receiverEmail = (String) transactionMap.get(DataFieldKeys.RECEIVER_ID);

        sendMail(senderEmail, buildSenderMessage(transactionStatus, amount, transactionId));
        if (Status.SUCCESSFUL.name().equals(transactionStatus)) {
            sendMail(receiverEmail, buildReceiverMessage(amount, senderEmail));
        }
    }

    @Override
    @KafkaListener(topics = Topic.WALLET_CREATED, groupId = Constant.CONSUMER_GROUP_ID)
    public void notifyWalletCreated(String message) {
        try {
            LOG.info("Received wallet created message: {}", message);
            Map<String, Object> walletCreatedMap = gson.fromJson(message, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            processWalletCreated(walletCreatedMap);
        } catch (Exception e) {
            LOG.error("Error processing wallet created message: {}", message, e);
        }
    }

    private void processWalletCreated(Map<String, Object> walletCreatedMap) {
        String name = (String) walletCreatedMap.get(DataFieldKeys.NAME);
        String email = (String) walletCreatedMap.get(DataFieldKeys.EMAIL);
        String status = (String) walletCreatedMap.get(DataFieldKeys.STATUS);
        String mailContent = "Hi " + name + ", Your account " + (Status.SUCCESSFUL.name().equals(status) ? "has been setup successfully" : "setup has failed");
        sendMail(email, mailContent);
    }

    @Override
    @KafkaListener(topics = Topic.WALLET_DELETED, groupId = Constant.CONSUMER_GROUP_ID)
    public void notifyWalletDeleted(String message) {
        try {
            LOG.info("Received wallet deleted message: {}", message);
            Map<String, Object> walletDeletedMap = gson.fromJson(message, new TypeToken<HashMap<String, Object>>() {
            }.getType());
            processWalletDeleted(walletDeletedMap);
        } catch (Exception e) {
            LOG.error("Error processing wallet deleted message: {}", message, e);
        }
    }

    private void processWalletDeleted(Map<String, Object> walletDeletedMap) {
        String name = (String) walletDeletedMap.get(DataFieldKeys.NAME);
        String email = (String) walletDeletedMap.get(DataFieldKeys.EMAIL);
        String status = (String) walletDeletedMap.get(DataFieldKeys.STATUS);
        String mailContent = "Hi " + name + ", Your account " + (Status.SUCCESSFUL.name().equals(status) ? "has been deleted successfully" : "deletion has failed");
        sendMail(email, mailContent);
    }

    private String buildSenderMessage(String transactionStatus, Long amount, String transactionId) {
        return transactionStatus.equals(Status.SUCCESSFUL.name()) ?
                "Your account has been debited with amount " + amount + ", transaction id = " + transactionId :
                "Your transaction of amount " + amount + ", transaction id = " + transactionId + " has failed!";
    }

    private String buildReceiverMessage(Long amount, String senderEmail) {
        return "Your account has been credited with amount " + amount + " for the transaction done by user " + senderEmail;
    }

    private void sendMail(String toMail, String message) {
        try {
            simpleMailMessage.setTo(toMail);
            simpleMailMessage.setSubject(mailSubject);
            simpleMailMessage.setFrom(mailUsername);
            simpleMailMessage.setText(message);
            javaMailSender.send(simpleMailMessage);
            LOG.info("Sent email to {}", toMail);
        } catch (Exception e) {
            LOG.error("Error sending email to {}", toMail, e);
        }
    }
}

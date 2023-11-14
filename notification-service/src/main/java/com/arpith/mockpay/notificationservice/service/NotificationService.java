package org.arpith.mockpay.notificationservice.service;

public interface NotificationService {
    void notifyTransactionCompleted(String message);

    void notifyWalletCreated(String message);

    void notifyWalletDeleted(String message);
}

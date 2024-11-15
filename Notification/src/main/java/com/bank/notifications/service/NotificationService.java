package com.bank.notifications.service;

import com.bank.notifications.configs.ActiveMq;
import com.bank.notifications.dto.WithdrawFunds;
import jakarta.jms.Message;
import jakarta.jms.MessageListener;
import jakarta.jms.ObjectMessage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.jms.annotation.JmsListener;
import org.springframework.jms.core.JmsTemplate;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;

import java.io.Serializable;

@Service
public class NotificationService {
    final JmsTemplate jmsTemplate;
    final ActiveMq activeMq;

    public NotificationService(JmsTemplate jmsTemplate, ActiveMq activeMq) {
        this.jmsTemplate = jmsTemplate;
        this.activeMq = activeMq;
    }

}
@Component
class WithdrawalListener implements MessageListener {
    static final Logger LOGGER = LoggerFactory.getLogger(WithdrawalListener.class);
    @Override
    @JmsListener(destination ="${active-mq.withdrawals}" )
    public void onMessage(Message message) {
        try{
            LOGGER.info("Received withdrawal notification");
            ObjectMessage objectMessage = (ObjectMessage)message;
            Serializable withdrawFunds = objectMessage.getObject();
            //do additional processing
            LOGGER.info("Received Message: {}", withdrawFunds.toString());
        } catch(Exception e) {
            LOGGER.error("Received Exception : {}", e.getMessage());
        }
    }
}

@Component
class CashDepositListener implements MessageListener {
    static final Logger LOGGER = LoggerFactory.getLogger(CashDepositListener.class);
    @Override
    @JmsListener(destination ="${active-mq.cash-deposit}" )
    public void onMessage(Message message) {
        try{
            LOGGER.info("Received Message: {}", message.toString());
            ObjectMessage objectMessage = (ObjectMessage)message;
            Serializable cashDeposit = objectMessage.getObject();
            //do additional processing
            LOGGER.info("Received Message: {}", cashDeposit.toString());
        } catch(Exception e) {
            LOGGER.error("Received Exception : {}", e.getMessage());
        }
    }
}

@Component
class FundsTransfer implements MessageListener {
    static final Logger LOGGER = LoggerFactory.getLogger(FundsTransfer.class);
    @Override
    @JmsListener(destination ="${active-mq.funds-transfer}" )
    public void onMessage(Message message) {
        try{
            LOGGER.info("Received Message: {}", message.toString());
            ObjectMessage objectMessage = (ObjectMessage)message;
            Serializable fundsTransfer = objectMessage.getObject();
            //do additional processing
            LOGGER.info("Received Message: {}", fundsTransfer.toString());
        } catch(Exception e) {
            LOGGER.error("Received Exception : {}", e.getMessage());
        }
    }
}

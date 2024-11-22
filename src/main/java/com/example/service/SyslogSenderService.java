package com.example.service;

import org.springframework.stereotype.Service;

import com.cloudbees.syslog.Facility;
import com.cloudbees.syslog.MessageFormat;
import com.cloudbees.syslog.Severity;
import com.cloudbees.syslog.sender.TcpSyslogMessageSender;

@Service
public class SyslogSenderService {

    private final TcpSyslogMessageSender messageSender;

    public SyslogSenderService() {
        // Initialize the TcpSyslogMessageSender
        messageSender = new TcpSyslogMessageSender();
        messageSender.setDefaultMessageHostname("myhostname");
        messageSender.setDefaultAppName("myapp");
        messageSender.setDefaultFacility(Facility.USER);
        messageSender.setDefaultSeverity(Severity.INFORMATIONAL);
        messageSender.setSyslogServerHostname("0.0.0.0");
        messageSender.setSyslogServerPort(514);
        messageSender.setMessageFormat(MessageFormat.RFC_5424);
        messageSender.setSsl(false); // Enable SSL if needed
    }

    public void sendTestSyslogMessage() {
        // Send a test message to the syslog server
        try {
            messageSender.sendMessage("This is a test message sent from Spring Boot!");
            System.out.println("Test syslog message sent.");
        } catch (Exception e) {
            e.printStackTrace();
            System.err.println("Failed to send syslog message: " + e.getMessage());
        }
    }
}
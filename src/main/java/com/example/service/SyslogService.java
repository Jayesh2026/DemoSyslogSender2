package com.example.service;

import java.io.IOException;
import java.io.PrintWriter;
import java.net.InetAddress;
import java.net.Socket;
import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class SyslogService {

    private static final Logger logger = LoggerFactory.getLogger(SyslogService.class);

    @Value("${syslog.host:0.0.0.0}")
    private String syslogHost;

    @Value("${syslog.port:514}")
    private int syslogPort;

    @Value("${spring.application.name:default-app-name}") // Fetching the app name from application.properties
    private String appName;

    private static final String VERSION = "1";
    private final String hostname;
    private final DateTimeFormatter timestampFormat;
    private final String processId;

    // Syslog facility levels
    public static final int FACILITY_USER = 1;

    // Syslog severity levels
    public static final int SEVERITY_ERROR = 3;
    public static final int SEVERITY_WARNING = 4;
    public static final int SEVERITY_NOTICE = 5;
    public static final int SEVERITY_INFO = 6;
    public static final int SEVERITY_DEBUG = 7;

    // Constructor to initialize Syslog service parameters
    public SyslogService() throws IOException {
        this.hostname = InetAddress.getLocalHost().getHostName();
        this.timestampFormat = DateTimeFormatter
                .ofPattern("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")
                .withZone(ZoneId.systemDefault());
        
        // Get the current process ID
        this.processId = String.valueOf(ProcessHandle.current().pid());
    }

    // Method to format structured data in a syslog message
    private String formatStructuredData(Map<String, String> data) {
        if (data == null || data.isEmpty()) {
            return "-";
        }

        StringBuilder sb = new StringBuilder();
        sb.append("[meta@47450 ");

        for (Map.Entry<String, String> entry : data.entrySet()) {
            String value = entry.getValue()
                    .replace("\"", "\\\"")
                    .replace("]", "\\]")
                    .replace("\\", "\\\\");
            sb.append(entry.getKey()).append("=\"").append(value).append("\" ");
        }

        sb.setLength(sb.length() - 1);  // Remove trailing space
        sb.append("]");
        return sb.toString();
    }

    // Main method to send a syslog message with different parameters
    public void sendMessage(int facility, int severity, String procId, String msgId,
                            Map<String, String> structuredData, String message) {
        int priority = (facility * 8) + severity;
        String timestamp = timestampFormat.format(Instant.now());
        String sdElement = formatStructuredData(structuredData);

        // Use the injected appName
        String syslogMessage = String.format("<%d>%s %s %s %s %s %s %s %s",
                priority,
                VERSION,
                timestamp,
                hostname,
                appName,  // Using the app name injected from application.properties
                // (procId != null ? procId : "-"),
                processId,
                (msgId != null ? msgId : "-"),
                sdElement,
                message);

        logger.debug("Sending syslog message: {}", syslogMessage);

        // Using try-with-resources to handle socket and writer properly
        try (Socket socket = new Socket(syslogHost, syslogPort);
            PrintWriter writer = new PrintWriter(socket.getOutputStream(), true)) {
            writer.println(syslogMessage);
        } catch (IOException e) {
            logger.error("Failed to send syslog message: {}", e.getMessage(), e);
        }
    }

    // Convenience methods for sending syslog messages with different severity levels

    public void info(String msgId, String message) {
        sendMessage(FACILITY_USER, SEVERITY_INFO, processId, msgId, null, message);
    }

    public void info(String msgId, Map<String, String> structuredData, String message) {
        sendMessage(FACILITY_USER, SEVERITY_INFO, processId, msgId, structuredData, message);
    }

    public void error(String msgId, String message) {
        sendMessage(FACILITY_USER, SEVERITY_ERROR, processId, msgId, null, message);
    }

    public void error(String msgId, Map<String, String> structuredData, String message) {
        sendMessage(FACILITY_USER, SEVERITY_ERROR, processId, msgId, structuredData, message);
    }

    public void debug(String msgId, String message) {
        sendMessage(FACILITY_USER, SEVERITY_DEBUG, processId, msgId, null, message);
    }

    public void debug(String msgId, Map<String, String> structuredData, String message) {
        sendMessage(FACILITY_USER, SEVERITY_DEBUG, processId, msgId, structuredData, message);
    }

    public void warning(String msgId, String message) {
        sendMessage(FACILITY_USER, SEVERITY_WARNING, processId, msgId, null, message);
    }

    public void warning(String msgId, Map<String, String> structuredData, String message) {
        sendMessage(FACILITY_USER, SEVERITY_WARNING, processId, msgId, structuredData, message);
    }

    public void notice(String msgId, String message) {
        sendMessage(FACILITY_USER, SEVERITY_NOTICE, processId, msgId, null, message);
    }

    public void notice(String msgId, Map<String, String> structuredData, String message) {
        sendMessage(FACILITY_USER, SEVERITY_NOTICE, processId, msgId, structuredData, message);
    }
}

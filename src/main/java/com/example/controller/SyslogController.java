package com.example.controller;

import com.example.service.SyslogService;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@RestController
@RequestMapping("/syslog")
public class SyslogController {

    private static final Logger logger = LoggerFactory.getLogger(SyslogController.class);

    private final SyslogService syslogService;

    public SyslogController(SyslogService syslogService) {
        this.syslogService = syslogService;
    }

    // Endpoint to send an info log
    @PostMapping("/info")
    public ResponseEntity<String> sendInfoLog(@RequestParam String msgId, @RequestBody String message) {
        logger.info("Info log sent successfully.");;
        syslogService.info(msgId, message);
        return ResponseEntity.ok("Info log sent successfully.");
    }

    // Endpoint to send an error log
    @PostMapping("/error")
    public ResponseEntity<String> sendErrorLog(@RequestParam String msgId, @RequestBody String message) {
        syslogService.error(msgId, message);
        return ResponseEntity.ok("Error log sent successfully.");
    }

    // Endpoint to send a debug log
    @PostMapping("/debug")
    public ResponseEntity<String> sendDebugLog(@RequestParam String msgId, @RequestBody String message) {
        syslogService.debug(msgId, message);
        return ResponseEntity.ok("Debug log sent successfully.");
    }

    // Endpoint to send a warning log
    @PostMapping("/warning")
    public ResponseEntity<String> sendWarningLog(@RequestParam String msgId, @RequestBody String message) {
        syslogService.warning(msgId, message);
        return ResponseEntity.ok("Warning log sent successfully.");
    }

    // Endpoint to send a notice log
    @PostMapping("/notice")
    public ResponseEntity<String> sendNoticeLog(@RequestParam String msgId, @RequestBody String message) {
        syslogService.notice(msgId, message);
        return ResponseEntity.ok("Notice log sent successfully.");
    }

    // Endpoint to send a custom log with structured data
    @PostMapping("/custom")
    public ResponseEntity<String> sendCustomLog(@RequestParam String msgId, @RequestBody Map<String, String> structuredData,
                                                @RequestParam String message) {
        syslogService.sendMessage(1, 6, null, msgId, structuredData, message);
        return ResponseEntity.ok("Custom log sent successfully.");
    }
}

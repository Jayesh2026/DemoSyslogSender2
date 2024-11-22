package com.example;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.example.service.SyslogSenderService;

@SpringBootApplication
public class SysSenderApplication implements CommandLineRunner {

	private final SyslogSenderService syslogSenderService;


	public SysSenderApplication(SyslogSenderService syslogSenderService) {
        this.syslogSenderService = syslogSenderService;
    }

	public static void main(String[] args) {
		SpringApplication.run(SysSenderApplication.class, args);
	}

	
    @Override
    public void run(String... args) throws Exception {
        // Send a test syslog message on startup
        syslogSenderService.sendTestSyslogMessage();
    }
}

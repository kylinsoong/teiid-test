package org.springframework.integration.samples.ftp;

import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;

public class FtpInboundChannelAdapterSample {

    public static void main(String[] args) {

        ConfigurableApplicationContext ctx = new ClassPathXmlApplicationContext("FtpInboundChannelAdapterSample-context.xml");
    }

}

package com.example.connectionlimit.config;

import org.eclipse.jetty.server.ConnectionLimit;
import org.springframework.boot.web.embedded.jetty.JettyServerCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class JettyConfig {

    @Bean
    public JettyServerCustomizer serverConnectionLimit() {
        return server -> server.addBean(new ConnectionLimit(1, server));
    }

//    @Bean
//    public JettyServerCustomizer connectorsConnectionLimit() {
//        return server -> server.addBean(new ConnectionLimit(1, server.getConnectors()));
//    }
}

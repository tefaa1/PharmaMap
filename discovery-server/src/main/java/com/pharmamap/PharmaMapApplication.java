package com.pharmamap;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

@SpringBootApplication
@EnableEurekaServer
public class PharmaMapApplication {
    public static void main(String []args){
        SpringApplication.run(PharmaMapApplication.class,args);
    }
}

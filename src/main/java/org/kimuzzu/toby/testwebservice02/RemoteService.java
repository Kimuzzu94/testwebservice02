package org.kimuzzu.toby.testwebservice02;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.embedded.tomcat.TomcatReactiveWebServerFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@SpringBootApplication
public class RemoteService {
/*
    @Bean
    TomcatReactiveWebServerFactory tomcatReactiveWebServerFactory() {
        return new TomcatReactiveWebServerFactory();
    }
*/
    @RestController
    public static class MyController {
        @GetMapping("/service")
        public String service(String req) throws InterruptedException {
            //Log.info("service");법
            Thread.sleep(2000);
            return req + "/service1";
        }

        @GetMapping("/service2")
        public String service2(String req) throws InterruptedException {
            //Log.info("service");법
            Thread.sleep(2000);
            return req + "/service2";
        }
    }

    public static void main(String[] args) {
        //톰켓 기본설정 오버라이드
        System.setProperty("server.port", "8081");
        System.setProperty("server.tomcat.max-threads", "1000");
        SpringApplication.run(RemoteService.class, args);
    }
}

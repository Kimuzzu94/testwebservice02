package org.kimuzzu.toby.testwebservice02;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Service;
import org.springframework.util.concurrent.ListenableFuture;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.concurrent.CompletableFuture;

@SpringBootApplication
@RestController
@Slf4j
@EnableAsync
public class Testwebservice02Application {
    static final String url = "http://localhost:8081/service?req={req}";
    static final String url2 = "http://localhost:8081/service2?req={req}";
/*
    @Bean
    NettyReactiveWebServerFactory nettyReactiveWebServerFactory() {
        return new NettyReactiveWebServerFactory();
    }
*/
    @Autowired
    MyService myService;

    WebClient client = WebClient.create();

    @GetMapping("/rest")
    public Mono<String> rest(int idx) {
        return  client.get().uri(url, idx).exchange()
                .flatMap(c -> c.bodyToMono(String.class))
                .flatMap(c -> client.get().uri(url2, c).exchange())
                .flatMap(c -> c.bodyToMono(String.class))
                .doOnNext(c -> log.info(c.toString()))
                .flatMap(s -> Mono.fromCompletionStage(myService.work(s)))
                .doOnNext(c -> log.info(c.toString()));
    }

    @Service
    public static class MyService {

        @Async
        public CompletableFuture<String> work(String req) {
            return CompletableFuture.completedFuture(req + "/MyServiceSync");
        }
    }

    public static void main(String[] args) {
        System.setProperty("reactor.ipc.netty.workerCount", "1");
        System.setProperty("reactor.ipc.netty.pool.maxConnections", "2000");
        SpringApplication.run(Testwebservice02Application.class, args);
    }

}

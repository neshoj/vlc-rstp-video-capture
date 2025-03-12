package com.neshoj.vlcstreamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;


@SpringBootApplication
public class VLCStreamingApplication {
    public static void main(String[] args) {
        SpringApplication.run(VLCStreamingApplication.class, args);
        new NativeDiscovery().discover();
    }
}

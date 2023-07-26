package com.neshoj.vlcstreamer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import uk.co.caprica.vlcj.discovery.NativeDiscovery;

import javax.swing.*;

@SpringBootApplication
public class VlcstreamerApplication {
    private static final String NATIVE_LIBRARY_SEARCH_PATH = "C:\\Program Files\\VideoLAN\\VLC";

    public static void main(String[] args) {
        SpringApplication.run(VlcstreamerApplication.class, args);
//        NativeLibrary.addSearchPath(RuntimeUtil.getLibVlcLibraryName(), NATIVE_LIBRARY_SEARCH_PATH);
//        System.out.println(LibVlc.INSTANCE.libvlc_get_version());
        new NativeDiscovery().discover();
    }

}

package com.neshoj.vlcstreamer.videoprocessor;

import org.springframework.stereotype.Service;
import org.springframework.web.bind.annotation.GetMapping;

@Service
public class StreamerController {
    @GetMapping("/stream")
    public void viewStreamerPage(){

    }
}

package com.neshoj.vlcstreamer.videoprocessor;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.embedded.EmbeddedMediaPlayer;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;

@RestController
public class StreamerController {
    @GetMapping("/stream")
    public void viewStreamerPage(@RequestBody String rstpLink){


        MediaPlayerFactory factory = new MediaPlayerFactory();
        EmbeddedMediaPlayer mediaPlayer = factory.newEmbeddedMediaPlayer();
        File dir = new File("C:\\testcam");
        dir.mkdirs();
        DateFormat df = new SimpleDateFormat("ddMMyyyy-HHmmss");
        String fileName = dir.getAbsolutePath() + "Record - " + df.format(new Date()) + ".mpg";
        String[] options = {":sout=#transcode{vcodec=mp2v,vb=1024,acodec=mpga,ab=128,channels=2,samplerate=48000}:duplicate{dst=file{dst=" + fileName + "},dst=display}", ":input-slave=alsa://hw:0,0"};
        mediaPlayer.startMedia(rstpLink,options);
        File file = new File("C:\\testcam\\snapshot-test.png");
        mediaPlayer.saveSnapshot(file);
        System.out.println(mediaPlayer.getSnapshot());

    }

    @GetMapping(path = "api/image")
    public void captureImageToDisk(@RequestBody String rstpLink ){

        MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory();
        HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
        mediaPlayer.prepareMedia(rstpLink);
        mediaPlayer.play();
        try{
            Thread.sleep(4000);
        } catch(InterruptedException e) {
            e.printStackTrace();
        }

        BufferedImage img = mediaPlayer.getSnapshot();
        File file = new File("C:\\testCam\\snapshot-test.png");
        try{
            boolean png = ImageIO.write(img, "png", file);
        } catch(IOException e) {
            e.printStackTrace();
        }

    }
}

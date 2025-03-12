package com.neshoj.vlcstreamer.videoprocessor;

import lombok.extern.log4j.Log4j2;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;
import uk.co.caprica.vlcj.player.MediaPlayerFactory;
import uk.co.caprica.vlcj.player.headless.HeadlessMediaPlayer;

import javax.imageio.ImageIO;
import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;

@Log4j2
@RestController
public class StreamerController {
    @Value("${ip.camera.path}")
    String rstpLink;

    @Value("${ip.camera.photo-storage}")
    String photoFolder;

    @Value("${ip.camera.connection-sleep-time}")
    int connectionSleepTime;

    @Value("${ip.camera.default-image-path}")
    String defaultImagePath;

    @GetMapping(path = "cam/api/{cameraId}", produces = MediaType.IMAGE_PNG_VALUE)
    public ResponseEntity<byte[]> captureImageToDisk(@PathVariable String cameraId) {
        return useVLC(cameraId);
    }

    public ResponseEntity<byte[]> useVLC(final String cameraId) {
        try {
            String[] VLC_ARGS = {
                    "--intf", "dummy",          // no interface
                    "--vout", "dummy",          // we don't want video (output)
                    "--no-audio"              // we don't want audio (decoding)
            };
            MediaPlayerFactory mediaPlayerFactory = new MediaPlayerFactory(VLC_ARGS);
            HeadlessMediaPlayer mediaPlayer = mediaPlayerFactory.newHeadlessMediaPlayer();
            String format = String.format(rstpLink, cameraId);
            mediaPlayer.prepareMedia(format);
            mediaPlayer.play();

            try {
                Thread.sleep(connectionSleepTime);
            } catch (final InterruptedException e) {
                e.printStackTrace();
            }

            BufferedImage img = mediaPlayer.getSnapshot();
            String imagePath;
            byte[] image = null;

            if (img == null) {
                try {
                    image = FileUtils.readFileToByteArray(new File(defaultImagePath));
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                imagePath = photoFolder + System.currentTimeMillis() + ".jpg";
                File file = new File(imagePath);
                ImageIO.write(img, "jpg", file);

                ByteArrayOutputStream bos = new ByteArrayOutputStream();
                ImageIO.write(img, "jpg", bos);
                image = bos.toByteArray();
            }

            mediaPlayer.stop();
            mediaPlayerFactory.release();

            return ResponseEntity.ok().contentType(MediaType.IMAGE_JPEG).body(image);

        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}

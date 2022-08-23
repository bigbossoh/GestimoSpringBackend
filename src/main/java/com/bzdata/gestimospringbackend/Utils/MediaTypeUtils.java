package com.bzdata.gestimospringbackend.Utils;

import javax.servlet.ServletContext;

import org.springframework.http.MediaType;

public class MediaTypeUtils {
    public static MediaType getMediaTypeForFileNane(ServletContext servletContext,String filename) {
        String mineType = servletContext.getMimeType(filename);
        try {
            MediaType mediaType = MediaType.parseMediaType(mineType);
            return mediaType;
        } catch (Exception e) {
            return MediaType.APPLICATION_OCTET_STREAM;
        }
    }
}

package com.mmz.spring.beans.factory.config;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.net.URLConnection;

public class UrlResource implements Resource {

	private final URL url;

    public UrlResource(URL url) {
        this.url = url;
    }

    /**
     * 通过URL资源返回其字节流，是Resource的实现
     * */
    public InputStream getInputStream() throws IOException{
        URLConnection urlConnection = url.openConnection();
        urlConnection.connect();
        return urlConnection.getInputStream();
    }

}

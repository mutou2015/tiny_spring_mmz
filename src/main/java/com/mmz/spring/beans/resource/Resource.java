package com.mmz.spring.beans.resource;

import java.io.IOException;
import java.io.InputStream;

/**
 * 获取资源的字节输入流
 * */
public interface Resource {
	
	InputStream getInputStream() throws IOException;

}

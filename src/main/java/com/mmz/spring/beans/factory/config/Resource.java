package com.mmz.spring.beans.factory.config;

import java.io.IOException;
import java.io.InputStream;

public interface Resource {
	
	InputStream getInputStream() throws IOException;

}

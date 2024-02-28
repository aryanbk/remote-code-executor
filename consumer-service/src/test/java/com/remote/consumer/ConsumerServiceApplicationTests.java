package com.remote.consumer;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.ApplicationContext;

import static org.junit.jupiter.api.Assertions.assertNotNull;

@SpringBootTest
class ConsumerServiceApplicationTests {

	@Autowired
	private ApplicationContext context;

	@Test
	void contextLoads() {
		assertNotNull(context);
	}
}

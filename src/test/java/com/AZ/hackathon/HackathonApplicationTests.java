package com.AZ.hackathon;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest(properties = {
		"spring.data.mongodb.uri=mongodb://localhost:27017/hackathon-test",
		"app.mongodb.ping-on-startup=false"
})
class HackathonApplicationTests {

	@Test
	void contextLoads() {
	}

}

package com.bcopstein.ex4_lancheriaddd_v1;

import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.TestPropertySource;

@SpringBootTest
@TestPropertySource(properties = {
	"spring.datasource.url=jdbc:h2:mem:contexttests;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE",
	"spring.sql.init.mode=always",
	"spring.sql.init.schema-locations=classpath:db/schema.sql",
	"spring.sql.init.data-locations=classpath:db/data.sql"
})
class Ex4LancheriadddV1ApplicationTests {

	@Test
	void contextLoads() {
	}

}

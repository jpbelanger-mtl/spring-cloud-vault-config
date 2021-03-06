/*
 * Copyright 2016 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.springframework.cloud.vault.config;

import static org.assertj.core.api.Assertions.*;

import java.util.Collections;

import org.junit.BeforeClass;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.cloud.vault.util.VaultRule;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

/**
 * Integration test using config infrastructure with token authentication. In case this test should fail because of SSL
 * make sure you run the test within the spring-cloud-vault-config/spring-cloud-vault-config directory as the keystore
 * is referenced with {@code ../work/keystore.jks}.
 * 
 * @author Mark Paluch
 */
@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = VaultConfigWithContextTests.TestApplication.class)
@ActiveProfiles("my-profile")
public class VaultConfigWithContextTests {

	@BeforeClass
	public static void beforeClass() throws Exception {

		VaultRule vaultRule = new VaultRule();
		vaultRule.before();

		vaultRule.prepare().writeSecret("testVaultApp/my-profile",
				Collections.singletonMap("vault.value", "hello"));

		vaultRule.prepare().writeSecret("testVaultApp",
				Collections.singletonMap("vault.value", "worls"));
	}

	@Value("${vault.value}")
	String configValue;

	@Test
	public void contextLoads() {

		assertThat(configValue).isEqualTo("hello");
	}

	@SpringBootApplication
	public static class TestApplication {

		public static void main(String[] args) {
			SpringApplication.run(TestApplication.class, args);
		}
	}
}

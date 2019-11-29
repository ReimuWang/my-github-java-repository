package org.reimuwang.commonability;

import org.apache.commons.configuration.ConfigurationException;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reimuwang.commonability.config.ConfigUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonAbilityApplicationTests {

	@Test
	public void contextLoads() throws ConfigurationException {
		System.out.println(ConfigUtils.getString("logconfig.name"));
	}

}

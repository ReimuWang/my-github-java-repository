package org.reimuwang.testspringboot;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.core.io.support.EncodedResource;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.util.FileCopyUtils;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonTests {

	@Test
	public void test() throws IOException {
		Resource resource = new FileSystemResource("/Users/reimuwang/Downloads/testjson.json");
		EncodedResource encRes = new EncodedResource(resource, "UTF-8");
		String content  = FileCopyUtils.copyToString(encRes.getReader());
		JSONObject.parseObject(content).forEach((k, v) -> System.out.println(v));
	}

}

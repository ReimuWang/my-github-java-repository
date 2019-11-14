package org.reimuwang.testspringboot;

import com.alibaba.fastjson.JSONObject;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.reimuwang.commonability.json.JsonInputUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;
import java.util.Map;

@RunWith(SpringRunner.class)
@SpringBootTest
public class JsonTests {

	@Test
	public void test() throws IOException {
		JsonInputUtils.createFromSysPath("/Users/reimuwang/Downloads/testjson.json")
				      .convertJsonArray(JsonTests.class)
		              .forEach(e -> {
		              	System.out.println(JSONObject.toJSONString(e.data));
					  });
	}

	private Map<String, Object> data;

	public Map<String, Object> getData() {
		return data;
	}

	public void setData(Map<String, Object> data) {
		this.data = data;
	}
}

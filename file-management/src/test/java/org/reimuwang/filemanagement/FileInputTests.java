package org.reimuwang.filemanagement;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.File;
import java.util.Arrays;

@RunWith(SpringRunner.class)
@SpringBootTest
public class FileInputTests {

	@Test
	public void test() {
		System.out.println(Arrays.asList("xxx.1".split("\\.", 3)));

	}
}

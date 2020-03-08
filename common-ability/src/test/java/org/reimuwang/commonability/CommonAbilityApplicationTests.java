package org.reimuwang.commonability;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.reimuwang.commonability.image.ImageUtils;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import java.io.IOException;

@RunWith(SpringRunner.class)
@SpringBootTest
public class CommonAbilityApplicationTests {

	@Test
	public void test() throws IOException {
		ImageUtils.downloadImage("https://timgsa.baidu.com/timg?image&quality=80&size=b9999_10000&sec=1577081550694&di=3b0202c0e043148f12feacd68eb60852&imgtype=0&src=http%3A%2F%2Fb.hiphotos.baidu.com%2Fzhidao%2Fpic%2Fitem%2F71cf3bc79f3df8dc406e8ae5cc11728b47102876.jpg", "/Users/reimuwang/temp/test.jpg");
	}

}

package org.reimuwang.testspringboot.arthas;

import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@Slf4j
public class ArthasController {

    @RequestMapping("/arthas")
    public String test() {
        for (int i = 2; i < 100; i++) {
            testArthas(ArthasEntity.init(i), 1);
        }
        return "success";
    }

    private static ArthasEntity testArthas(ArthasEntity arthasEntity, Integer change) {
        ArthasEntity result = ArthasEntity.init(arthasEntity.getSeed() + change);
        arthasEntity.refreshSeed(-1 * change);
        return result;
    }
}
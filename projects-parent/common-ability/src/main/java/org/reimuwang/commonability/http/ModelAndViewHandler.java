package org.reimuwang.commonability.http;

import org.apache.commons.lang3.StringUtils;
import org.reimuwang.commonability.server.CommonListResponse;
import org.springframework.web.servlet.ModelAndView;

import java.util.ArrayList;
import java.util.List;

public class ModelAndViewHandler {

    private ModelAndView modelAndView;

    /**
     * 必须为奇数
     */
    private Integer pageCountForShow = 5;

    private ModelAndViewHandler(String viewName) {
        if (StringUtils.isBlank(viewName)) {
            throw new NullPointerException("构建modelAndView时，viewName为空");
        }
        this.modelAndView = new ModelAndView(viewName);
    }

    public static ModelAndViewHandler init(String viewName) {
        return new ModelAndViewHandler(viewName);
    }

    public ModelAndViewHandler add(String name, Object data) {
        if (StringUtils.isBlank(name)) {
            throw new NullPointerException("向modelAndView中添加属性时，name为空");
        }
        if (null == data) {
            throw new NullPointerException("向modelAndView中添加属性时，data为空");
        }
        this.modelAndView.addObject(name, data);
        return this;
    }

    public ModelAndView get() {
        return this.modelAndView;
    }

    public ModelAndViewHandler addPageInfo(CommonRequest request, CommonListResponse<?> result) {
        if (result.getDataList().isEmpty()) {
            return this;
        }

        Integer totalCount = result.getTotalCount();
        Integer page = request.getPage();
        Integer pageSize = request.getPageSize();
        int maxPage = totalCount % pageSize == 0 ? totalCount / pageSize : totalCount / pageSize + 1;

        this.add("currentPage", page);
        this.add("maxPage", maxPage);
        List<Integer> pageList = new ArrayList<>();
        this.add("pageList", pageList);

        int pageWishShowStart = page - (this.pageCountForShow - 1) / 2;
        pageWishShowStart = pageWishShowStart <= 0 ? 1 : pageWishShowStart;
        int pageWishShowEnd = page + (this.pageCountForShow - 1) / 2;
        pageWishShowEnd = pageWishShowEnd > maxPage ? maxPage : pageWishShowEnd;

        for (int i = pageWishShowStart; i <= pageWishShowEnd; i++) {
            pageList.add(i);
        }

        return this;
    }
}

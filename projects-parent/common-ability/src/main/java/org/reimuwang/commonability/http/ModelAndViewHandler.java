package org.reimuwang.commonability.http;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;

public class ModelAndViewHandler {

    private ModelAndView modelAndView;

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
}

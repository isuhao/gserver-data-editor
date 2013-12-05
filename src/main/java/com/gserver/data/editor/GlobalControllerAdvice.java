package com.gserver.data.editor;

import java.beans.PropertyEditorSupport;

import javax.servlet.http.HttpServletRequest;

import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;

import com.gserver.data.editor.exception.ParameterException;
import com.gserver.data.editor.util.EntityUtils;
import com.gserver.data.editor.util.ReflectionUtils;

@ControllerAdvice
public class GlobalControllerAdvice {

	@InitBinder
	// 此处的参数也可以是ServletRequestDataBinder类型
	public void initBinder(WebDataBinder binder) throws Exception {
		// 表示如果命令对象有TableEntity类型的属性，将使用该属性编辑器进行类型转换
		binder.registerCustomEditor(TableEntity.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				return getValue().toString();
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {
				Class<TableEntity> beanClass = EntityUtils.getMappedClass(text);
				TableEntity newInstance = ReflectionUtils.getNewInstance(beanClass);
				setValue(newInstance);
			}
		});
	}

	/** 基于@ExceptionHandler异常处理 */
	
	@ExceptionHandler// @ResponseBody(value = { BusinessException.class, ParameterException.class,
	// Exception.class})
	public String exp(HttpServletRequest request, Exception ex) {

		request.setAttribute("ex", ex);

		// 根据不同错误转向不同页面
		if (ex instanceof ParameterException) {
			return "error-parameter";
		} else {
			return "error";
		}
	}
}

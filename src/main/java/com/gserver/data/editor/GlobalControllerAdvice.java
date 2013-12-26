package com.gserver.data.editor;

import java.beans.PropertyEditorSupport;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.ServletRequestDataBinder;
import org.springframework.web.bind.WebDataBinder;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.InitBinder;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.gserver.data.editor.exception.ParameterException;
import com.gserver.data.editor.util.EntityUtils;

@ControllerAdvice
public class GlobalControllerAdvice {

	@InitBinder
	// 此处的参数也可以是ServletRequestDataBinder类型
	public void initBinder(WebDataBinder binder, final HttpServletRequest request) throws Exception {
		// 表示如果命令对象有TableEntity类型的属性，将使用该属性编辑器进行类型转换
		binder.registerCustomEditor(TableEntity.class, new PropertyEditorSupport() {
			@Override
			public String getAsText() {
				return getValue().toString();
			}

			@Override
			public void setAsText(String text) throws IllegalArgumentException {

				TableEntity newInstance = EntityUtils.getEntityInstance(text);
				new ServletRequestDataBinder(newInstance).bind(request);
				setValue(newInstance);
			}
		});
	}

	/** 基于@ExceptionHandler异常处理 */

	@ExceptionHandler(RuntimeException.class)
	// @ResponseBody(value = {
	// ParameterException.class,
	// Exception.class})
	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
	public String handleUnexpectedServerError(HttpServletRequest request, Exception ex) {

		request.setAttribute("ex", ex);
		return "error";
	}

	@ExceptionHandler(ParameterException.class)
	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
	@ResponseBody
	public String handleInvalidRequestError(ParameterException ex) {
		return ex.getMessage();
	}
}

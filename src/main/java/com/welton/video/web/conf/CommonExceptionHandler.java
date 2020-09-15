package com.welton.video.web.conf;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.SimpleMappingExceptionResolver;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.welton.micro.common.RestResponseException;
import com.welton.micro.common.rsp.ResultData;

/**
 * 通用异常请求处理，在Controller和处理服务中抛出异常，在此类中统一处理<br>
 * ajax请求（有@ResponseBody的Controller）发生错误，输出JSON。<br>
 * 页面请求（无@ResponseBody的Controller）发生错误，输出错误页面。<br>
 * 需要与AnnotationMethodHandlerAdapter使用同一个messageConverters<br>
 * Controller中需要有专门处理异常的方法。
 * 
 * 备注：此类必须放置在Application包或子包下面
 * @author yekaihe {@link RestResponseException}
 */
@Component
public class CommonExceptionHandler extends SimpleMappingExceptionResolver {
	private static Logger logger = LoggerFactory.getLogger(CommonExceptionHandler.class);

	public ModelAndView resolveException(HttpServletRequest request, HttpServletResponse response, Object handler,
			Exception ex) {
		
		//ResourceHttpRequestHandler
		if(!(handler instanceof HandlerMethod) && ex != null) {
			logger.warn(ex.getMessage());
			return null;
		}
		
		// 判断有没有@ResponseBody的注解没有的话调用父方法
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		
		//返回值需要处理成Body数据（AJAX）
		ResponseBody body = handlerMethod.getMethodAnnotation(ResponseBody.class);
		
		//RestContrller注解的Controller
		RestController[] rest = handlerMethod.getBean().getClass().getDeclaredAnnotationsByType(RestController.class);
		if (body == null && (rest==null || rest.length==0)) {
			return super.doResolveException(request, response, handlerMethod, ex);
		}
		
		RestResponseException e;
		if(ex instanceof RestResponseException){
			e = (RestResponseException)ex;
			
			// 调试模式打印调试日志
			if(logger.isDebugEnabled()) {
				//构造原始logger
				Logger exLogger = LoggerFactory.getLogger(e.getStackTrace()[0].getClassName());
				exLogger.debug(e.getDebugMsg());
				
				//Trace级别下打印堆栈（用于定位问题）
				logger.trace(e.getDebugMsg(), e);
			}
		} else {
			e = new RestResponseException(ex);
			logger.warn("not expected exception.", ex);
		}
		
		ResultData bean = ResultData.exception(e);
		
		ModelAndView mv = new ModelAndView();
		// 设置状态码,正常返回
		response.setStatus(HttpStatus.OK.value()); // 设置ContentType
		response.setContentType(MediaType.APPLICATION_JSON_VALUE); // 避免乱码
		response.setCharacterEncoding("UTF-8");
		response.setHeader("Cache-Control", "no-cache, must-revalidate");
		try {
			PrintWriter writer = response.getWriter();
			ObjectMapper objectMapper = new ObjectMapper();
			objectMapper.writeValue(writer, bean);
			writer.close();
		} catch (IOException ioe) {
			logger.error(ioe.getMessage());
		}
		return mv;
	}
}

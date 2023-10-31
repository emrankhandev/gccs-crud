package com.gccws.config;


import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;


/**
 * @Author    Md. Mizanur Rahman
 * @Since     August 1, 2022
 * @version   1.0.0
 */

@Order(Ordered.HIGHEST_PRECEDENCE)
@Component
public class CORSFilter implements Filter {

	final static Logger logger = LoggerFactory.getLogger(CORSFilter.class);

	@Override
	public void doFilter(ServletRequest req, ServletResponse res, FilterChain chain)
			throws IOException, ServletException {

		HttpServletRequest request = (HttpServletRequest) req;
		HttpServletResponse response = (HttpServletResponse) res;

		response.setHeader("Access-Control-Allow-Origin", "*");
		//response.setHeader("Access-Control-Allow-Credentials", "true");
		//response.setHeader("Access-Control-Allow-Private-Network", "true");
		//response.setHeader("Cache-Control", "public, max-age=604800");
		response.setHeader("Access-Control-Allow-Methods", "POST, GET, PUT, OPTIONS, DELETE");
		response.setHeader("Access-Control-Max-Age", "3600");
		//response.setHeader("Content-Security-Policy", "default-src self"); // Warning: after enable content security policy swagger ui is not one
		response.setHeader("Access-Control-Allow-Headers",
				"X-Requested-With, Content-Type, Authorization, Origin, Accept, Access-Control-Request-Method, Access-Control-Request-Headers");

		if(request.getMethod().equalsIgnoreCase("POST") ||
				request.getMethod().equalsIgnoreCase("GET") ||
				request.getMethod().equalsIgnoreCase("PUT") ||
				request.getMethod().equalsIgnoreCase("OPTIONS") ||
				request.getMethod().equalsIgnoreCase("DELETE")){

			if("OPTIONS".equalsIgnoreCase(request.getMethod()))
			{
				response.setStatus(HttpServletResponse.SC_OK);
			}
			else {
				chain.doFilter(req, res);
			}

		}else{
			String responseToClient= "{\n\"status\": 405,\n\"error\": \"Method Not Allowed\"\n}";
			response.setStatus(HttpServletResponse.SC_METHOD_NOT_ALLOWED);
			response.getWriter().write(responseToClient);
			response.getWriter().flush();
		}

	}

	@Override
	public void init(FilterConfig filterConfig) throws ServletException {
	    logger.info("Implementation not required");

	}

	@Override
	public void destroy() {
	    logger.info("Implementation not required");

	}

}

package com.quiz.exception.JSONParse;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.web.access.AccessDeniedHandler;
import org.springframework.stereotype.Component;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quiz.response.GlobalResponse;

@Component
public class CustomAccessDeniedHandler implements AccessDeniedHandler{

	@Override
	public void handle(HttpServletRequest request, HttpServletResponse response, AccessDeniedException ex) throws IOException {
        response.setStatus(HttpServletResponse.SC_FORBIDDEN);
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        
        Map<String, String> errorMessages = new HashMap<>();
		errorMessages.put("UnAutorized"," You do not have permission to access this resource." );
        
		/* Object of global response */
        GlobalResponse errorResponse = new GlobalResponse();
        errorResponse.setId(null);
        errorResponse.setMsg("Error occured due to following reasons");
        errorResponse.setErrors(errorMessages);
        errorResponse.setStatus(false);

        ObjectMapper mapper = new ObjectMapper();
        response.getWriter().write(mapper.writeValueAsString(errorResponse));
    }
}

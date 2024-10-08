package com.payment.service.dto.response;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.payment.service.dto.AppUserBasicProjectionDto;
import com.payment.service.dto.RestResponse;
import com.payment.service.enumerations.Namespace;
import com.payment.service.enumerations.RoleName;
import jakarta.servlet.ServletResponseWrapper;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.MediaType;

import java.io.IOException;
import java.util.List;
import java.util.Set;

public class ServletResponse {


    private static ObjectWriter ow = new ObjectMapper()
            .writer().withDefaultPrettyPrinter();


    public static void getErrorResponse(int code, RestResponse message, HttpServletResponse response) throws IOException {
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        response.setStatus(code);
        final ServletResponseWrapper responseWrapper = (ServletResponseWrapper) response;
        responseWrapper
                .getResponse()
                .getOutputStream()
                .write(ow.writeValueAsString(message).getBytes());
    }

    public static void getLoginResponse(String accessToken, String refreshToken, HttpServletResponse response,
                                        AppUserBasicProjectionDto user,
                                        Namespace namespace, Set<RoleName> roleNames) throws IOException {

        response.setContentType(MediaType.APPLICATION_JSON_VALUE);

        final ServletResponseWrapper responseWrapper = (ServletResponseWrapper) response;
        responseWrapper.getResponse().getOutputStream().write(ow.writeValueAsString(
                LoginResponse.builder()
                        .accessToken(accessToken)
                        .refreshToken(refreshToken)
                        .user(new UserDetailsDTO(user.publicId(),
                                user.mobile(), user.firstName(), user.lastName(), namespace, roleNames, user.kycLevel(),
                              user.primaryAccountId(), user.email()))
                        .build()
        ).getBytes());
    }

}

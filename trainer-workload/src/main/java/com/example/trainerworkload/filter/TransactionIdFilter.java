package com.example.trainerworkload.filter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.slf4j.MDC;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.UUID;

@Component
@Order(1)
public class TransactionIdFilter extends OncePerRequestFilter {

    public static final String TX_HEADER = "X-Transaction-Id";

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain chain)
        throws ServletException, IOException {

        String txId = request.getHeader(TX_HEADER);
        if (txId == null || txId.isBlank()) {
            txId = UUID.randomUUID().toString();
        }

        MDC.put("transactionId", txId);
        request.setAttribute("transactionId", txId);
        response.setHeader(TX_HEADER, txId);

        try {
            chain.doFilter(request, response);
        } finally {
            MDC.clear();
        }
    }
}
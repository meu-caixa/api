package br.com.meucaixa.api.annotations;

import io.opentelemetry.api.trace.Span;
import io.opentelemetry.api.trace.Tracer;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.stereotype.Component;

@Aspect
@Component
public class SpanAspect {
    private final Tracer tracer;

    public SpanAspect(Tracer tracer) {
        this.tracer = tracer;
    }

    @Around("@annotation(withSpan)")
    public Object traceMethod(ProceedingJoinPoint joinPoint, WithSpan withSpan) throws Throwable {
        String spanName = withSpan.value().isEmpty() ? joinPoint.getSignature().toShortString() : withSpan.value();
        Span span = tracer.spanBuilder(spanName).startSpan();
        try {
            span.addEvent("Method execution started");
            return joinPoint.proceed();
        } catch (Throwable throwable) {
            span.recordException(throwable);
            throw throwable;
        } finally {
            span.end();
        }
    }
}

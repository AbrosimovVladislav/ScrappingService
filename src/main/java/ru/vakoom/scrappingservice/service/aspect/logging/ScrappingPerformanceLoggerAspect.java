package ru.vakoom.scrappingservice.service.aspect.logging;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.context.annotation.EnableAspectJAutoProxy;
import org.springframework.stereotype.Component;
import org.springframework.util.StopWatch;
import ru.vakoom.scrappingservice.model.Offer;
import ru.vakoom.scrappingservice.model.ScrappingDateLog;
import ru.vakoom.scrappingservice.repository.ScrappingDateLogRepository;

import java.util.Date;
import java.util.List;

@Slf4j
@Aspect
@Component
@EnableAspectJAutoProxy
@RequiredArgsConstructor
public class ScrappingPerformanceLoggerAspect {

    private final ScrappingDateLogRepository scrappingDateLogRepository;

    @Pointcut("@within(annotation) || @annotation(annotation)")
    public void methodsAndPublicMethodsOfClassesWithMeasurePerformanceAnnotation(MeasurePerformance annotation) {
    }

    @Around(value = "methodsAndPublicMethodsOfClassesWithMeasurePerformanceAnnotation(annotation)", argNames = "joinPoint,annotation")
    public Object measurePerformance(ProceedingJoinPoint joinPoint, MeasurePerformance annotation) {
        final String className = joinPoint.getSignature().getDeclaringType().getSimpleName();
        final String methodName = joinPoint.getSignature().getName();

        ScrappingDateLog scrappingDateLog = null;
        final StopWatch stopWatch = new StopWatch();
        Exception exception = null;
        Object result;

        stopWatch.start();
        try {
            result = joinPoint.proceed();
            List<Offer> offers = (List<Offer>) result;
            if (!offers.isEmpty() && annotation.isScrappingNeeded()) {
                scrappingDateLog = new ScrappingDateLog();
                scrappingDateLog.setShopName(offers.get(0).getShopName());
                scrappingDateLog.setCountOfRecords(offers.size());
                scrappingDateLog.setDateOfScrap(new Date());
            }
        } catch (Throwable e) {
            exception = (Exception) e;
            throw new RuntimeException(e);
        } finally {
            stopWatch.stop();

            if (scrappingDateLog != null && annotation.isScrappingNeeded()) {
                scrappingDateLog.setTimeOfScrapping(stopWatch.getTotalTimeMillis());
                scrappingDateLogRepository.save(scrappingDateLog);
            }

            if (exception != null) {
                log.error("Exception catches inside PerformanceLoggerAspect ({}.{}()) with message {}. Duration {}ms",
                        className, methodName, exception.getMessage(), stopWatch.getTotalTimeMillis());
            } else {
                log.info("Duration of call: {}.{}() - {}ms", className, methodName, stopWatch.getTotalTimeMillis());
            }

        }
        return result;
    }

}

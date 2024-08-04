package ru.somarov.auth.infrastructure.observability.opentelemetry

import io.opentelemetry.api.common.AttributeKey
import io.opentelemetry.api.common.Attributes
import io.opentelemetry.api.trace.propagation.W3CTraceContextPropagator
import io.opentelemetry.exporter.otlp.logs.OtlpGrpcLogRecordExporter
import io.opentelemetry.exporter.otlp.trace.OtlpGrpcSpanExporter
import io.opentelemetry.sdk.OpenTelemetrySdk
import io.opentelemetry.sdk.common.internal.OtelVersion
import io.opentelemetry.sdk.logs.SdkLoggerProvider
import io.opentelemetry.sdk.logs.export.BatchLogRecordProcessor
import io.opentelemetry.sdk.metrics.SdkMeterProvider
import io.opentelemetry.sdk.resources.Resource
import io.opentelemetry.sdk.trace.SdkTracerProvider
import io.opentelemetry.sdk.trace.export.BatchSpanProcessor
import io.opentelemetry.sdk.trace.samplers.Sampler
import ru.somarov.auth.infrastructure.props.AppProps

fun createOpenTelemetrySdk(props: AppProps): OpenTelemetrySdk {
    return OpenTelemetrySdk.builder()
        .setPropagators { W3CTraceContextPropagator.getInstance() }
        .setMeterProvider(buildMeterProvider())
        .setLoggerProvider(buildLoggerProvider(props))
        .setTracerProvider(buildTracerProvider(props))
        .build()
}

private fun buildMeterProvider(): SdkMeterProvider {
    return SdkMeterProvider.builder().build()
}

private fun buildLoggerProvider(props: AppProps): SdkLoggerProvider {
    val builder = SdkLoggerProvider
        .builder()
        .addLogRecordProcessor(
            BatchLogRecordProcessor.builder(
                OtlpGrpcLogRecordExporter.builder()
                    .setEndpoint("${props.otel.protocol}://${props.otel.host}:${props.otel.logsPort}")
                    .build()
            ).build()
        )
        .setResource(
            Resource.create(
                Attributes.builder()
                    .put("telemetry.sdk.name", "opentelemetry")
                    .put("telemetry.sdk.language", "java")
                    .put("telemetry.sdk.version", OtelVersion.VERSION)
                    .put("service.name", props.name)
                    .build()
            )
        )
    return builder.build()
}

private fun buildTracerProvider(props: AppProps): SdkTracerProvider {
    val sampler = Sampler.parentBased(Sampler.traceIdRatioBased(props.otel.tracingProbability))
    val resource = Resource.getDefault()
        .merge(Resource.create(Attributes.of(AttributeKey.stringKey("service.name"), props.name)))

    val builder = SdkTracerProvider.builder().setSampler(sampler).setResource(resource)
        .addSpanProcessor(
            BatchSpanProcessor.builder(
                OtlpGrpcSpanExporter.builder()
                    .setEndpoint("${props.otel.protocol}://${props.otel.host}:${props.otel.tracingPort}")
                    .build()
            ).build()
        )
    return builder.build()
}

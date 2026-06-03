package com.geo.dsp.config;

import com.fasterxml.jackson.core.JsonGenerator;
import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import com.fasterxml.jackson.databind.JsonSerializer;
import com.fasterxml.jackson.databind.SerializerProvider;
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule;
import org.springframework.boot.autoconfigure.jackson.Jackson2ObjectMapperBuilderCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.io.IOException;
import java.time.LocalDate;
import java.time.OffsetDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
/**
 * Jackson 全局配置：完整处理 OffsetDateTime 的序列化与反序列化
 * 使用 deserializerByType/serializerByType 确保最高优先级
 */
@Configuration
public class JacksonConfig {

    @Bean
    public Jackson2ObjectMapperBuilderCustomizer offsetDateTimeCustomizer() {
        return builder -> {
            builder.serializerByType(OffsetDateTime.class, new IsoOffsetDateTimeSerializer());
            builder.deserializerByType(OffsetDateTime.class, new LenientOffsetDateTimeDeserializer());
            builder.modules(new JavaTimeModule());
        };
    }

    static class IsoOffsetDateTimeSerializer extends JsonSerializer<OffsetDateTime> {
        @Override
        public void serialize(OffsetDateTime value, JsonGenerator gen, SerializerProvider provider) throws IOException {
            gen.writeString(DateTimeFormatter.ISO_OFFSET_DATE_TIME.format(value));
        }
    }

    static class LenientOffsetDateTimeDeserializer extends JsonDeserializer<OffsetDateTime> {

        private static final DateTimeFormatter[] FORMATTERS = {
                DateTimeFormatter.ISO_OFFSET_DATE_TIME,
                DateTimeFormatter.ISO_LOCAL_DATE_TIME,
                DateTimeFormatter.ISO_LOCAL_DATE
        };

        @Override
        public OffsetDateTime deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
            String text = p.getText().trim();
            if (text.isEmpty()) return null;

            for (DateTimeFormatter fmt : FORMATTERS) {
                try {
                    if (fmt == DateTimeFormatter.ISO_LOCAL_DATE) {
                        return LocalDate.parse(text, fmt)
                                .atStartOfDay()
                                .atOffset(ZoneOffset.ofHours(8));
                    } else if (fmt == DateTimeFormatter.ISO_LOCAL_DATE_TIME) {
                        return java.time.LocalDateTime.parse(text, fmt)
                                .atOffset(ZoneOffset.ofHours(8));
                    } else {
                        return OffsetDateTime.parse(text, fmt);
                    }
                } catch (DateTimeParseException ignored) {
                }
            }
            throw new IOException("无法解析日期: " + text);
        }
    }
}

package ru.vakoom.scrappingservice.service;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;
import java.util.Map;

@Data
@Accessors(fluent = true)
public class ScrapperMeta {
    private String productParamName;
    private ScrapperMethod scrapperMethod;
    private List<String> classChain;
    private Map<String, String> additionalParams;
}

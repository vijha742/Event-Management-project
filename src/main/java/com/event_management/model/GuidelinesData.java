package com.event_management.model;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class GuidelinesData {
    @JsonProperty("generalRules")
    private List<String> generalRules;
    
    @JsonProperty("technicalGuidelines")
    private List<String> technicalGuidelines;
    
    @JsonProperty("judgingCriteria")
    private List<String> judgingCriteria;
    
    private ResourceInfo resources;
}

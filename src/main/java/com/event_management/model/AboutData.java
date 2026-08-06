package com.event_management.model;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class AboutData {
    private String title;
    private List<AboutSection> sections;
    private CallToAction callToAction;
}

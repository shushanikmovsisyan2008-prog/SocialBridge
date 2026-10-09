package com.socialsupport.controller;

import jakarta.servlet.http.HttpServletRequest;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import lombok.RequiredArgsConstructor;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import java.util.LinkedHashMap;
import java.util.Map;

@ControllerAdvice
@RequiredArgsConstructor
public class SeoModelAdvice {
    private final ObjectMapper json;

    @ModelAttribute
    void addPageMetadata(HttpServletRequest request, Model model) {
        String path = request.getRequestURI();
        String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        model.addAttribute("canonicalUrl", base + path);
        model.addAttribute("metaDescription", descriptionFor(path));
        boolean privatePage = path.equals("/account") || path.equals("/login") || path.equals("/register");
        boolean searchPage = path.equals("/services") && request.getQueryString() != null;
        model.addAttribute("robotsDirective", privatePage || searchPage ? "noindex,follow" : "index,follow");
        Map<String, Object> search = Map.of("@type", "SearchAction", "target", base + "/services?q={search_term_string}", "query-input", "required name=search_term_string");
        Map<String, Object> website = new LinkedHashMap<>();
        website.put("@context", "https://schema.org"); website.put("@type", "WebSite");
        website.put("name", "SocialBridge"); website.put("url", base + "/"); website.put("potentialAction", search);
        try { model.addAttribute("websiteSchema", json.writeValueAsString(website)); }
        catch (JsonProcessingException e) { model.addAttribute("websiteSchema", "{}"); }
    }

    private String descriptionFor(String path) {
        if (path.equals("/")) return "Explore clear, practical information about family, food, wellbeing, housing, work and community support.";
        if (path.equals("/about")) return "Learn about SocialBridge and its goal of making social support information easier to find.";
        if (path.equals("/services")) return "Browse and search SocialBridge's directory of practical social support topics and resources.";
        if (path.equals("/contact")) return "Find your next step with SocialBridge's support directory and account options.";
        if (path.startsWith("/services/")) return "Explore practical guidance, resources and next steps for this social support topic.";
        return "SocialBridge helps people explore clear information about social support and community resources.";
    }
}

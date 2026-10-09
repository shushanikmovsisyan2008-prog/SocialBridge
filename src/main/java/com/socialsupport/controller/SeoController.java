package com.socialsupport.controller;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

@Controller
public class SeoController {
    private static final String[] PUBLIC_PATHS = {
            "/", "/about", "/services", "/contact",
            "/services/family-help", "/services/food-assistance", "/services/wellbeing",
            "/services/employment", "/services/housing", "/services/community"
    };

    @GetMapping(value = "/robots.txt", produces = MediaType.TEXT_PLAIN_VALUE)
    @ResponseBody
    String robots() {
        String sitemap = ServletUriComponentsBuilder.fromCurrentContextPath()
                .path("/sitemap.xml").toUriString();
        return "User-agent: *\nAllow: /\nSitemap: " + sitemap + "\n";
    }

    @GetMapping(value = "/sitemap.xml", produces = MediaType.APPLICATION_XML_VALUE)
    @ResponseBody
    String sitemap() {
        String base = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString();
        String urls = java.util.Arrays.stream(PUBLIC_PATHS)
                .map(path -> "<url><loc>" + base + path + "</loc></url>")
                .collect(java.util.stream.Collectors.joining());
        return "<?xml version=\"1.0\" encoding=\"UTF-8\"?>"
                + "<urlset xmlns=\"http://www.sitemaps.org/schemas/sitemap/0.9\">" + urls + "</urlset>";
    }
}

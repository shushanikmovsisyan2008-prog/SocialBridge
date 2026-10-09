package com.socialsupport.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.ArrayList;
import java.util.List;

@Controller
public class SiteController {
    private static class ServiceItem {
        private final String slug, name, category, icon, summary, details, solution;
        ServiceItem(String slug, String name, String category, String icon,
                    String summary, String details, String solution) {
            this.slug = slug; this.name = name; this.category = category; this.icon = icon;
            this.summary = summary; this.details = details; this.solution = solution;
        }
        public String getSlug() { return slug; }
        public String getName() { return name; }
        public String getCategory() { return category; }
        public String getIcon() { return icon; }
        public String getSummary() { return summary; }
        public String getDetails() { return details; }
        public String getSolution() { return solution; }
    }

    private final List<ServiceItem> services = List.of(
        new ServiceItem("family-help", "Ընտանիքի աջակցություն", "Ընտանիք", "bi-people",
            "Խորհրդատվություն և գործնական աջակցություն ընտանիքներին։",
            "Գտեք ընտանեկան խորհրդատվություն, ծնողավարման ուղեցույցներ և համայնքային ռեսուրսներ։",
            "Գործողությունների պլան․ կազմեք ընտանիքի հիմնական կարիքների ցանկը, կապվեք համայնքային սոցիալական աշխատողի հետ և պայմանավորվեք նախնական խորհրդատվության համար։"),
        new ServiceItem("food-assistance", "Սննդի աջակցություն", "Առաջնային կարիքներ", "bi-basket2",
            "Գտեք սննդի և առաջին անհրաժեշտության իրերի աջակցության ծրագրեր։",
            "Ուսումնասիրեք համայնքային սննդի բանկերն ու ամենօրյա կարիքներին օգնող ծրագրերը։",
            "Գործողությունների պլան․ հաշվեք առաջիկա շաբաթվա հիմնական սննդային կարիքները, ճշտեք մոտակա համայնքային կենտրոնի կամ բարեգործական կազմակերպության ընդունելության օրերը և նախապես զանգահարեք՝ անհրաժեշտ փաստաթղթերը պարզելու համար։"),
        new ServiceItem("wellbeing", "Հոգեկան և ընդհանուր բարեկեցություն", "Առողջություն", "bi-heart-pulse",
            "Գտեք զրուցելու և բարեկեցության աջակցություն ստանալու հնարավորություն։",
            "Կապ հաստատեք տեղական աջակցության ծառայությունների և համայնքային կազմակերպությունների հետ։",
            "Գործողությունների պլան․ ընտրեք վստահելի մասնագետ կամ խորհրդատվական կենտրոն, զանգահարեք առաջին հանդիպումը նշանակելու համար և գրանցեք այն հարցերը, որոնք ցանկանում եք քննարկել։"),
        new ServiceItem("employment", "Աշխատանք և հմտություններ", "Զբաղվածություն", "bi-briefcase",
            "Կատարեք հաջորդ քայլը դեպի աշխատանք և նոր հմտություններ։",
            "Գտեք աշխատանքի որոնման խորհուրդներ, ուսուցման հնարավորություններ և մասնագիտական ուղղորդում։",
            "Գործողությունների պլան․ թարմացրեք ինքնակենսագրականը, ընտրեք ձեզ համապատասխան երեք թափուր աշխատատեղ և շաբաթվա ընթացքում դիմեք դրանց։ Զուգահեռ ճշտեք անվճար մասնագիտական դասընթացների առկայությունը։"),
        new ServiceItem("housing", "Բնակարանային հարցերի աջակցություն", "Բնակարան", "bi-house-heart",
            "Ստացեք օգնություն բնակարանային տարբերակները հասկանալու համար։",
            "Գտեք խորհրդատվություն և կազմակերպություններ, որոնք կարող են բացատրել ձեր հնարավոր տարբերակները։",
            "Գործողությունների պլան․ հավաքեք բնակության և վարձակալության հետ կապված փաստաթղթերը, գրանցեք հրատապ խնդիրները և կապվեք համայնքի սոցիալական ծառայության կամ իրավաբանական խորհրդատվության կենտրոնի հետ՝ հնարավոր աջակցության պայմանները ճշտելու համար։"),
        new ServiceItem("community", "Համայնքային կապեր", "Համայնք", "bi-chat-heart",
            "Ծանոթացեք տեղական խմբերին և ստեղծեք աջակցող կապեր։",
            "Բացահայտեք ձեր շրջանի համայնքային միջոցառումներն ու խմբերը։",
            "Գործողությունների պլան․ ընտրեք ձեզ հետաքրքրող մեկ համայնքային միջոցառում կամ կամավորական խումբ, ճշտեք մասնակցության պայմանները և փորձեք մասնակցել առաջիկա հանդիպմանը։")
    );

    @GetMapping("/")
    public String home(Model model) {
        model.addAttribute("services", services.subList(0, 3));
        return "home";
    }
    @GetMapping("/about") public String about() { return "about"; }
    @GetMapping("/contact") public String contact() { return "contact"; }

    @GetMapping("/services")
    public String services(@RequestParam(defaultValue = "") String q, Model model) {
        List<ServiceItem> found = new ArrayList<>();
        for (ServiceItem service : services) {
            String text = service.getName() + " " + service.getCategory() + " " + service.getSummary();
            if (text.toLowerCase().contains(q.toLowerCase())) found.add(service);
        }
        model.addAttribute("services", found);
        model.addAttribute("query", q);
        return "services";
    }

    @GetMapping("/services/{slug}")
    public String service(@PathVariable String slug, Model model) {
        for (ServiceItem service : services) {
            if (service.getSlug().equals(slug)) {
                model.addAttribute("service", service);
                return "service-detail";
            }
        }
        return "error/404";
    }
}

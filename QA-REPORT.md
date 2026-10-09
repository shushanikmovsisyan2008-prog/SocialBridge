# Project notes

- User account pages and database calls are in `UserController`.
- Public pages are in `SiteController`; SEO routes and page metadata are kept separately.
- The configured server port is 8080.
- The latest Maven build could not be confirmed in this environment because Java failed while closing the compiler's `spring-orm` archive. The code change should be rebuilt locally after opening the project.
- Lighthouse scores need to be measured against the deployed site.

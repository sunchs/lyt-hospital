package com.sunchs.lyt.hospital;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.web.servlet.MultipartConfigFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.multipart.MultipartResolver;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

import javax.servlet.MultipartConfigElement;
import java.io.File;

@SpringBootApplication(scanBasePackages = {"com.sunchs.lyt.db","com.sunchs.lyt.hospital"})
public class HospitalApplication {
    public static void main(String[] args) {
        SpringApplication.run(HospitalApplication.class, args);
    }

//    private CorsConfiguration buildConfig() {
//        CorsConfiguration corsConfiguration = new CorsConfiguration();
//        corsConfiguration.addAllowedOrigin("*");
//        corsConfiguration.addAllowedHeader("*");
//        corsConfiguration.addAllowedMethod("*");
//        corsConfiguration.setAllowCredentials(true);//这两句不加不能跨域上传文件，
//        corsConfiguration.setMaxAge(83600L);//加上去就可以了
////        corsConfiguration.addAllowedHeader("Access-Control-Allow-Origin");
//        return corsConfiguration;
//    }

//    /**
//     * 跨域过滤器
//     * @return
//     */
//    @Bean
//    public CorsFilter corsFilter() {
//        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
//        source.registerCorsConfiguration("/**", buildConfig()); // 4
//        return new CorsFilter(source);
//    }

    @Bean(name = "uploadInterceptor")
    public WebMvcConfigurerAdapter uploadInterceptor() {
        return new WebMvcConfigurerAdapter() {
            @Override
            public void addInterceptors(InterceptorRegistry registry) {
                registry.addInterceptor(new UploadInterceptor());
                //.excludePathPatterns("/imageThumbnail");
            }
        };
    }

    @Bean
    public MultipartConfigElement multipartConfigElement() {
        MultipartConfigFactory factory = new MultipartConfigFactory();
        //单个文件最大
        factory.setMaxFileSize("80MB"); //KB,MB
        /// 设置单个请求总上传数据总大小
        factory.setMaxRequestSize("102400KB");
        String rootPath = "/lytTemp";
        File root = new File(rootPath);
        if ( ! root.exists()) {
            root.mkdirs();
        }
//        if (!StringUtils.isEmpty(tempLocation)) {
            File tempLocationFile = new File(rootPath);
            if (!tempLocationFile.exists()) {
                boolean mkdirs = tempLocationFile.mkdirs();
                if (!mkdirs) {
                    throw new RuntimeException("fail to create temp location : " + rootPath);
                }
            }
            factory.setLocation(rootPath);
//        }
        return factory.createMultipartConfig();
    }

//    @Bean(name = "multipartResolver")
//    public MultipartResolver multipartResolver() {
////        log.info("Loading the multipart resolver");
//        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
//        multipartResolver.setDefaultEncoding("UTF-8");
//        multipartResolver.setResolveLazily(true);//resolveLazily属性启用是为了推迟文件解析，以在在UploadAction中捕获文件大小异常
//        multipartResolver.setMaxInMemorySize(40960);
//        multipartResolver.setMaxUploadSize(50*1024*1024);//上传文件大小 50M 50*1024*1024
//        return multipartResolver;
//    }
}
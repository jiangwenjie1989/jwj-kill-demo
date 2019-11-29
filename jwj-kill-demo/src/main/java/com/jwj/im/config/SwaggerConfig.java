package com.jwj.im.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.ResponseEntity;
import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.ParameterBuilder;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.schema.ModelRef;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Parameter;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;
import java.util.List;


@Configuration
@EnableSwagger2
public class SwaggerConfig{
   @Bean
   public Docket customDocket(){
      ParameterBuilder par = new ParameterBuilder();
      ParameterBuilder par1 = new ParameterBuilder();
      ParameterBuilder par2 = new ParameterBuilder();
      ParameterBuilder par3 = new ParameterBuilder();
      //ParameterBuilder par4 = new ParameterBuilder();
      
      List<Parameter> pars = new ArrayList<>();
      par.name("requestId").description(" 请求id").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par1.name("timestamp").description("请求时间戳").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par2.name("channelType").description("请求来源类型").modelRef(new ModelRef("String")).parameterType("header").required(false);
      par3.name("sign").description("参数签名").modelRef(new ModelRef("String")).parameterType("header").required(false);
     //par4.name("salt").description("加盐").modelRef(new ModelRef("String")).parameterType("header").required(false);
      pars.add(par.build());
      pars.add(par1.build());
      pars.add(par2.build());
      pars.add(par3.build());
      //pars.add(par4.build());
      return new Docket(DocumentationType.SWAGGER_2)
           .apiInfo(apiInfo())
           .genericModelSubstitutes(ResponseEntity.class)
           .select()
           //配置仅需要显示api的路径
           .apis(RequestHandlerSelectors.basePackage("com.jwj.im.controller"))
           .build()
           .globalOperationParameters(pars);
             
   }
  
   private ApiInfo apiInfo() {
       return new ApiInfoBuilder()
               .title("秒杀测试api接口文档")
               .description("api根地址：http://。。。。。。。/ " +
                       "header里面定义的参数如下：" +
                       "timestamp:时间戳验证," +
                       "requestId:保证每次请求都是唯一," +
                       "sign:签名," +
                       "channelId:渠道来源 ios android h5 web ")
              // .termsOfServiceUrl("https://xiaomo.info/")
               .contact("***")
               .version("0.0.1")
               .build();
   }


}

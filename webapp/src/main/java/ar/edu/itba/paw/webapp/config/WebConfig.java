package ar.edu.itba.paw.webapp.config;

import ar.edu.itba.paw.webapp.dto.annotations.*;
import ar.edu.itba.paw.webapp.dto.annotations.Number;
import org.hibernate.validator.constraints.Email;
import org.hibernate.validator.constraints.NotBlank;
import org.hibernate.validator.constraints.NotEmpty;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.support.ReloadableResourceBundleMessageSource;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.jdbc.datasource.init.DataSourceInitializer;
import org.springframework.jdbc.datasource.init.DatabasePopulator;
import org.springframework.jdbc.datasource.init.ResourceDatabasePopulator;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.JpaVendorAdapter;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.validation.beanvalidation.LocalValidatorFactoryBean;
import org.springframework.web.multipart.commons.CommonsMultipartResolver;
import org.springframework.web.servlet.ViewResolver;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;
import org.springframework.web.servlet.view.InternalResourceViewResolver;
import org.springframework.web.servlet.view.JstlView;

import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.*;
import java.util.HashMap;
import java.util.Map;
import java.util.Properties;
import java.util.concurrent.TimeUnit;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import javax.validation.Validator;
import javax.validation.ValidatorFactory;
import javax.validation.constraints.*;
import javax.ws.rs.core.CacheControl;

@EnableTransactionManagement
@EnableWebMvc
@ComponentScan({
        "ar.edu.itba.paw.webapp.controller",
        "ar.edu.itba.paw.services",
        "ar.edu.itba.paw.persistence",
})
@Configuration
public class WebConfig extends WebMvcConfigurerAdapter {
    @Bean
    public ViewResolver viewResolver() {
        final InternalResourceViewResolver viewResolver = new InternalResourceViewResolver();

        viewResolver.setViewClass(JstlView.class);
        viewResolver.setPrefix("/WEB-INF/jsp/");
        viewResolver.setSuffix(".jsp");


        return viewResolver;
    }


    @Bean
    public DataSource dataSource() {
        final SimpleDriverDataSource ds = new SimpleDriverDataSource();

        ds.setDriverClass(org.postgresql.Driver.class);
        ds.setUrl("jdbc:postgresql://localhost/paw-2020b-2");
        ds.setUsername("paw-2020b-2");
        ds.setPassword("pt8AieF9x");

        return ds;
    }

    @Bean(name="messageSource")
    public MessageSource messageSource() {
        final ReloadableResourceBundleMessageSource messageSource = new ReloadableResourceBundleMessageSource();
        messageSource.setBasename("classpath:i18n/messages");
        messageSource.setDefaultEncoding(Charset.defaultCharset().displayName());
        messageSource.setCacheSeconds(5);

        return messageSource;
    }

    @Bean
    public URL getURL() throws MalformedURLException {
        return new URL("http://pawserver.it.itba.edu.ar/paw-2020b-2");
    }

    @Bean(name = "multipartResolver")
    public CommonsMultipartResolver multipartResolver() {
        CommonsMultipartResolver multipartResolver = new CommonsMultipartResolver();
        multipartResolver.setMaxUploadSize(70*1024*1000000L);
        return multipartResolver;
    }

    @Bean
    public DataSourceInitializer dataSourceInitializer(final DataSource ds) {
        final DataSourceInitializer dsi = new DataSourceInitializer();

        dsi.setDataSource(ds);
        dsi.setDatabasePopulator(databasePopulator());

        return dsi;
    }

    // Use addScript(Resource) to include .sql files
    private DatabasePopulator databasePopulator() {
        return new ResourceDatabasePopulator();
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        registry.addResourceHandler("/resources/**").addResourceLocations("/resources/", "file:/resources/");
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory() {
        final LocalContainerEntityManagerFactoryBean factoryBean = new LocalContainerEntityManagerFactoryBean();
        factoryBean.setPackagesToScan("ar.edu.itba.paw.models");
        factoryBean.setDataSource(dataSource());
        final JpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        factoryBean.setJpaVendorAdapter(vendorAdapter);
        final Properties properties = new Properties();
        properties.setProperty("hibernate.hbm2ddl.auto", "update");
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL92Dialect");

        factoryBean.setJpaProperties(properties);
        return factoryBean;
    }

    @Bean
    public PlatformTransactionManager transactionManager(final EntityManagerFactory emf) {
        return new JpaTransactionManager(emf);
    }

    @Bean
    public ValidatorFactory getValidatorFactory(final MessageSource messageSource) {
        LocalValidatorFactoryBean validatorFactory = new LocalValidatorFactoryBean();
        validatorFactory.setValidationMessageSource(messageSource);
        return validatorFactory;
    }

    @Bean
    public Validator validator(final ValidatorFactory getValidatorFactory){
        return getValidatorFactory.getValidator();
    }

    // Including this cache control into a response means the response is going to be cached
    // Do not include if you dont want to cache answer
    @Bean
    public CacheControl cacheControl(){
        CacheControl cacheControl = new CacheControl();
        // We inform clients that they should cache our responses only on their browsers
        cacheControl.setPrivate(true);
        // Set freshness to 1 minute, determine what is the amount we want to cache for
        cacheControl.setMaxAge(Math.toIntExact(TimeUnit.MINUTES.toSeconds(1)));
        // We tell client to revalidate stale caches
        cacheControl.setMustRevalidate(true);
        return cacheControl;
    }

    @Bean(name="annotationCodes")
    public Map<String,String> configMap(){

        // possible error codes
        final String ALREADY_EXISTS = "already_exists";
        final String INVALID = "invalid";
        final String MISSING = "missing";
        final String MISSING_FIELD = "missing_field";
        final String UNPROCESSABLE = "unprocessable";
        final String OTHER = "other";

        Map<String,String> map = new HashMap<>();

        // from javax validation
        map.put(Max.class.getName(),INVALID);
        map.put(Min.class.getName(),INVALID);
        map.put(NotNull.class.getName(), MISSING_FIELD);
        map.put(Null.class.getName(), INVALID);
        map.put(Pattern.class.getName(), INVALID);
        map.put(Size.class.getName(), INVALID);

        // from hibernate validation
        map.put(NotBlank.class.getName(), MISSING_FIELD);
        map.put(NotEmpty.class.getName(), MISSING_FIELD);
        map.put(Email.class.getName(), INVALID);

        // created
        map.put(ArrayAsString.class.getName(), INVALID);
        map.put(BooleanArrayAsString.class.getName(), INVALID);
        map.put(ByteArray.class.getName(), INVALID);
        map.put(ClinicId.class.getName(), MISSING);
        map.put(Days.class.getName(), INVALID);
        map.put(EmailCollection.class.getName(), INVALID);
        map.put(IntegerSize.class.getName(), INVALID);
        map.put(Locale.class.getName(), INVALID);
        map.put(MedicId.class.getName(), MISSING);
        map.put(PatientPlan.class.getName(), INVALID);
        map.put(NotRegistered.class.getName(),ALREADY_EXISTS);
        map.put(Number.class.getName(), INVALID);
        map.put(Password.class.getName(), INVALID);
        map.put(StudyTypeId.class.getName(), MISSING);
        map.put(TimeArrayAsString.class.getName(), INVALID);
        map.put(TimeIntervals.class.getName(), INVALID);

        return map;
    }

}

# What are Java Annotations?
* Labels that provide meta-data about the class
  * Boot (Class): Color (label)
* ``@Override`` telling the compiler that we want to override that method. At compile time, the compiler will verify the override.

# Spring Configuration with Annotations
* XML can be very verbose
* Configure beans with Annotations
* Spring will scan your Java classes for annotations
* Automatically register the beans in the Spring container (no more XML needed).

# Development Process
1. Enable component scanning in Spring config file
2. Add the ``@Component`` Annotation to class
3. Retrieve the bean from Spring container

Enable component scanning in Spring config file
```xml
<beans ...>
    <context:component-scan base-package="com.dstoecklein.springdemo" />
</beans>
```

Add the ``@Component`` Annotation to class. Will register bean. ``thatSillyCoach`` is the bean id.
````java
@Component("thatSillyCoach")
public class TennisCoach implements Coach {
    
    @Override
    public String getDailyWorkout() {
        return "TennisCoach";
    }
}
````

Retrieve the bean from Spring container

````java
Coach theCoach = context.getBean("thatSillyCoach", Coach.class);
````

# Constructor Injection
* Now we are using ``@Autowiring``
* For dependency injection, Spring can autowire these dependencies
* Spring will look for a class that matches the property (by class or interface)
* 3 types
  * Constructor injection
  * Setter injection
  * Field injection

## Autowiring example
* Injecting FortuneService into a Coach implementation
* Spring will scan ``@Components``
* Did anyone implement FortuneService interface???

1. Define the dependency interface and class
2. Create a constructor in your class for injections
3. Configure the dependency injection with ``@Autowired``

Define the dependency interface and class

````java
public interface FortuneService {
    public String getFortune();
}

@Component
public class HappyFortuneService implements FortuneService {
    public String getFortune() {
        return "Today is your lucky day!";
    }
}
````

Create a constructor in your class for injections
Configure the dependency injection with ``@Autowired``

````java
@Component
public class TennisCoach implements Coach {
    private FortuneService fortuneService;

    @Autowired
    public TennisCoach(FortuneService theFortuneService) {
        fortuneService = theFortuneService;
    }
}
````

Spring will find a bean that implements ``FortuneService``, here its ``HappyFortuneService``. So we get the ``Coach`` object and its ``FortuneService`` wired up together.

# Setter injection
* Inject dependencies by calling setter methods

1. Create setter methods in your class
2. Configure the dependency injection with ``@Autowired``

Create setter methods in your class
Configure the dependency injection with ``@Autowired``

````java
public class TennisCoach implements Coach {
  private FortuneService fortuneService;
  
  public TennisCoach() {}
  
  @Autowired
  public void setFortuneService(FortuneService fortuneService) {
    this.fortuneService = fortuneService;
  }
}
````

# Field injection
* Inject dependencies by setting the field values on your class directly
* Accomplished by using Java reflection

1. Configure the dependency using ``@Autowired``
  2. Applied directly on fields
  3. No setters required

````java
public class TennisCoach implements Coach {
    
    @Autowired
    private FortuneService fortuneService;

    public TennisCoach() {}
}
````

## Which injection type to use?
Choose a style and keep consistent.

# Annotation Autowiring and Qualifiers
* But which implementation to choose if multiple interface implementations exist?
* ``NoUniqueBeanDefinitionException``
* Add ``beanId`` to ``@Qualifier``

````java
@Component
public class TennisCoach implements Coach {
  @Autowired
  @Qualifier("happyFortuneService")
  private FortuneService fortuneService;
}
````

# Bean Scopes with Annotations
* We have already used scopes with XML declarations
* We can do it using ``@Scope("singleton")`` or ``@Scope("prototype")`` in the Java class that implements interface.

# Bean lifecycle methods / hooks
* Remember, we can add custom code during *bean initialization* and *bean destruction*.
* We can do it using ``@PostConstruct`` or ``@PreDestroy``.

````java
@Component
public class TennisCoach implements Coach {
    
    @PostConstruct
    public void doMyStartupStuff() {
        ...
    }

    @PreDestroy
    public void doMyCleanupStuff() {
        ...
    }
}
````

# Configure a Spring container using Java instead of XML
There are 3 ways of configuring a Spring container
1. Full XML config
2. XML Component Scan (making the XML a little smaller)
3. Java Configuration Class with ``@Configuration``

Step by Step:
1. Create a Java class with ``@Configuration``
2. Add component scan (optional)
3. Read Spring Java config class
4. Retrieve bean from Spring container

Create a Java class with ``@Configuration``

````java
@Configuration
public class SprotConfig {
    
}
````

Add component scan (optional)

````java
@Configuration
@ComponentScan("com.dstoecklein.springdemo")
public class SprotConfig {

}
````

Read Spring Java config class
````java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SportConfig.class);
````

Retrieve bean from Spring container
````java
Coach theCoach = context.getBean("tennisCoach", Coach.class);
````

# Defining Beans using Java instead of XML

````java
public class SwimCoach implements Coach {
}
````

Step by Step:
1. Define method to expose the bean
2. Injection bean dependencies
3. Read Spring Java config class
4. Retrieve bean from Spring container

Define method to expose the bean. Create new instance of ``SwimCoach`` inside our Config. The method name ``swimCoach`` will be the ``beanId``. No ``component Scan`` required.
````java
@Configuration
public class SportConfig {

    @Bean
    public Coach swimCoach() {
        SwimCoach mySwimCoach = new SwimCoach();
        return mySwimCoach;
    }
}
````

Injection bean dependencies. We know our Coach need ``Fortune`` dependency. So how do we pull that in?

We define a ``bean`` for ``FortuneService`` that returns a new instance of ``happyFortuneService``. Again, the method name will be the ``beanId``. Next we inject that dependency to ``mySwimCoach``.
````java
@Configuration
public class SportConfig {

    @Bean
    public FortuneService happyFortuneService() {
        return new HappyFortuneService();
    }

    @Bean
    public Coach swimCoach() {
        SwimCoach mySwimCoach = new SwimCoach(happyFortuneService());
        return mySwimCoach;
    }
}
````

Read Spring Java config class
````java
AnnotationConfigApplicationContext context = new AnnotationConfigApplicationContext(SportConfig.class);
````

Retrieve bean from Spring container
````java
Coach theCoach = context.getBean("swimCoach", Coach.class);
````

# Inject values from properties file

Step by Step:
1. Create properties file
2. Load properties file in Spring config
3. Reference values from properties file

Create properties file
````text
foo.email=example@team.com
foo.team=exampleTeam
````

Load properties file in Spring config

````java
@Configuration
@PropertySource("classpath:sport.properties")
public class SportConfig {
    ...
}
````

Reference values from properties file
````java
public class SwimCoach implements Coach {
    @Value("${foo.email}")
    private String email;

    @Value("${foo.team}")
    private String team;
}
````
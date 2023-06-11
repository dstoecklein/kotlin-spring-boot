# 1. Spring Bean
A Spring Bean is simply a Java object. When Java objects are created by the Spring Container, then Spring refers to them as "Spring Beans".Spring Beans are created from normal Java classes ... just like Java objects.

> In summary, whenever you see "Spring Bean", just think Java object.

# 2. Spring IoC Container (Inversion of Control)
Responsible for managing the objects (beans) of an application. It uses dependency injection to achieve inversion of control. ``BeanFactory`` and `ApplicationContext` represent the Spring IoC container. `BeanFactory` is the root interface for accessing the Spring container. `ApplicationContext` is a sub-interface of `BeanFactory`.
* Create and manage objects (Inversion of control)
* Tell ``BeanFactory`` to give me an object
* Spring automatically creates objects based on configurations
  * XML (legacy)
  * Java Annotations (modern)
  * Java Source Code (modern)
* Inject objects dependencies (Dependency injection)

# 3. Spring Development Process
1. Configure Spring Beans
2. Create a Spring Container
3. Retrieve Beans from Spring Container

## 3.1 Example (XML-based configuration)
``applicationContext.xml``
```XML
<beans>
    <bean id="myCoach" class="com.dstoecklein.springdemo.BaseballCoach"></bean>
</beans>
```

Creating a Spring Container. Note that `ClassPathXmlApplicationContext` is used to create the container. This loads an XML file from the classpath. There are more types: `AnnotationConfigApplicationContext`, `AnnotationConfigWebApplicationContext`, ...
```java
ClassPathXmlApplicationContext context = new ClassPathXmlApplicationContext("applicationContext.xml");
```

Retrieve Beans from container
```java
Coach theCoach = context.getBean("myCoach", Coach.class);
```

# 4. Dependency Injection
* A ``car`` object has multiple dependencies such as ``engine`` etc.
* A car factory (Spring object factory) assembles the car with its dependencies
* **Injection types**
  * Constructor injection
  * Setter injection
  * Auto-Wiring via annotations

## 4.1 Constructor injection
1. Define the dependency interface and class
2. Create a constructor in your class for injections
3. Configure the dependency injection in Spring config file

Define the dependency interface and class
```java
public interface FortuneService {
    public String getFortune();
}

public class HappyFortuneService implements FortuneService {
    public String getFortune() {
        return "Today is your lucky day!";
    }
}
```

Create a constructor in your class for injections. A constructor that accepts dependencies.
```java
public class BaseballCoach implements Coach {
    private FortuneService fortuneService;
    
    public BaseballCoach(FortuneService theFortuneService) {
        fortuneService = theFortuneService;
    }
}
```

Configure the dependency injection in Spring config file
```xml
<bean id="myFortuneService" 
      class="com.dstoecklein.springdemo.HappyFortuneService">
</bean>

<bean id="myCoach" 
      class="com.dstoecklein.springdemo.BaseballCoach">
        <constructor-arg ref="myFortuneService" />
</bean>
```

### 4.1.1 What's happening behind the scenes
* Spring will create objects for all beans 
  * ``HappyFortuneService myFortuneService = new HappyFortuneService();``
  * ``BaseballCoach myCoach = new BaseballCoach(myFortuneService);``

## 4.2 Setter injection
1. Create setter methods for dependency injection
2. Configure the dependency injection in Spring config file

Create setter methods for dependency injection

```java
public class CricketCoach implements Coach {
  private FortuneService fortuneService;

  public CricketCoach() {
  }
  
  // this method will be called by Spring for injection
  public void setFortuneService(FortuneService fortuneService) {
      this.fortuneService = fortuneService;
  }
}
```

Configure the dependency injection in Spring config file
```xml
<bean id="myFortuneService" class="com.dstoecklein.springdemo.HappyFortuneService"></bean>

<bean id="myCricketCoach" class="com.dstoecklein.springdemo.CricketCoach">
    <!-- Spring will look for setFortuneService. 
    We must define method name without "set" and with lower case 
    -->   
    <property name="fortuneService" ref="myFortuneService" />
</bean>
```

### 4.2.1 Literal injection
1. Create setter methods for dependency injection
2. Configure the dependency injection in Spring config file

Create setter methods for dependency injection

```java
public class CricketCoach implements Coach {
    private FortuneService fortuneService;
    private String emailAddress;
    private String team;

    // create a no-arg constructor
    public CricketCoach() {
    }

    // create setter methods
    public void setFortuneService(FortuneService fortuneService) {
      this.fortuneService = fortuneService;
    }

    public void setEmailAddress(String emailAddress) {
      this.emailAddress = emailAddress;
    }

    public void setTeam(String team) {
      this.team = team;
    }
}
```

Configure the dependency injection in Spring config file
```xml
<bean id="myCricketCoach" class="com.dstoecklein.springdemo.CricketCoach">
  <!-- set up setter injection -->
  <property name="fortuneService" ref="myFortuneService"/>
  <property name="emailAddress" value="example@team.com"/>
  <property name="team" value="ExampleTeam"/>
</bean>
```

### 4.2.2 Inject values from Properties file
* Problem with literal injection is that values were hardcoded. We want to read these values from a properties file.

1. Create Properties file
2. Load Properties file into Spring config file
3. Reference values from Properties file

Create Properties file
```text
foo.email=example@team.com
foo.team=exampleTeam
```

Load Properties file into Spring config file

```xml
<context:property-placeholder location="classpath:sport.properties"/>
```

Reference values from Properties file
```xml
<bean id="myCricketCoach" class="com.dstoecklein.springdemo.CricketCoach">
  <!-- set up setter injection -->
  <property name="fortuneService" ref="myFortuneService"/>
  <property name="emailAddress" value="${foo.email}"/>
  <property name="team" value="${foo.team}"/>
</bean>
```

# 5. Scopes
* Refers to a lifecycle of a bean
  * How long does the bean live
  * How many instances are created
  * How is the bean shared
* Default scope is ``singleton``
  * Meaning the Spring container only creates 1 instances of a bean

These will reference the same bean:
```java
Coach theCoach = context.getBean("myCoach", Coach.class);
Coach anotherCoach = context.getBean("myCoach", Coach.class);
```

Another scopes are:
* ``prototype``: Creates new bean instance for each container request. Like the ``new`` keyword.
* ``request``: Scopes to an HTTP web request
* ``session``: Scoped to an HTTP web session
* ``global-session``: Scoped to a global HTTP web session

# 6. Bean lifecycle
* Container started -> bean instantiated -> dependency injected -> internal Spring processing -> Your custom init method (optional)

```xml
<bean id="myCoach" class="..." init-method="doMyStartupStuff"></bean>
<bean id="myCoach" class="..." destroy-method="doMyCleanupStuff"></bean>
```

1. Define your methods for init and destroy
2. Configure the method names in Spring config file
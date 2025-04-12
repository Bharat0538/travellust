# TravelLust - Modern Travel Company Website

A modern, responsive travel company website built with Spring Boot, Thymeleaf, Bootstrap 5, and JavaScript. The website features a beautiful design with animations, responsive layout, and modern UI components.

## Features

- Responsive design that works on all devices
- Modern and clean user interface
- Smooth animations and transitions using AOS library
- Mobile-friendly navigation
- Beautiful image galleries
- Interactive tour packages section
- Testimonials section
- Contact form
- Social media integration
- Google Maps integration

## Technologies Used

- Java 21
- Spring Boot 3.3.4
- Thymeleaf
- HTML5
- CSS3
- JavaScript
- Bootstrap 5
- AOS (Animate On Scroll) library
- Font Awesome icons
- Google Fonts

## Prerequisites

To run this application, you need:

- JDK 21
- Maven 3.6+ or Gradle 7+
- Git

## Getting Started

### Clone the repository

```bash
git clone https://github.com/yourusername/travellust.git
cd travellust
```

### Run the application with Maven

```bash
mvn spring-boot:run
```

### Run the application with Gradle

```bash
./gradlew bootRun
```

### Access the application

Open your browser and navigate to:

```
http://localhost:8080/travellust
```

## Project Structure

```
travellust/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── com/
│   │   │       └── travellust/
│   │   │           ├── controller/
│   │   │           └── TravelLustApplication.java
│   │   └── resources/
│   │       ├── static/
│   │       │   ├── css/
│   │       │   ├── js/
│   │       │   └── images/
│   │       ├── templates/
│   │       │   ├── fragments/
│   │       │   ├── layout/
│   │       │   ├── index.html
│   │       │   ├── destinations.html
│   │       │   ├── packages.html
│   │       │   ├── about.html
│   │       │   ├── testimonials.html
│   │       │   └── contact.html
│   │       └── application.properties
│   └── test/
└── pom.xml
```

## Future Enhancements

- User login/registration system
- Booking system
- Dynamic packages from database
- Admin panel to update plans
- Payment gateway integration

## Browser Compatibility

The website is compatible with all modern browsers:
- Chrome (latest)
- Firefox (latest)
- Safari (latest)
- Edge (latest)

## Credits

- Images: Unsplash
- Icons: Font Awesome
- Fonts: Google Fonts
- Animation: AOS Library

## License

This project is licensed under the MIT License - see the LICENSE file for details. 
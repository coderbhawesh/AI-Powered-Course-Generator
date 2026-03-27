# AI-Powered Course Generator

An intelligent backend system that generates structured learning courses using AI.
Built with Spring Boot, MongoDB, and integrated with LLM APIs (Gemini/OpenAI).

---

## Features

* Generate complete courses from a single topic
* Structured output: Modules → Lessons
* REST APIs using Spring Boot
* AI-powered content generation
* Persistent storage with MongoDB

---

## Tech Stack

* Backend: Spring Boot (Java)
* Database: MongoDB
* AI Integration: Gemini / OpenAI API
* Build Tool: Maven

---

## Project Structure

```
src/
 ├── controller/
 ├── service/
 ├── repository/
 ├── model/
 └── dto/
```

---

## Setup Instructions

### 1. Clone the repository

```
git clone https://github.com/coderbhawesh/AI-Powered-Course-Generator.git
cd AI-Powered-Course-Generator
```

---

### 2. Configure application.properties

```
spring.data.mongodb.uri=mongodb://localhost:27017/course_db
ai.api.key=YOUR_API_KEY
```

---

### 3. Run MongoDB

```
brew services start mongodb-community
```

or

```
docker run -d -p 27017:27017 --name mongo mongo
```

---

### 4. Run the application

```
mvn spring-boot:run
```

---

## API Endpoint

### Generate Course

```
POST /api/courses/generate
```

### Request

```
{
  "topic": "Data Structures"
}
```

---

## Output

```
{
  "title": "",
  "description": "",
  "modules": [
    {
      "title": "",
      "lessons": ["", ""]
    }
  ]
}
```

---

## How It Works

1. User sends a topic
2. Backend sends prompt to LLM
3. AI generates structured course content
4. Response is parsed into DTOs
5. Stored in MongoDB
6. Returned via API

---

## Future Enhancements

* Personalized learning paths
* Progress tracking
* Difficulty levels
* Frontend integration

---

## Author

Bhawesh Pandit

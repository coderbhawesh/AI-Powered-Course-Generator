# AI Course Generator

AI Course Generator is a full-stack learning assistant that turns a topic into a structured course, suggests related YouTube videos, and lets users download the generated course as a PDF.

The project includes:
- a Spring Boot backend for course generation, persistence, PDF export, and YouTube recommendations
- a React frontend with a chat-style interface for generating and viewing courses

## What It Does

- Generates a course from a single topic prompt
- Structures the result into modules and lessons
- Stores generated courses in MongoDB
- Returns YouTube recommendations alongside the course response
- Exports a generated course as a PDF
- Provides a frontend interface for interacting with the APIs

## Tech Stack

### Backend
- Java 21
- Spring Boot
- Spring Security
- Spring Data MongoDB
- Maven
- Gemini API
- YouTube Data API
- iText PDF

### Frontend
- React
- Vite
- JavaScript

## Repository Structure

```text
.
├── frontend/                 # React frontend
├── src/                      # Spring Boot backend source
│   ├── main/
│   └── test/
├── .mvn/                     # Maven wrapper files
├── mvnw
├── mvnw.cmd
├── pom.xml
└── README.md
```

## Core Backend Endpoints

### Generate a Course

```http
POST /v1/api/course/generate
Content-Type: application/json
```

Request:

```json
{
  "topic": "Java Basics"
}
```

Sample response shape:

```json
{
  "id": "course-id",
  "title": "Java Basics",
  "description": "A beginner-friendly introduction to Java.",
  "modules": [
    {
      "id": "module-id",
      "title": "Introduction to Java",
      "lessons": [
        {
          "id": "lesson-id",
          "title": "What is Java?"
        }
      ]
    }
  ],
  "youtubeRecommendations": [
    {
      "videoId": "abc123",
      "title": "Java Full Course",
      "description": "Video description",
      "thumbnailUrl": "https://...",
      "videoUrl": "https://www.youtube.com/watch?v=abc123",
      "channelTitle": "Channel Name"
    }
  ]
}
```

### Get a Saved Course

```http
GET /v1/api/course/{id}
```

### Get YouTube Recommendations by Course Name

```http
GET /v1/api/course/youtube-recommendations?courseName=Java%20Basics&maxResults=5
```

### Get YouTube Recommendations by Course ID

```http
GET /v1/api/course/{id}/youtube-recommendations?maxResults=5
```

### Download Course PDF

```http
GET /courses/{id}/pdf
```

## How It Works

1. A user enters a learning topic in the frontend or calls the backend API directly.
2. The backend sends a prompt to Gemini to generate a structured course.
3. The generated content is parsed into course, module, and lesson objects.
4. The course is saved in MongoDB.
5. Related YouTube videos are fetched using the generated course title.
6. The final course response is returned with optional video recommendations.
7. The user can download the saved course as a PDF.

## Configuration

The project is set up so secrets do not need to be committed to Git.

### Backend

The backend reads its main configuration from:

- [`src/main/resources/application.properties`](/Users/bhaweshkumarpandit/Documents/hackathon/src/main/resources/application.properties)

For local development, secrets can be placed in:

- `src/main/resources/application-secrets.properties`

That file is ignored by Git.

You can also provide values through environment variables:

```bash
export GEMINI_API_KEY="your_gemini_api_key"
export YOUTUBE_API_KEY="your_youtube_api_key"
export GOOGLE_CLIENT_ID="your_google_client_id"
export JWT_SECRET="your_jwt_secret"
export MONGODB_URI="mongodb://localhost:27017/lesson"
```

### Frontend

Create a local frontend env file from:

```bash
cp frontend/.env.example frontend/.env
```

Set:

```env
VITE_API_BASE_URL=http://localhost:8080
```

## Running the Project Locally

### 1. Start MongoDB

Example with Docker:

```bash
docker run -d -p 27017:27017 --name mongo mongo
```

### 2. Run the Backend

From the project root:

```bash
./mvnw spring-boot:run
```

The backend runs on:

```text
http://localhost:8080
```

### 3. Run the Frontend

From the `frontend` directory:

```bash
npm install
npm run dev
```

The frontend usually runs on:

```text
http://localhost:5173
```

## Testing

To run backend tests:

```bash
./mvnw test
```

To build the frontend:

```bash
cd frontend
npm run build
```

## Deployment Notes

- Keep backend and frontend deployments separate unless you intentionally bundle them behind one server.
- Set production environment variables instead of hardcoding secrets.
- Update the frontend API base URL to point to the deployed backend.
- Make sure MongoDB is reachable from the deployed backend.
- Configure CORS in the backend for your deployed frontend domain if needed.

## Current Status

The repository currently contains:
- Spring Boot backend APIs for course generation, YouTube recommendations, and PDF export
- React frontend with a chat-style course generation flow
- local-only secrets support through an ignored config file

# just checking

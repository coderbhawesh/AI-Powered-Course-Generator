import { useState } from "react";

const API_BASE_URL = import.meta.env.VITE_API_BASE_URL || "http://localhost:8080";

const initialMessage = {
  id: "welcome",
  role: "assistant",
  type: "welcome",
  text: "Tell me what you want to learn, and I'll generate a course with lesson structure, video recommendations, and a PDF download option."
};

function App() {
  const [topic, setTopic] = useState("");
  const [messages, setMessages] = useState([initialMessage]);
  const [loading, setLoading] = useState(false);
  const [error, setError] = useState("");

  const canSubmit = topic.trim().length > 0 && !loading;

  const placeholderExamples = ["Java Basics", "Machine learning for beginners", "DSA interview prep"];

  async function handleSubmit(event) {
    event.preventDefault();
    const trimmedTopic = topic.trim();
    if (!trimmedTopic) {
      return;
    }

    const userMessage = {
      id: crypto.randomUUID(),
      role: "user",
      type: "prompt",
      text: trimmedTopic
    };

    setMessages((current) => [...current, userMessage]);
    setTopic("");
    setError("");
    setLoading(true);

    try {
      const response = await fetch(`${API_BASE_URL}/v1/api/course/generate`, {
        method: "POST",
        headers: {
          "Content-Type": "application/json"
        },
        body: JSON.stringify({ topic: trimmedTopic })
      });

      if (!response.ok) {
        throw new Error(`Request failed with status ${response.status}`);
      }

      const course = await response.json();
      setMessages((current) => [
        ...current,
        {
          id: crypto.randomUUID(),
          role: "assistant",
          type: "course",
          course
        }
      ]);
    } catch (requestError) {
      setError(
        requestError instanceof Error
          ? requestError.message
          : "Something went wrong while generating the course."
      );
    } finally {
      setLoading(false);
    }
  }

  return (
    <div className="app-shell">
      <div className="backdrop backdrop-left" />
      <div className="backdrop backdrop-right" />

      <main className="app-frame">
        <section className="hero-panel">
          <p className="eyebrow">AI Learning Companion</p>
          <h1>Learn with a course plan that feels like a guided conversation.</h1>
          <p className="hero-copy">
            Generate a structured roadmap, browse YouTube recommendations, and export the full
            course outline as a PDF.
          </p>
          <div className="example-row">
            {placeholderExamples.map((example) => (
              <button
                key={example}
                type="button"
                className="example-chip"
                onClick={() => setTopic(example)}
              >
                {example}
              </button>
            ))}
          </div>
        </section>

        <section className="chat-panel">
          <div className="chat-header">
            <div>
              <p className="chat-label">Course Generator</p>
              <h2>Chat-style learning planner</h2>
            </div>
            <span className={`status-pill ${loading ? "is-busy" : ""}`}>
              {loading ? "Generating..." : "Ready"}
            </span>
          </div>

          <div className="message-list">
            {messages.map((message) => (
              <MessageCard key={message.id} message={message} apiBaseUrl={API_BASE_URL} />
            ))}

            {loading ? (
              <div className="message assistant">
                <div className="bubble">
                  <p className="message-role">Assistant</p>
                  <div className="typing-dots" aria-label="Loading">
                    <span />
                    <span />
                    <span />
                  </div>
                </div>
              </div>
            ) : null}
          </div>

          <form className="composer" onSubmit={handleSubmit}>
            <label className="composer-label" htmlFor="topic-input">
              What do you want to learn?
            </label>
            <div className="composer-row">
              <textarea
                id="topic-input"
                className="composer-input"
                value={topic}
                onChange={(event) => setTopic(event.target.value)}
                placeholder="Ask for a topic like 'Java fundamentals for backend development'"
                rows={3}
              />
              <button className="submit-button" type="submit" disabled={!canSubmit}>
                Generate Course
              </button>
            </div>
            {error ? <p className="error-text">{error}</p> : null}
          </form>
        </section>
      </main>
    </div>
  );
}

function MessageCard({ message, apiBaseUrl }) {
  if (message.role === "user") {
    return (
      <div className="message user">
        <div className="bubble">
          <p className="message-role">You</p>
          <p>{message.text}</p>
        </div>
      </div>
    );
  }

  if (message.type === "course") {
    const { course } = message;
    return (
      <div className="message assistant">
        <div className="bubble bubble-course">
          <p className="message-role">Assistant</p>
          <CourseCard course={course} apiBaseUrl={apiBaseUrl} />
        </div>
      </div>
    );
  }

  return (
    <div className="message assistant">
      <div className="bubble">
        <p className="message-role">Assistant</p>
        <p>{message.text}</p>
      </div>
    </div>
  );
}

function CourseCard({ course, apiBaseUrl }) {
  const modules = Array.isArray(course.modules) ? course.modules : [];
  const recommendations = Array.isArray(course.youtubeRecommendations)
    ? course.youtubeRecommendations
    : [];
  const pdfUrl = course.id ? `${apiBaseUrl}/courses/${course.id}/pdf` : "";

  return (
    <article className="course-card">
      <div className="course-topbar">
        <div>
          <h3>{course.title || "Generated Course"}</h3>
          <p className="course-description">
            {course.description || "A structured roadmap generated from your topic."}
          </p>
        </div>
        {pdfUrl ? (
          <a className="pdf-button" href={pdfUrl} target="_blank" rel="noreferrer">
            Download PDF
          </a>
        ) : null}
      </div>

      <section className="course-section">
        <div className="section-heading">
          <h4>Learning Path</h4>
          <span>{modules.length} modules</span>
        </div>
        <div className="module-grid">
          {modules.map((module, index) => (
            <div key={module.id || `${module.title}-${index}`} className="module-card">
              <div className="module-index">Module {index + 1}</div>
              <h5>{module.title}</h5>
              <ul>
                {(module.lessons || []).map((lesson, lessonIndex) => (
                  <li key={lesson.id || `${lesson.title}-${lessonIndex}`}>{lesson.title}</li>
                ))}
              </ul>
            </div>
          ))}
        </div>
      </section>

      <section className="course-section">
        <div className="section-heading">
          <h4>YouTube Recommendations</h4>
          <span>{recommendations.length} videos</span>
        </div>
        {recommendations.length ? (
          <div className="video-grid">
            {recommendations.map((video) => (
              <a
                key={video.videoId || video.videoUrl}
                className="video-card"
                href={video.videoUrl}
                target="_blank"
                rel="noreferrer"
              >
                <img src={video.thumbnailUrl} alt={video.title} />
                <div className="video-copy">
                  <h5>{video.title}</h5>
                  <p>{video.channelTitle}</p>
                </div>
              </a>
            ))}
          </div>
        ) : (
          <div className="empty-state">
            <p>No video recommendations are available for this course yet.</p>
          </div>
        )}
      </section>
    </article>
  );
}

export default App;

# WebFML 2.0 - Modern FAMILIAR Web IDE

**WebFML** is a modern web-based IDE for the FAMILIAR feature modeling language, completely rebuilt with cutting-edge technologies.

## 🚀 Tech Stack Modernization

### Backend (Java/Spring Boot 3.x)
- **Spring Boot 3.2.1** - Modern, production-ready framework
- **Java 21** - Latest LTS version with modern language features
- **Maven** - Dependency management
- **Session Management** - Stateful interpreter per user
- **REST API** - Clean, RESTful endpoints

### Frontend (React + TypeScript)
- **React 18** - Latest React with concurrent features
- **TypeScript** - Type safety and better developer experience
- **Vite** - Lightning-fast build tool (replaces Gulp/Webpack)
- **Monaco Editor** - Professional code editor (VSCode engine, replaces ACE)
- **TailwindCSS** - Modern utility-first CSS framework
- **Tanstack Query** - Powerful data fetching and caching
- **Zustand** - Lightweight state management

### Removed Outdated Technologies
- ❌ Play Framework 2.2 → ✅ Spring Boot 3.x
- ❌ Scala 2.10 → ✅ Java 21
- ❌ AngularJS 1.4 → ✅ React 18 + TypeScript
- ❌ Bower → ✅ npm/pnpm
- ❌ Gulp 3 → ✅ Vite
- ❌ ACE Editor → ✅ Monaco Editor
- ❌ jQuery → ✅ Modern React patterns
- ❌ JSHint → ✅ ESLint + Prettier

## 📁 New Project Structure

```
webfml/
├── backend/                          # Spring Boot backend
│   ├── src/main/java/
│   │   └── fr/inria/familiar/webfml/
│   │       ├── WebFmlApplication.java      # Main application
│   │       ├── controller/                 # REST controllers
│   │       │   ├── FamiliarController.java
│   │       │   └── WorkspaceController.java
│   │       ├── service/                    # Business logic
│   │       │   ├── FamiliarInterpreterService.java
│   │       │   └── WorkspaceService.java
│   │       └── dto/                        # Data transfer objects
│   ├── src/main/resources/
│   │   └── application.yml                 # Configuration
│   └── pom.xml                             # Maven dependencies
│
├── frontend/                         # React + TypeScript frontend
│   ├── src/
│   │   ├── components/                     # React components
│   │   │   ├── Editor.tsx                  # Monaco code editor
│   │   │   ├── Console.tsx                 # Interactive console
│   │   │   └── Toolbar.tsx                 # Toolbar with actions
│   │   ├── api/
│   │   │   └── client.ts                   # API client (Axios)
│   │   ├── App.tsx                         # Main app component
│   │   └── main.tsx                        # Entry point
│   ├── package.json                        # npm dependencies
│   ├── vite.config.ts                      # Vite configuration
│   └── tsconfig.json                       # TypeScript configuration
│
└── README-NEW.md                     # This file
```

## 🛠️ Development Setup

### Prerequisites

- **Java 21** or later ([Download](https://adoptium.net/))
- **Node.js 20** or later ([Download](https://nodejs.org/))
- **Maven 3.8+** ([Download](https://maven.apache.org/))
- **FAMILIAR dependencies** (see original README.md)

### Backend Setup

```bash
cd backend

# Build the project
mvn clean install

# Run the application
mvn spring-boot:run

# Backend will run on http://localhost:8080
```

### Frontend Setup

```bash
cd frontend

# Install dependencies
npm install

# Start development server
npm run dev

# Frontend will run on http://localhost:5173
```

### Full Stack Development

1. Start the backend:
   ```bash
   cd backend && mvn spring-boot:run
   ```

2. In a new terminal, start the frontend:
   ```bash
   cd frontend && npm run dev
   ```

3. Open your browser to `http://localhost:5173`

## 🏗️ Building for Production

### Backend

```bash
cd backend
mvn clean package

# Run the JAR
java -jar target/webfml-backend-2.0.0.jar
```

### Frontend

```bash
cd frontend
npm run build

# Static files will be in frontend/dist/
# Serve with any static file server or integrate with backend
```

## 🔑 Key Features

### Modern Code Editor
- **Monaco Editor** - The same editor used in VS Code
- Syntax highlighting for FAMILIAR language
- Code completion and IntelliSense
- Keyboard shortcuts (Ctrl+Enter to run)
- Line numbers and minimap

### Interactive Console
- Real-time command execution
- Command history (up/down arrows)
- Error highlighting
- Variable tracking
- Clear and reset functionality

### Workspace Management
- File tree navigation
- Create/delete files and folders
- Load and save scripts
- Support for .fml and .dimacs files

### Session Management
- Per-user interpreter isolation
- Persistent variables during session
- Automatic session cleanup

## 🔧 Configuration

### Backend Configuration (`backend/src/main/resources/application.yml`)

```yaml
server:
  port: 8080

webfml:
  workspace:
    base-path: ${user.home}/.webfml/workspace

cors:
  allowed-origins: http://localhost:5173
```

### Frontend Configuration (`frontend/vite.config.ts`)

```typescript
server: {
  port: 5173,
  proxy: {
    '/api': {
      target: 'http://localhost:8080',
      changeOrigin: true,
    },
  },
}
```

## 🐳 Docker Deployment (Coming Soon)

```bash
# Build and run with Docker Compose
docker-compose up -d
```

## 📊 API Endpoints

### FAMILIAR Interpreter

- `POST /api/familiar/interpret` - Execute FAMILIAR command (with reset)
- `POST /api/familiar/eval-prompt` - Evaluate prompt (without reset)
- `GET /api/familiar/variable/{id}` - Get variable value
- `GET /api/familiar/variables` - Get all variable IDs
- `POST /api/familiar/reset` - Reset interpreter
- `GET /api/familiar/keywords` - Get FAMILIAR keywords

### Workspace Management

- `GET /api/workspace/files` - List all files
- `GET /api/workspace/file?filename=...` - Load file content
- `POST /api/workspace/file?filename=...` - Save file content
- `POST /api/workspace/file/create?name=...` - Create new file
- `DELETE /api/workspace/file?name=...` - Delete file
- `POST /api/workspace/folder?name=...` - Create folder
- `DELETE /api/workspace/folder?name=...` - Delete folder

## 🧪 Testing

### Backend Tests
```bash
cd backend
mvn test
```

### Frontend Tests
```bash
cd frontend
npm test
```

## 📝 Migration Notes

### FAMILIAR Integration
- Updated to FAMILIAR 1.2 (from 1.0.10)
- KSynthesis integration maintained
- Session-based interpreter management
- All core FAMILIAR operations supported

### Breaking Changes from Old Version
- URL structure changed (now under `/api` prefix)
- Session management is now handled by Spring Session
- File paths are relative to configured workspace
- JSON response format slightly different (standardized)

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Make your changes
4. Run tests and linting
5. Submit a pull request

## 📄 License

[Same as original WebFML project]

## 🙏 Acknowledgments

- Original WebFML project team
- FAMILIAR language developers
- Spring Boot and React communities

## 📚 Additional Resources

- [FAMILIAR Project](https://familiar-project.github.io/)
- [FAMILIAR Documentation](https://github.com/FAMILIAR-project/familiar-language)
- [Spring Boot Documentation](https://spring.io/projects/spring-boot)
- [React Documentation](https://react.dev/)
- [Vite Documentation](https://vitejs.dev/)
- [Monaco Editor](https://microsoft.github.io/monaco-editor/)

---

**Sources:**
- [FAMILIAR project](https://familiar-project.github.io/)
- [FAMILIAR-project GitHub](https://github.com/FAMILIAR-project)
- [FAMILIAR language repository](https://github.com/FAMILIAR-project/familiar-language)

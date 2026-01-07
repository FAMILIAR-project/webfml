# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

WebFML is a web-based IDE for the FAMILIAR feature modeling language. It provides a Monaco-based code editor and interactive console for writing and executing FAMILIAR scripts.

## Tech Stack

- **Backend**: Spring Boot 3.2.1, Java 21, Maven
- **Frontend**: React 18, TypeScript, Vite 5, TailwindCSS
- **Core Dependencies**: FAMILIAR-Standalone 1.2, KSynthesis

## Common Commands

### Backend (from `backend/` directory)
```bash
mvn spring-boot:run          # Start dev server on :8080
mvn clean package            # Build production JAR
mvn test                     # Run tests
```

### Frontend (from `frontend/` directory)
```bash
npm install                  # Install dependencies
npm run dev                  # Start dev server on :5173
npm run build                # Build for production (outputs to dist/)
npm run lint                 # ESLint check
npm run format               # Prettier formatting
```

### Docker (full stack)
```bash
docker-compose up -d         # Start both services (frontend on :80, backend on :8080)
```

## Architecture

```
webfml/
├── backend/                 # Spring Boot REST API
│   └── src/main/java/fr/inria/familiar/webfml/
│       ├── controller/      # REST endpoints (FamiliarController, WorkspaceController)
│       ├── service/         # Business logic (FamiliarInterpreterService, WorkspaceService)
│       └── dto/             # Data transfer objects
└── frontend/                # React SPA
    └── src/
        ├── components/      # Editor.tsx (Monaco), Console.tsx, Toolbar.tsx
        └── api/client.ts    # Axios API client with TypeScript types
```

**Key architectural patterns:**
- Session-based interpreter: Each user gets an isolated FAMILIAR interpreter instance (managed via `ConcurrentHashMap` in `FamiliarInterpreterService`)
- API proxy: Vite dev server proxies `/api/*` requests to backend on port 8080
- CORS is configured for `localhost:5173` in development

## API Endpoints

### FAMILIAR Interpreter (`/api/familiar/`)
- `POST /interpret` - Execute FAMILIAR command (with reset)
- `POST /eval-prompt` - Evaluate command (without reset)
- `POST /reset` - Reset interpreter state
- `GET /variables` - Get all variable IDs
- `GET /variable/{id}` - Get specific variable value
- `GET /keywords` - Get FAMILIAR language keywords

### Workspace (`/api/workspace/`)
- `GET /files` - List files
- `GET /file?filename=...` - Load file content
- `POST /file?filename=...` - Save file content

## Development Workflow

1. Start backend: `cd backend && mvn spring-boot:run`
2. Start frontend: `cd frontend && npm run dev`
3. Open browser at `http://localhost:5173`

The frontend proxies API calls to the backend automatically.

## FAMILIAR Dependencies

Building requires FAMILIAR-Standalone and KSynthesis to be installed in local Maven repository:
```bash
# FAMILIAR
git clone git@github.com:FAMILIAR-project/familiar-language.git
cd familiar-language && mvn clean install -DskipTests

# KSynthesis
git clone https://github.com/gbecan/FOReverSE-KSynthesis.git
cd KSynthesis && mvn clean install
```

## Code Style

- Frontend uses Prettier (single quotes, no semicolons, 100 char line width)
- Frontend uses ESLint with TypeScript strict mode
- Path alias `@/` maps to `src/` in frontend imports

# Migration Guide: WebFML 1.0 → 2.0

This guide explains how to migrate from the old Play Framework + AngularJS stack to the new Spring Boot + React stack.

## Overview of Changes

### Backend Migration (Play → Spring Boot)

| Old (Play Framework 2.2) | New (Spring Boot 3.x) |
|--------------------------|------------------------|
| Scala controllers | Java REST controllers |
| Play routes | Spring MVC annotations |
| Play sessions | Spring Session |
| SBT build | Maven build |
| Scala 2.10 | Java 21 |

### Frontend Migration (AngularJS → React)

| Old (AngularJS 1.4) | New (React 18) |
|---------------------|----------------|
| AngularJS controllers | React components + hooks |
| $scope | React state (useState) |
| $http | Axios + Tanstack Query |
| ACE Editor | Monaco Editor |
| Bower | npm/pnpm |
| Gulp | Vite |
| jQuery | Modern JavaScript |

## Step-by-Step Migration

### Phase 1: Setup New Project Structure

1. **Create new directories:**
   ```bash
   mkdir -p backend/src/main/java
   mkdir -p frontend/src
   ```

2. **Copy FAMILIAR dependencies:**
   - The `lib/` folder JARs are now managed through Maven
   - Update `backend/pom.xml` with FAMILIAR dependencies

### Phase 2: Backend Migration

#### Controller Migration Example

**Old Play Controller (Scala):**
```scala
object WebFMLInterpreter extends Controller {
  def interpret = Action { request =>
    val body = request.body.asJson
    // ... FAMILIAR logic
    Ok(Json.toJson(result))
  }
}
```

**New Spring Controller (Java):**
```java
@RestController
@RequestMapping("/familiar")
public class FamiliarController {
    @PostMapping("/interpret")
    public ResponseEntity<?> interpret(@RequestBody InterpretRequest request) {
        // ... FAMILIAR logic
        return ResponseEntity.ok(response);
    }
}
```

#### Session Management Migration

**Old (Play):**
```scala
val interp = FamiliarIDEController.mkInterpreter(request.session)
```

**New (Spring):**
```java
FMLBasicInterpreter interpreter = interpreterService.getInterpreter(session.getId());
```

### Phase 3: Frontend Migration

#### Component Migration Example

**Old AngularJS Controller:**
```javascript
angular.module('app').controller('EditorCtrl', function($scope, $http) {
    $scope.code = '';
    $scope.run = function() {
        $http.post('/interpret', { command: $scope.code })
            .success(function(data) { /* ... */ });
    };
});
```

**New React Component:**
```typescript
const Editor: React.FC = () => {
    const [code, setCode] = useState('');

    const handleRun = async () => {
        const response = await familiarApi.interpret(code);
        // ... handle response
    };

    return <MonacoEditor value={code} onChange={setCode} />;
};
```

### Phase 4: API Endpoint Mapping

| Old Play Route | New Spring Endpoint |
|----------------|---------------------|
| POST /interpret | POST /api/familiar/interpret |
| POST /evalPrompt/:cmd | POST /api/familiar/eval-prompt?command=... |
| GET /variable/:id | GET /api/familiar/variable/{id} |
| POST /reset | POST /api/familiar/reset |
| GET /listFiles | GET /api/workspace/files |
| POST /saveAs/:content/:filename | POST /api/workspace/file?filename=... |

### Phase 5: Configuration Migration

**Old (application.conf):**
```hocon
application.secret="changeme"
logger.application=DEBUG
```

**New (application.yml):**
```yaml
spring:
  application:
    name: webfml-backend

logging:
  level:
    fr.inria.familiar: DEBUG
```

## Testing Your Migration

### 1. Test Backend Independently

```bash
cd backend
mvn spring-boot:run

# Test API endpoint
curl http://localhost:8080/api/familiar/keywords
```

### 2. Test Frontend Independently

```bash
cd frontend
npm install
npm run dev

# Open http://localhost:5173
```

### 3. Integration Testing

1. Start both backend and frontend
2. Open browser DevTools
3. Test each feature:
   - [ ] Load file from workspace
   - [ ] Execute FAMILIAR command
   - [ ] Use interactive console
   - [ ] Save file
   - [ ] Reset environment

## Common Issues and Solutions

### Issue 1: FAMILIAR Dependencies Not Found

**Solution:** Ensure FAMILIAR and KSynthesis are installed in your local Maven repository:

```bash
cd /path/to/familiar-language
mvn clean install -DskipTests

cd /path/to/KSynthesis
mvn clean install
```

### Issue 2: CORS Errors in Development

**Solution:** Update `backend/src/main/resources/application.yml`:

```yaml
cors:
  allowed-origins: http://localhost:5173
  allow-credentials: true
```

### Issue 3: Session Not Persisting

**Solution:** Ensure cookies are enabled and check that Spring Session is configured correctly.

### Issue 4: Monaco Editor Not Loading

**Solution:** Check that the Monaco Editor worker files are correctly served:

```bash
# In frontend directory
npm install @monaco-editor/react monaco-editor
```

## Data Migration

### Workspace Files

The workspace structure remains the same. Copy your existing `repository/` folder:

```bash
cp -r old-webfml/repository/ new-webfml/backend/repository/
```

Or configure the path in `application.yml`:

```yaml
webfml:
  workspace:
    base-path: /path/to/existing/repository
```

## Rollback Plan

If you need to rollback to the old version:

1. Keep the old codebase in a separate branch
2. Document any data changes
3. The new backend is stateless, so no database migration needed
4. Simply switch back to the old branch and run:

```bash
# Old version
play run
```

## Performance Improvements

Expected improvements in the new version:

- **Build time:** 10x faster with Vite (vs Gulp)
- **Hot reload:** ~50ms with Vite (vs 2-3s with Play)
- **Bundle size:** ~40% smaller with modern tooling
- **First load:** ~2x faster with code splitting
- **Development experience:** Much improved with TypeScript

## Next Steps

After migration:

1. ✅ Test all FAMILIAR operations
2. ✅ Verify file operations work correctly
3. ✅ Check session management
4. ✅ Test on different browsers
5. ⬜ Set up CI/CD pipeline
6. ⬜ Deploy to production
7. ⬜ Monitor for issues

## Getting Help

- Check the [README-NEW.md](README-NEW.md) for setup instructions
- Review the [FAMILIAR documentation](https://familiar-project.github.io/)
- Open an issue on GitHub

## Conclusion

The migration brings significant improvements in:
- Developer experience
- Performance
- Maintainability
- Modern best practices
- Security

While it requires effort upfront, the long-term benefits are substantial.

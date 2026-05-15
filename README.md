# WonderPets

A Java Swing desktop application that provides symptom-based dietary and nutritional guidance for pets. Enter a symptom, get instant recommendations on what to feed more, feed less, which nutrients to increase, and which to monitor.

---

## Features

- Symptom search with live autocomplete
- Quick-tag buttons for eight common symptoms
- Colour-coded result cards (eat more · eat less · increase nutrients · monitor)
- Admin panel for adding and removing symptom entries
- Twelve built-in symptom records seeded at startup

---

## Project Structure

```
src/
├── main/java/wonderpets/
│   ├── model/
│   │   ├── ModelEvent.java          # Push-MVC event object
│   │   ├── ModelListener.java       # Observer interface
│   │   ├── AbstractModel.java       # Listener registration & notification
│   │   ├── SymptomEntry.java        # Immutable symptom data record
│   │   └── SymptomIndex.java        # Searchable collection of entries
│   ├── view/
│   │   ├── JFrameView.java          # Abstract base Swing view
│   │   ├── SearchView.java          # Main window with search & autocomplete
│   │   ├── ResultView.java          # Four-card result display
│   │   └── AdminView.java           # Login + entry management panel
│   ├── controller/
│   │   ├── AbstractController.java  # Base controller with string dispatch
│   │   ├── SearchController.java    # Handles search and suggest operations
│   │   └── AdminController.java     # Auth-gated add/delete operations
│   ├── util/
│   │   └── SeedData.java            # Hardcoded startup symptom records
│   └── Main.java                    # Application entry point
└── test/java/test/
    └── SymptomIndexTest.java        # JUnit 5 tests for SymptomIndex
```

---

## Requirements

- Java 17 or later
- Gradle 8.15 or later (wrapper included)

> **Note:** Gradle 8.14 and below do not support Java 25. If you are running Java 25, wait for Gradle 8.15+ or compile directly — see [Running without Gradle](#running-without-gradle).

---

## Running the App

```bat
.\gradlew run
```

The main search window opens automatically. Type a symptom name or click a quick-tag button to see dietary recommendations.

---

## Running the Tests

```bat
.\gradlew test
```

Test reports are written to `build/reports/tests/test/index.html`.

---

## Running without Gradle

If Gradle is unavailable or incompatible with your JDK, compile and run directly:

```bat
:: Compile
for /r src\main\java %f in (*.java) do javac -d out "%f"

:: Run
java -cp out wonderpets.Main
```

On Unix/macOS:

```sh
find src/main/java -name "*.java" | xargs javac -d out
java -cp out wonderpets.Main
```

---

## Admin Panel

Click **Admin Panel** in the bottom-right corner of the search window.

| Field    | Value      |
|----------|------------|
| Password | `admin123` |

Once logged in you can:
- **Add** a new symptom entry by filling in the form fields (comma-separated lists for the nutrition fields) and clicking **Add**
- **Delete** an existing entry using the Delete button in the table
- **Logout** using the button in the top bar

---

## Development Notes

- All AI-assisted code generation prompts for this project are logged in [`ctxt/PROMPTS.txt`](ctxt/PROMPTS.txt).
- The push-MVC pattern is used throughout: models notify views via `ModelListener`/`ModelEvent`; views never query the model directly after construction.
- `CopyOnWriteArrayList` in `AbstractModel` makes listener iteration safe under concurrent add/remove.
- `SymptomEntry` is fully immutable — all list fields are defensively copied on input and on access.

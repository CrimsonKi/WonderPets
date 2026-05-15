# Software Design Document

**Group:** WonderPets  
**Project:** Symptom-Based Dietary Recommendation Tool  
**Version:** 1.0  

---

## 1. Architecture Overview

WonderPets follows the **push-MVC (Model–View–Controller)** pattern. The model notifies views of changes by pushing events through a listener interface, so views never poll the model directly.

```
┌─────────────┐   notifyChanged()   ┌─────────────┐
│    Model    │ ──────────────────► │    View     │
│             │                     │             │
│ SymptomIndex│ ◄────────────────── │ SearchView  │
│             │   user actions via  │ ResultView  │
│             │   Controller        │ AdminView   │
└─────────────┘                     └─────────────┘
       ▲                                   │
       │        ┌─────────────┐            │
       └──────► │ Controller  │ ◄──────────┘
                │             │
                │ SearchCtrl  │
                │ AdminCtrl   │
                └─────────────┘
```

---

## 2. Package Structure

```
wonderpets/
├── model/
│   ├── ModelEvent.java          Event object carrying the changed model
│   ├── ModelListener.java       Observer interface (modelChanged)
│   ├── AbstractModel.java       Manages listeners; fires notifyChanged()
│   ├── SymptomEntry.java        Immutable data record for one symptom
│   └── SymptomIndex.java        Searchable list of SymptomEntry objects
├── view/
│   ├── JFrameView.java          Abstract JFrame + ModelListener base
│   ├── SearchView.java          Main window with search and autocomplete
│   ├── ResultView.java          Four-card result display
│   └── AdminView.java           Login panel + entry management table
├── controller/
│   ├── AbstractController.java  Holds model + view refs; string dispatch
│   ├── SearchController.java    handleSearch / handleSuggest
│   └── AdminController.java     authenticate / addSymptom / deleteSymptom
├── util/
│   └── SeedData.java            Populates index with 12 built-in entries
└── Main.java                    Entry point; wires all components together
```

---

## 3. Class Design

### 3.1 Model Layer

**`SymptomEntry`** — immutable value object  
- Fields: `symptom`, `eatMore`, `eatLess`, `increaseNutrients`, `monitorNutrients`  
- All list fields defensively copied on construction and on access  
- Validates that `symptom` is non-null and non-blank  

**`SymptomIndex`** — extends `AbstractModel`  
- Maintains an `ArrayList<SymptomEntry>`  
- `search(String)` — case-insensitive exact match, returns entry or null  
- `autoSuggest(String)` — case-insensitive prefix match, returns list  
- `addEntry` / `removeEntry` — mutate and call `notifyChanged()`  

**`AbstractModel`**  
- Holds a `CopyOnWriteArrayList<ModelListener>` — safe for concurrent add/remove during iteration  
- `addModelListener` uses `addIfAbsent` to prevent duplicates  

### 3.2 View Layer

**`JFrameView`** — abstract base  
- Registers itself as a `ModelListener` on construction  
- `modelChanged()` dispatches `refresh()` onto the EDT via `SwingUtilities.invokeLater` if not already on it  
- Subclasses implement `refresh()` to redraw from the model  

**`SearchView`**  
- `JTextField` + `JButton` for search input  
- `JPopupMenu` containing a `JList<String>` for autocomplete suggestions  
- Row of quick-tag `JButton`s for common symptoms  
- Opens `ResultView` on a successful search; shows `JOptionPane` for no-match  

**`ResultView`**  
- Four `JPanel` cards arranged in a 2×2 `GridLayout`  
- Colours: green (eat more), red (eat less), blue (increase nutrients), yellow (monitor)  
- Each card has a `TitledBorder` and a `JList` of items  
- `DISPOSE_ON_CLOSE` — closing a result window does not exit the app  

**`AdminView`**  
- `CardLayout` switches between a login card and an admin card  
- Login card: `JPasswordField`, error label  
- Admin card: five-field form (symptom + four CSV lists), `JTable` with a Delete button column  
- `refresh()` rebuilds the table from `index.getAll()` when the model changes  

### 3.3 Controller Layer

**`SearchController`**  
- `handleSearch(String)` — normalises input, calls `index.search()`, stores last query  
- `handleSuggest(String)` — trims input, delegates to `index.autoSuggest()`  
- Tracks `lastQuery` and `searchPerformed` so views can distinguish no-query from no-result  

**`AdminController`**  
- `authenticate(String)` — checks against `ADMIN_PASS = "admin123"`  
- `addSymptom(SymptomEntry)` — auth-gated; rejects duplicates via `index.search()` first  
- `deleteSymptom(String)` — auth-gated; delegates to `index.removeEntry()`  
- `operation("Logout")` — ends the session  

---

## 4. Data Design

Each symptom entry holds five fields. All list fields are stored as unmodifiable `List<String>`.

```
SymptomEntry
├── symptom            : String          "Fatigue"
├── eatMore            : List<String>    ["Spinach", "Lentils", ...]
├── eatLess            : List<String>    ["Sugar", "Caffeine", ...]
├── increaseNutrients  : List<String>    ["Iron", "Vitamin B12", ...]
└── monitorNutrients   : List<String>    ["Vitamin D"]
```

Twelve entries are seeded at startup by `SeedData.seed(SymptomIndex)`:

| Symptom            | Symptom            |
|--------------------|--------------------|
| Fatigue            | Inflammation       |
| Bloating           | Anxiety            |
| Brain fog          | Constipation       |
| Insomnia           | High blood pressure|
| Anaemia            | Joint pain         |
| Skin problems      | Nausea             |

---

## 5. UI Design

### 5.1 Search Window (SearchView)

```
┌────────────────────────────────────────────┐
│               WonderPets                   │
│    Symptom-Based Dietary Guide             │
├────────────────────────────────────────────┤
│  [_______________________________] [Search]│
│  ┌──────────────────────────────┐          │
│  │ Fatigue                      │ ← popup  │
│  │ Bloating                     │          │
│  └──────────────────────────────┘          │
│                                            │
│  [Fatigue][Bloating][Brain fog][Insomnia]  │
│  [Anxiety][Inflammation][Joint pain][Nausea│
│                                            │
│                                 [Admin Panel│
└────────────────────────────────────────────┘
```

### 5.2 Result Window (ResultView)

```
┌────────────────────────────────────────────┐
│           Symptom: Fatigue                 │
├─────────────────┬──────────────────────────┤
│  Eat More 🟢   │  Eat Less 🔴             │
│  • Spinach      │  • Sugar                 │
│  • Lentils      │  • Caffeine              │
│  • Eggs         │  • Processed foods       │
├─────────────────┼──────────────────────────┤
│  Increase 🔵   │  Monitor 🟡              │
│  • Iron         │  • Vitamin D             │
│  • B12          │  • Ferritin              │
│  • Magnesium    │  • Blood glucose         │
├─────────────────┴──────────────────────────┤
│  For educational purposes only. Consult    │
│  a qualified veterinarian.                 │
└────────────────────────────────────────────┘
```

### 5.3 Admin Window (AdminView) — Login Card

```
┌────────────────────────────────┐
│          Admin Login           │
│                                │
│  Password:                     │
│  [________________________]    │
│                                │
│           [ Log In ]           │
│                                │
└────────────────────────────────┘
```

### 5.4 Admin Window (AdminView) — Admin Card

```
┌──────────────────────────────────────────┐
│ WonderPets Admin               [Logout]  │
├──────────────────────────────────────────┤
│ ┌ Add New Symptom ──────────────────┐   │
│ │ Symptom Name:    [______________] │   │
│ │ Eat More (CSV):  [______________] │   │
│ │ Eat Less (CSV):  [______________] │   │
│ │ Increase (CSV):  [______________] │   │
│ │ Monitor (CSV):   [______________] │   │
│ │ [Add]  Status message here        │   │
│ └───────────────────────────────────┘   │
│                                          │
│ Symptom │ Eat More │ Eat Less │ ... │Del │
│─────────┼──────────┼──────────┼─────┼───│
│ Fatigue │ Spinach  │ Sugar    │ ... │[X]│
│ Bloating│ Ginger   │ Beans    │ ... │[X]│
└──────────────────────────────────────────┘
```

---

## 6. Key Design Decisions

| Decision | Rationale |
|---|---|
| Push-MVC over pull-MVC | Views update automatically when the model changes; no polling needed |
| `CopyOnWriteArrayList` for listeners | Listeners can safely add/remove themselves during a `modelChanged` callback |
| `SymptomEntry` immutable | Prevents accidental mutation of index data from outside the model layer |
| `CardLayout` in AdminView | Clean login/admin switch without opening a second window |
| Autocomplete via `JPopupMenu` | Attaches directly to the search field; no third-party library required |
| `DISPOSE_ON_CLOSE` on ResultView | Closing a result card does not terminate the whole application |

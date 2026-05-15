# Project Setup Guide
## VS Code + GitHub + AI Coding Assistant

---

## Original Brief

> "give me step by step instruction on how to create it in vscode as well as save commits to github so it creates a proper network graph in git. also vscode should have an agent to assist with coding and there's a way to cite the prompts you ask though not entirely sure what he meant"

---

## Part 1 — Setting Up VS Code

1. Download and install [Visual Studio Code](https://code.visualstudio.com/)
2. Open VS Code
3. Go to **File → Open Folder** and select your project folder (or create a new empty folder first)
4. Install the **Extension Pack for Java** from the Extensions panel (Ctrl+Shift+X) — this gives you Java language support, debugging, and project management
5. Install the **Gradle for Java** extension for Gradle build support

---

## Part 2 — Connecting to GitHub

### First time only — link VS Code to GitHub
1. Install [Git](https://git-scm.com/downloads) if you don't have it
2. Open a terminal in VS Code (**Terminal → New Terminal**)
3. Set your identity:
   ```
   git config --global user.name "Your Name"
   git config --global user.email "your@email.com"
   ```
4. Go to [github.com](https://github.com) and create a new repository (leave it empty — no README)

### Initialise your project as a Git repo
5. In the VS Code terminal, from your project folder:
   ```
   git init
   git add .
   git commit -m "Initial commit"
   ```
6. Copy the remote URL from GitHub and run:
   ```
   git remote add origin https://github.com/YourUsername/YourRepo.git
   git push -u origin main
   ```

### Making commits (do this regularly as you work)
7. After making changes, save your files, then in the terminal:
   ```
   git add .
   git commit -m "Short description of what you changed"
   git push
   ```
   > **Tip for the network graph:** commit little and often across different days. One big commit at the end produces a flat graph. Small commits over several sessions produce a proper activity graph on GitHub.

---

## Part 3 — AI Coding Assistant (Claude Code)

VS Code has an extension called **Claude Code** that acts as an AI coding agent directly inside your editor.

### Installing Claude Code
1. Open the Extensions panel (Ctrl+Shift+X)
2. Search for **Claude Code**
3. Click Install
4. Sign in with your Anthropic account when prompted

### Using Claude Code
- Open the Claude Code panel from the sidebar or press **Ctrl+Shift+P** and search for Claude
- Type your request in plain English — for example:
  - *"Create a Java class called SymptomEntry with these fields..."*
  - *"Add a search method that is case-insensitive"*
  - *"Write JUnit 5 tests for this class"*
- Claude Code reads your project files, writes code, and can run terminal commands on your behalf
- You can review every change before it is applied

---

## Part 4 — Citing AI-Generated Code (Prompt Logging)

If your course or supervisor requires you to declare AI assistance, the standard approach is to keep a log of every prompt you submitted.

### How this project does it
This project maintains a file at `ctxt/PROMPTS.txt` that records every prompt submitted to the AI assistant during development. This gives a clear, auditable trail of:
- What was asked
- When it was asked
- What the AI was responsible for

### How to set it up in your own project
1. Create a folder called `ctxt/` in your project root
2. Create a file called `PROMPTS.txt` inside it
3. Each time you submit a prompt to an AI tool, paste the prompt into the file with the date:
   ```
   [2026-05-14]
   Create a Java class called SymptomEntry with final fields...

   [2026-05-14]
   Write JUnit 5 tests for SymptomIndex covering search, autoSuggest...
   ```
4. Commit `PROMPTS.txt` to GitHub along with your code so the history is preserved

This satisfies most academic integrity declarations by being transparent about which parts were AI-assisted and what instructions were given.

<div align="center">

# Slang Dictionary

### A Java 21 Swing application to look up a local slang dictionary.

![Static Badge](https://img.shields.io/badge/java-21-orange?logo=java)

</div>

## Takeaways

Main takeaways of this project are:

- Ability to establish, manage, and work in a team. (This doesn't work since he changed it to a solo project instead with how few students there are)
- Ability to analyze and write technical documentation for an application.
- Ability to understand and use basic native Java data structures.
- Ability to write basic syntax in Java.
- Ability to utilize Java IO.
- Ability to utilize Java Collections.
- Ability to manage and handle Exceptions.
- Ability to manage a project structure.
- Ability to analyze the given prompt.
- Ability to separate the given required tasks into smaller actions, and finding the appropriate Java classes to implement.
- Ability to work with an IDE. Recommendations: IntellIJ IDEA, Eclipse, NetBeans.

## Description

This project focuses more on IO, OOP, Generics and Collections.

### Required Tasks

- [x] Must utilize Java IO, OOP, and Collections. The application can use Console, Swing or Java FX.
- [x] Must have a menu to pick an option, regardless of UI choices.
- [x] Must load data from files into objects.
- [ ] Must have a search time under 1s for 100.000 words, even though the provided default file doesn't have that many. (Because of custom pane paints, a query involving 100,000 words exceeds 1s by 0.5s?)
- [x] Must save the current data after modifications.
- [x] Must use a version control system, with at least 10 commits spread across multiple days.

## Features

### Required

**Required feature list**:

- [x] Find by the word.
- [x] Find by the definition.
- [ ] View search history.
- [ ] Add a new word. Notify if duplicated, and allow overwriting or adding new.
- [x] Edit a word.
- [ ] Delete a word, with confirmation.
- [x] Reset back to default list.
- [x] Randomly select a word and display it as the "Word of the Day".

### All

#### Databases

This application uses multiple databases, so you can effectively have multiple types of dictionaries, maybe one for English slangs, one for German slangs and one for Russian slangs. Only one database is loaded and viewed at one time to prevent loading too many entries into the memory, as the entire dictionary loads in memory.

| Feature       | Name            | Action                                                                                                                                                                                                       |
|---------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `file.open`   | Open Database   | Open an existing database, usually under the name `<uuid>.dict`.                                                                                                                                             |
| `file.new`    | Create Database | Create a new database bound to a file, with options to _bootstrap_ the database with default values, like the 7,000 slang words dictionary or the randomly generated 100,000 entries dictionary for testing. |
| `file.close`  | Close Database  | Close the database after asking if the user wants to save any changes.                                                                                                                                       |
| `file.reload` | Reload Database | Drop all changes and reload the database from the file, if it still exists.                                                                                                                                  |
| `file.save`   | Save Database   | Save all changes done to the currently opened database.                                                                                                                                                      |
| `file.bomb`   | Bomb Database   | Nuke all entries on the database. To save this change, use the `file.save` feature.                                                                                                                          |

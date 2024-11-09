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
- [x] Must have a search time under 1s for 100.000 words, even though the provided default file doesn't have that many.
- [x] Must save the current data after modifications.
- [x] Must use a version control system, with at least 10 commits spread across multiple days.

### Features

**Required feature list**:

- [x] Find by the word.
- [x] Find by the definition.
- [ ] View search history.
- [ ] Add a new word. Notify if duplicated, and allow overwriting or adding new.
- [x] Edit a word.
- [ ] Delete a word, with confirmation.
- [x] Reset back to default list.
- [ ] Randomly select a word and display it as the "Word of the Day".

**Bonus feature list**

- `Instant Find`: As you type in the search input, the word list is instantly filtered by what you type. This seems pretty fast because of Java's Streams API.
- `Internationalization`: There's a language change button, where the effect is taken immediately.
- `Favorite Words`: Favorite a word so it will always appear before non-favorite words.
- `View Filter`: You can choose the checkbox "Show Only Favorites" will prune all non-favorite words from showing in the table.

<div align="center">

# Slang Dictionary

### A Java 21 Swing application to look up a local slang dictionary

![Static Badge](https://img.shields.io/badge/java-21-orange?logo=java)
[![Codacy Badge](https://app.codacy.com/project/badge/Grade/da80c89fedf946899ca104426c6f5f31)](https://app.codacy.com/gh/hikawi/slang-dictionary/dashboard?utm_source=gh&utm_medium=referral&utm_content=&utm_campaign=Badge_grade)

</div>

## Takeaways

Main takeaways of this project are:

- Ability to establish, manage, and work in a team. (This doesn't work since he changed it to a solo project instead
  with how few students there are)
- Ability to analyze and write technical documentation for an application.
- Ability to understand and use basic native Java data structures.
- Ability to write basic syntax in Java.
- Ability to utilize Java IO.
- Ability to utilize Java Collections.
- Ability to manage and handle Exceptions.
- Ability to manage a project structure.
- Ability to analyze the given prompt.
- Ability to separate the given required tasks into smaller actions, and finding the appropriate Java classes to
  implement.
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

## Features

### Required Features

- [x] Find by the word.
- [x] Find by the definition.
- [x] View search history.
- [x] Add a new word. Notify if duplicated, and allow overwriting or adding new.
- [x] Edit a word.
- [x] Delete a word, with confirmation.
- [x] Reset back to default list.
- [x] Randomly select a word and display it as the "Word of the Day".
- [x] Quiz Feature

### All Features

#### Databases

This application uses multiple databases, so you can effectively have multiple types of dictionaries, maybe one for
English slangs, one for German slangs and one for Russian slangs. Only one database is loaded and viewed at one time to
prevent loading too many entries into the memory, as the entire dictionary loads in memory.

| Feature       | Name            | Action                                                                                                                                                                                                       |
|---------------|-----------------|--------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------|
| `file.open`   | Open Database   | Open an existing database, usually under the name `<uuid>.dict`.                                                                                                                                             |
| `file.new`    | Create Database | Create a new database bound to a file, with options to _bootstrap_ the database with default values, like the 7,000 slang words dictionary or the randomly generated 100,000 entries dictionary for testing. |
| `file.close`  | Close Database  | Close the database after asking if the user wants to save any changes.                                                                                                                                       |
| `file.reload` | Reload Database | Drop all changes and reload the database from the file, if it still exists.                                                                                                                                  |
| `file.save`   | Save Database   | Save all changes done to the currently opened database.                                                                                                                                                      |
| `file.bomb`   | Bomb Database   | Nuke all entries on the database. To save this change, use the `file.save` feature.                                                                                                                          |
| `file.reset`  | Reset Database  | Reset the database back into default entries.                                                                                                                                                                |
| `file.rename` | Rename Database | Rename the database, but the file name shouldn't be changed.                                                                                                                                                 |

#### Editing

This section provides actions to manipulate the dictionary.

| Feature       | Name              | Action                                                                                 |
|---------------|-------------------|----------------------------------------------------------------------------------------|
| `edit.add`    | Add a word        | Appends a new entry into the dictionary.                                               |
| `edit.edit`   | Edit a word       | Edits an entry's word or definition.                                                   |
| `edit.star`   | Favorite a word   | Marks a word as favorite and prevents its deletion. You can mark multiple at once.     |
| `edit.unstar` | Unfavorite a word | Unmarks a word from faovirte and allows its deletion. You can unmark multiple at once. |
| `edit.lock`   | Lock a word       | Locks a word and prevents it from edits. You can lock multiple at once.                |
| `edit.unlock` | Unlock a word     | Unlocks a word and allows it to be edited. You can unlock multiple at once.            |
| `edit.remove` | Remove a word     | Deletes a word from the dictionary.                                                    |

#### Searching

This application provides multiple ways to customize your searches.

| Feature          | Name                | Action                                                                                                                                |
|------------------|---------------------|---------------------------------------------------------------------------------------------------------------------------------------|
| `search.sort`    | Sort Favorites      | Ability to sort favorite words to top, to bottom, hidden, only show favorites or in an ordinary order with other words.               |
| `search.instant` | Instant Search      | Ability to search as you type using Swing Workers.                                                                                    |
| `search.match`   | Match Searches      | Ability to pick whether the search should match against words or definitions, or casings or uses the search text as a regex.          |
| `search.random`  | Random Word         | See the word of the day, and ability to request a new word. Ability to see a certain message if there is nothing within the database. |
| `search.history` | View Search History | You can see what words were queried, how many results returned, how much time elapsed and when that query happened.                   |

### Quiz

The quiz is formatted in a way, so that your goal would be to go for the highest score possible. Score is affected by your **Combo Stack**, which goes up by how many answers you get correct in a row. The higher your combo goes, the higher your multiplier scales. But be aware, one wrong answer will collapse your multiplier back to 1x.

There are partners, acting as lifelines, you can choose up to 4, each having a unique one-time use ability that may help planning to max out your score gain:

- Noah: Grants immunity to damage and combo break for 10s.
- Mio: Converts 30% of damage taken into score.
- Eunie: Heals 20% of your max HP.
- Taion: Grants immunity to damage from incorrect answers for the next 3 incorrect answers.
- Sena: The next score change is multiplied by 4.
- Lanz: Reduces all damage taken, but also score gains.
- Matthew: Increases combo multiplier by 1.5x.
- A: Heals 5% of your max HP and grants immunity to damage for 10s. (She originally was going to grant immunity to damage, heal and slow down time for that period, but that seems too difficult to do)
- Nikol: Reduces all damage taken greatly, but combo multiplier no longer stacks.
- Glimmer: Heals 10% of your max HP, and grants a multiplier on the next 3 questions.
- Rex: The next score change is multiplied by 10.
- Shulk: Heal 10% of your HP once you take fatal damage.

## Submission Criteria

- [x] The submission is archived.
- [x] Grade yourself, based on max score 100. Each *required* feature is worth 10. If the feature is slow, (over 1s), that feature gets -5. If the UI is unusable and difficult to see, then that feature takes a -3. Of course, unimplemented or errors is a 0. (He was dumb, there were 9 requireds, but he numbered them 1 to 8, and then 10)
- [x] Use other collections, other than List.
- [x] More than 10 commits
- [x] Plagiarism is a full 0 on the course.
- [x] Referencing is OK and needs notes.
- [x] Publish a YouTube video demonstrating and all features.
- [x] Archive file includes source, built jar, default data files, commit image and a .txt file for self-grading.

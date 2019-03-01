# Contributing to WraithEngine

Thank you for taking the time to make WraithEngine a better project for everyone to enjoy! It's because of your support that this project can be what is today. Thank you. You're doing amazing work! Whether you're posting about bugs you've found, proposing code changes, or submitting feature requests, you're helping to build this community. So let's dive into the contribution guidelines!

---

<br>

## Bug Reports and Feature Requests

Bug reports and feature requests submitted for WraithEngine should use a template format when possible. Please avoid making duplicate issues, and instead comment on the original issue. If you are submitting a duplicate bug report, and the original issue has already been closed, please check why it is closed. Do not open a new duplicate issue unless you absolutely have to, such as part of the bug still exist. Check WraithEngine versions and plugins installed. If the issue is caused by a plugin instead of WraithEngine, please constact that plugin first.

---

<br>

## Code Contributions

### New Features

Some features may not align with the core direction of WraithEngine, and should instead be applied as a seperate plugin instead of a core feature. Features such as world editors, or custom file managers are examples of these. WraithEngine is indented to be a library of the most common functions that most games will require, not all features that make be used. This is to keep the projects and servers as close to the required work flow of users as possible. Some more examples are listed below:

**Examples**
- World Builders / Editors
- Custom File Managers (Such as remote file streaming services)
- Effects or Models
- Chat Filters
- Extra Commands (such as commands that are only used in specific circumstances.

Now, there are a few expections to these rules, such as default and test assets. Assets used for the sole purpose of testing or default assets that come included with the engine to get users started before any plugins are installed can be attached. However, most these must be approved by a member of Wraithaven Games before uploading. Please contact us at wraithavengames@gmail.com to get in touch. Some *very small* files may be added to res/TestPlugin/Unit Tests for the purpose of test coverage. These should be kept as small as possible, and have no names of indivuals, places, or events. Try to keep test to something along the lines of 'example', 'test', 'sample, 'abc', and so on. And if you can reuse a file for multiple tests, please do.

All large test assets added to the engine must be approved by Wraithaven Games, and only with the intent of being replaced at a later point in time.

<br>

### Refacoring and Redesign

<br>

**Submitted Code Quality**

Refactoring code is always important. It keeps things clean, and keeps development moving forward. Try and keep your code as clean and neat as possible before uploading a change. Its not realistic to assume code will be flawless on the first pass, so if there's a few things that can be cleaned up, that's okay. As long as it's reasonable. And if you need a piece of code to be looked at again at a later point, submit a Github issue to let everyone else know this piece can be improved.

Interfaces should be used whenever possible (and reasonable) to do so. This will make future refactoring or feature adjustments much more practical. Please wrap your code in interfaces.

<br>

**Editing In-Place Code**

Editing code that has already been submitted can be pretty tricky. Changing the names or functions of specific methods may cause issues with other plugins that have been developed for WraithEngine. If this code is not currently wrapped in an interface, old code should be left alone, (or adjusted so the current functionality remains with the same class and method names) and tagged as depreciated. This will allow old code to be slowly moved out without breaking too many plugins that depended on this code. If the code is wrapped in an interface, try to make adjustments without touching the interface. You may make a new interface and mark the old one as depreciated as needed. If in doubt, ask.

<br>

### Code Readability

<br>

**Documentation**

Document your work. It's just better for everyone. At least for public methods. This is very important for developers who intend to use any APIs you make. Comment lines and private code documentation are recommended but not enforced. This may be done at your own descretion.

It is also worth noting, class names and methods names *should be meaningful!* The purpose is to give a basic idea of how the code works in as few words as possible. This doesn't mean use twenty words per method name, but try to keep the name relavent. "openTerminal" and moveForward" are far more useful than "loop" and "go", for example.

<br>

**API**

All code for WraithEngine is basically a giant API library for plugins. So keed that in mind while writing your code. If your code should not be called from a plugin, keep it private or protected, or package only. This will help users know how they should interact with your code. Any code that is intendent to be called from other parts of the code, wrap it in an interface. That will help ensure code is more cleanly connected with other parts of the project, as well as for plugins. This will also make future development easier for everyone.

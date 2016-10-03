# aqa
An Interpreter for the AQA Pseudocode language (GCSE Computer Science)

## Rationale
When teaching GCSE Computer Science to students in year 10 I noticed that many of them have little in the way of a basic grounding in programming.  The exam board we use at the school where I work is AQA (aqa.org.uk).  In the AQA GCSE Computer Science specification they refer to the 'AQA Pseudocode Language'.  While this isn't intended to be a complete language, the exam board uses this pseudocode for all of their examples in exam questions.

It seemed sensible to me for my students to gain experience in this pseudo-language to help them better understand the exam qeustions.  I started to use it when teaching algorithms etc.  I soon noticed though that, due to the lack of basic experience in programming, my students did not understand how the code worked.

I therefore decided that if I were to write an interpreter for the language, students could try out simple examples in the AQA Pseudocode Language and could see their programs take shape.

This, then, has the advantage of killing two birds with one stone.  Students learn about the mechanics of sequence, selection, iteration, algorithm design and the use of pseudocode to describe solutions, as well as understanding the language in more depth (thus improving understanding of exam questions).

## Implementation
In its current form, this program is designed to read and execute instructions in one step.  The basic building blocks are:

* Tokenizer: splits the input into tokens according to the description of the language provided by AQA.
* Parser: works through the tokens determining what instructions to execute and checking syntax.
* Virtual Machine: holds current state (value stack, variable table, subroutine table).

The parser is implemented as a recursive descent parser according to the grammar specified below.  I've taken as much of the grammar as I can from AQA's own specification and have 'invented' the rest according to what seems sensible to me.

## Language Specification.
Refer to http://filestore.aqa.org.uk/resources/computing/AQA-8520-TG-PC.PDF


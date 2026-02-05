# Reflection Log

This document captures reflections on the development of 3D geometric classes in Java, 
focusing on design patterns, principles, and lessons learned.

## Factory Method Design Pattern: Line3D

For this reflection, I decided to look into the Factory Method design pattern that
Claude implemented in the Line3D class.

### Claude's Comments

Claude explained the Factory Method in the Line3D.java comments as follows:

> FACTORY METHOD PATTERN:
> - Provides static factory methods (fromPoints, fromDirectionVector) for flexible 
> construction
> - These methods offer more semantic clarity than constructors
> - Allows for validation and computation before object creation

Claude also explained each usage of the Factory Method along with the code itself, like
in this example (referring to fromPoints):

> This factory method provides a more explicit and semantically clear way to create
> a line from two points, matching the naming convention of other factory methods.

### What is it?

Based on Claude's comments and my own personal research, I was able to come to a basic
understanding of the usage of the Factory Method pattern.

The Factory Method is essentially used to allow subclasses to instantiate classes. 
Instead of having one constructor method and concrete classes, the Factory Method can 
delay instantiation to the subclasses and allow for more flexibility.

For example, if I were to create a video game with different enemies, I could define
specific enemy types within a constructor `createEnemy()`, like "zombie" and "vampire,"
but I wouldn't be able to add more enemy types without changing all the code that
pertains to `createEnemy()`. With the Factory Method, I can have `createEnemy()` be an
abstract method that refers to its subclasses to actually create the enemy types.

### How does that apply to this project?

In this instance, Claude created a `midpoint()` method that calls `createPoint()` to
create a point. Instead of holding all the types of points possible and determining
which type to use within `midpoint()`, `midpoint()` just calls `createPoint()` and lets
it handle the instantiation.
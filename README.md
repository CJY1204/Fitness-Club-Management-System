# Fitness Club Management System

A Java console-based application that digitizes the operations of a fitness club — member registration, class scheduling, equipment tracking, and administrative reporting — built as a team project for the *Object-Oriented Programming Techniques* course.

## Overview

The system supports three distinct user roles (**Member**, **Trainer**, **Admin**), each with their own dashboard and permissions. It demonstrates core OOP principles including abstraction, inheritance, polymorphism, encapsulation, and object association through a fully working membership and booking workflow.

## Features

- **Role-based Authentication** — separate login flows for Members, Trainers, and Admins
- **Tiered Membership System** — Basic, Silver, and Gold tiers (via an abstract `Membership` class and subclasses), each with different pricing, sauna access, and booking priority
- **Class Scheduling & Booking** — members can view and book fitness classes, with Gold members receiving priority slots
- **Equipment Management** — admins can add, update, and track equipment status (Available / Under Maintenance / Broken)
- **Trainer Management** — create, update, and assign trainers to classes
- **Revenue Reporting** — auto-generated monthly revenue report by membership tier
- **Persistent Data Storage** — all data (members, bookings, equipment, trainers) is saved to text files between sessions

## Tech Stack

- **Language:** Java
- **Paradigm:** Object-Oriented Programming (Abstraction, Inheritance, Polymorphism, Encapsulation)
- **Data Storage:** Flat-file storage (`.txt`) for members, bookings, and equipment records

## My Role

**Project Leader** — coordinated task allocation across a 4-member team and oversaw module integration.

Personally designed and implemented the **Membership & Member Services module**, including:
- Abstract `Membership` class with `BasicMembership`, `SilverMembership`, and `GoldMembership` subclasses
- `Member` class handling personal information, BMI calculation, profile display, class viewing, and booking cancellation

## How to Run

1. Ensure you have a JDK installed (Java 8 or above).
2. Compile all `.java` source files:
   ```
   javac *.java
   ```
3. Run the main system controller:
   ```
   java FitnessSystem
   ```
4. Follow the on-screen menu to register as a member, log in, or access the admin/trainer portals.

## Project Structure (Key Classes)

| Class | Responsibility |
|---|---|
| `Membership`, `BasicMembership`, `SilverMembership`, `GoldMembership` | Membership tiers and fee logic |
| `Member` | Member profile, BMI, bookings |
| `FitnessClass`, `Booking` | Class scheduling and booking management |
| `Equipment` | Equipment tracking |
| `Trainer` | Trainer profile and class assignments |
| `Admin` | Administrative operations and reporting |
| `FitnessSystem` | Main controller — login, registration, workflow |

---
*Developed as part of AMCS2204 Object-Oriented Programming Techniques, TAR University of Management and Technology.*

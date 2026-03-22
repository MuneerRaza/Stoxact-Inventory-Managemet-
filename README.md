# Stoxact — Inventory & Stock Management App

An Android inventory management application built with Java and Firebase for small to medium businesses to track products, manage stock, generate invoices, and monitor business activity.

## Features

- **Stock Management** — Add, edit, delete, and search products with category-based filtering
- **Invoice Generation** — Create and manage invoices tied to inventory items
- **Business Insights** — Visual charts and analytics for stock overview and trends
- **Activity Logs** — Track all inventory changes and operations with timestamps
- **Authentication** — Firebase Auth with email/password and Google Sign-In
- **Cloud Sync** — Firebase Realtime Database for real-time data across devices
- **Onboarding** — Guided slider walkthrough for new users

## Tech Stack

| Component | Technology |
|---|---|
| **Language** | Java |
| **Platform** | Android (Native) |
| **Auth** | Firebase Authentication |
| **Database** | Firebase Realtime Database |
| **UI** | Material Design, Custom XML layouts |
| **Charts** | MPAndroidChart |
| **Concurrency** | Java Multithreading |

## App Structure

```
app/src/main/java/com/example/project/
├── Dashboard.java              # Main activity with navigation drawer
├── Login.java                  # Firebase email & Google auth
├── SignUp.java                 # User registration
├── SplashScreen.java           # Animated splash screen
├── OnBoarding.java             # Intro slider for new users
├── Fragments/
│   ├── HomeFragment.java       # Dashboard overview
│   ├── StockFragment.java      # Product management
│   ├── InvoiceFragment.java    # Invoice creation & tracking
│   ├── InsightFragment.java    # Business analytics & charts
│   ├── LogsFragment.java       # Activity history
│   └── AboutUsFragment.java    # App info
└── Helper/
    ├── Authentication.java     # Auth utilities
    ├── DataBase.java           # Firebase CRUD operations
    ├── Item.java               # Product data model
    ├── StockAdapter.java       # RecyclerView adapter for stock
    ├── InvoiceAdapter.java     # RecyclerView adapter for invoices
    ├── LogsAdapter.java        # RecyclerView adapter for logs
    └── UserData.java           # User profile model
```

## Setup

1. Clone the repository
2. Open in Android Studio
3. Connect your Firebase project (replace `google-services.json`)
4. Build and run on an Android device or emulator

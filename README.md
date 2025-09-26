# EV-Charger-Finder

## Overview  
EV-Charger-Finder is an **Android application** designed to help users find nearby **Electric Vehicle (EV) charging stations**.  
It integrates **Google Maps API** and device GPS to display charger locations, provide search functionality, and show detailed station information.  

---

## ✨ Features  
- **Current Location Tracking** – Detects and centers the map on the user’s location.  
- **Nearby Charger Finder** – Displays EV charging stations on Google Maps.  
- **Search Functionality** – Allows users to search for specific chargers or areas.  
- **Charger Details** – Opens a detail screen with full station information.  
- **Testing Included** – Contains unit tests and instrumentation tests.  

---

## Project Structure  
```
EV-Charger-Finder/
│── app/
│   ├── build.gradle.kts         # App-level Gradle build config
│   ├── proguard-rules.pro       # ProGuard settings
│   ├── src/
│   │   ├── main/
│   │   │   ├── AndroidManifest.xml
│   │   │   ├── java/com/example/assignmentthree/
│   │   │   │   ├── MainActivity.java
│   │   │   │   ├── MapsActivity.java
│   │   │   │   ├── CurrentLocation.java
│   │   │   │   ├── Searching.java
│   │   │   │   ├── DetailActivity.java
│   │   │   │   ├── EVCharger.java
│   │   │   │   └── MyMap.java
│   │   │   ├── res/ (layouts, drawables, values, xml)
│   │   ├── androidTest/ (instrumentation UI tests)
│   │   └── test/ (unit tests)
│── gradle/ (Gradle wrapper)
│── build.gradle.kts
│── settings.gradle.kts
```

---

## Requirements  
- **Android Studio (Arctic Fox or newer)**  
- **Gradle** (comes with Android Studio)  
- **Minimum SDK:** 21 (Android 5.0 Lollipop)  
- **Target SDK:** 34 (Android 14)  
- **Google Maps API Key** (required to run maps feature)  

---

## How to Run  
1. Clone the repository:  
   ```bash
   git clone https://github.com/yourusername/EV-Charger-Finder.git
   cd EV-Charger-Finder
   ```
2. Open the project in **Android Studio**.  
3. Add your **Google Maps API Key**:  
   - Navigate to `app/src/main/AndroidManifest.xml`.  
   - Insert your API key inside the `<meta-data>` tag for `com.google.android.geo.API_KEY`.  
4. Build and run the project on an **emulator or physical device**.  

---

## Running Tests  
- **Unit tests**:  
  ```bash
  ./gradlew test
  ```
- **Instrumentation tests** (UI tests on emulator/device):  
  ```bash
  ./gradlew connectedAndroidTest
  ```

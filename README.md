## google-services.json Download 
**Path: app/google-services.json**
## build.gradle
**Path: (Project level)**
```gradle
classpath 'com.google.gms:google-services:4.4.0'
```
## build.gradle
**Path: app/build.gradle (Module level)**
**plugins**
```gradle
id 'com.google.gms.google-services'
```
**dependencies**
```gradle
implementation 'com.google.firebase:firebase-analytics:21.3.0'
```
## MainActivity.java
**import package**
```java
import com.google.firebase.analytics.FirebaseAnalytics;
```
**Class এর ভিতরে Object declare**
```java
private FirebaseAnalytics firebaseAnalytics;
```
**onCreate() method এর ভিতরে**
```java
    // Firebase Analytics init
    firebaseAnalytics = FirebaseAnalytics.getInstance(this);

    // ONLY TWO EVENTS (Total User + Active User)
    trackUsers();
```
**নিচে এই method টা যোগ করুন**
```java
private void trackUsers() {

    // Total User → জীবনে প্রথমবার open হলেও count হবে
    firebaseAnalytics.logEvent(
            FirebaseAnalytics.Event.APP_OPEN,
            null
    );

    // Active User → প্রতিবার app open হলে count হবে
    firebaseAnalytics.logEvent(
            "active_user",
            null
    );
}
```
## Development Abdul Salam Studio

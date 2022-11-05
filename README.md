<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Supervisors][supervisors-shield]][supervisors-url]
[![Issues][issues-shield]][issues-url]
[![PullRequest][pullrequest-shield]][pullrequest-url]
[![ProjectBoardView][projectboardview-shield]][projectboardview-url]

# Freebies for Newbies

### Sprint 10 update 10/31 -11/04
* Team works on the individual topics assigned for the Workshop2.
* Prepare a presentation on the topics learned for Workshop 2.
* Add datepicker for the datefields.

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ABOUT THE PROJECT -->
## About The Project

This app helps International Students to register themselves as users or local guides and get access to all the events(cultural, sports and food) that are free of cost, find nearby free stuff.
Helps users to find below in their locality.

Software requirements: 
* Android Studio
* Java
* xml
* Database(Back4App for Authentication and user information storage) 
<p align="right">(<a href="#readme-top">back to top</a>)</p>
<!-- ABOUT THE DEPENDENCIES -->

## Dependencies in our android applicaton

Go to your Android Studio Project and find the build.gradle (Module:app) file. After that, copy and paste the code snippet right after the dependencies{} tag.

```groovy
// ... code
dependencies {...}

repositories {
  mavenCentral()
  jcenter()
  maven { url 'https://jitpack.io' }
}
```
Install the latest Parse Android SDK in your application, continue in the build.gradle (Module:app) file and copy and paste the code snippet inside the dependencies{} tag.
```groovy
android {...}

dependencies {
  // code...
  // Don't forget to change the line below with the latest version of Parse SDK for Android
  implementation "com.github.parse-community.Parse-SDK-Android:parse:latest.version.here"
}
```
Inside the strings.xml file, insert the following lines, with your application keys, in order to connect your project to Back4App Database.
* ./app/src/main/res/values/strings.xml
```groovy
<resources>
    <string name="back4app_server_url">https://parseapi.back4app.com/</string>

    <!-- Change BOTH strings as required -->
    <string name="back4app_app_id">APP_ID</string>
    <string name="back4app_client_key">CLIENT_KEY</string>
</resources>
```
You need to grant permission for your Android app to access the internet network. To do this, add the following code snippet to your AndroidManifest.xml file right after the application tag.
* ./app/src/main/AndroidManifest.xml
```groovy
<uses-permission android:name="android.permission.ACCESS_NETWORK_STATE" />
<uses-permission android:name="android.permission.INTERNET" />
```
Also, inside the application tag, add the following:
* ./app/src/main/AndroidManifest.xml
```groovy
<meta-data
  android:name="com.parse.SERVER_URL"
  android:value="@string/back4app_server_url" />
<meta-data
  android:name="com.parse.APPLICATION_ID"
  android:value="@string/back4app_app_id" />
<meta-data
  android:name="com.parse.CLIENT_KEY"
  android:value="@string/back4app_client_key" />
```
Create a Java file called App that extends Application. In this file, use the following line of code to import Parse.
```groovy
import com.parse.Parse;
```
Inside App.java onCreate method, right after super.onCreate() call the following code:
```groovy
Parse.initialize(new Parse.Configuration.Builder(this)
             .applicationId(getString(R.string.back4app_app_id))
             // if defined
             .clientKey(getString(R.string.back4app_client_key))
             .server(getString(R.string.back4app_server_url))
             .build()
);
```
Don’t forget to define this file in the AndroidManifest.xml. To do so, go to the AndroidManifest.xml file and add the following line of code inside the application tag:
```groovy
android:name=".App"
// If the name of the java file that extends Application that you created on the previous step isn’t “App”, don’t forget that the code above should have the correct name of the file
android:name=".name_of_the_file"
```

Useful Links:
* [Parse SDK Insatllation Guide](https://www.back4app.com/docs/android/parse-android-sdk)

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- CONTACT INFO -->
## Contact Info of Collaborators

Collaborators:
* [Jaichand Mulakalapalli (Team Leader)](https://github.com/jaichandm)
* [Adithya Krishna Raavi](https://github.com/Adithyakrishna9)
* [Saibabu Devarapalli](https://github.com/saibabu369)
* [Manoj Kumar Gude](https://github.com/manoj2205)
* [Harish Chowdary Bala](https://github.com/Harish6600)

Supervisor:
* [Dr. Chandra Mouli](https://github.com/cm2kotteti)

<p align="right">(<a href="#readme-top">back to top</a>)</p>


<!-- MARKDOWN LINKS & IMAGES -->

[contributors-shield]: https://img.shields.io/badge/Contributors-5-brightgreen
[contributors-url]: https://github.com/Sec03Team05Fall22GDP1/FreebiesforNewbies/graphs/contributors
[supervisors-shield]: https://img.shields.io/badge/Supervisors-1-yellowgreen
[supervisors-url]: https://github.com/Sec03Team05Fall22GDP1/FreebiesforNewbies/collaborators
[issues-shield]: https://img.shields.io/badge/Issues-0-red
[issues-url]: https://github.com/Sec03Team05Fall22GDP1/FreebiesforNewbies/issues
[pullrequest-shield]: https://img.shields.io/badge/Pull%20Requests-0-orange
[pullrequest-url]: https://github.com/Sec03Team05Fall22GDP1/FreebiesforNewbies/pulls
[projectboardview-shield]: https://img.shields.io/badge/Project%20BoardView-green
[projectboardview-url]: https://github.com/orgs/Sec03Team05Fall22GDP1/projects/1/views/2

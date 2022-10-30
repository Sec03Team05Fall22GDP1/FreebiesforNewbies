<a name="readme-top"></a>

<!-- PROJECT SHIELDS -->
[![Contributors][contributors-shield]][contributors-url]
[![Supervisors][supervisors-shield]][supervisors-url]
[![Issues][issues-shield]][issues-url]
[![PullRequest][pullrequest-shield]][pullrequest-url]
[![ProjectBoardView][projectboardview-shield]][projectboardview-url]

# Freebies for Newbies

### Sprint 8 update 10/10 -10/14
* Event Home & EventCreate pages Desgin Layouts in android studio
* Database Events object Creation in back4App
* Home & Event pages java Backend code 

<p align="right">(<a href="#readme-top">back to top</a>)</p>

<!-- ABOUT THE PROJECT -->
## About The Project

This app helps International Students to register themselves as users or local guides and get access to all the events(cultural, sports and food) that are free of cost, find nearby free stuff.
Helps users to find below in their locality.

Software requirements: 
* Android Studio
* Java
* xml
* Database(Firebase for Authentication and user information storage) 
<p align="right">(<a href="#readme-top">back to top</a>)</p>
<!-- ABOUT THE DEPENDENCIES -->

## Dependencies in our android applicaton

In your `app/build.gradle` file add a dependency on one of the Firebase libraries.

```groovy
dependencies {

	// Add the dependency for the Google services Gradle plugin
	// The google-services plugin is required to parse the google-services.json file
	classpath 'com.google.gms:google-services:4.3.13'
	
	//SDK used for firebase 
	implementation 'com.google.firebase:firebase-auth:21.0.8'
	// Firebase UI Library this can be also used instead of above code "implementation 'com.firebaseui:firebase-ui-auth:8.0.1'"
	
	// TODO: Add the dependencies for Firebase products you want to use
	// Import the Firebase BoM
	implementation platform('com.google.firebase:firebase-bom:30.4.1')
	
	// When using the BoM, don't specify versions in Firebase dependencie. Contains public API classes for Firebase Analytics.
	implementation 'com.google.firebase:firebase-analytics'

}
```
Useful Links:
* [Firebase Auth SDK Reference](https://firebase.google.com/docs/reference/android/com/google/firebase/auth/package-summary?authuser=0&hl=en)
* [Authenticate with Firebase using Password-Based Accounts on Android](https://firebase.google.com/docs/auth/android/password-auth?hl=en&authuser=0)

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

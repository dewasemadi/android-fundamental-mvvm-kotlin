
# Bangkit 2022: Android Fundamental
> Master branch is the final submission (submission 3)

![summary](https://user-images.githubusercontent.com/66185022/161162665-2d33e5a8-33cd-4cc8-b877-94a038b52e78.png)
> Just  **star** or  **fork** this repository, and follow my github. You have *supported* me!

This repository contains the source code of my submissions project at Dicoding "Belajar Fundamental Aplikasi Android" course, start from the first submission until the final submission. This course is a part of self-paced learning at Bangkit 2022 Mobile Development learning path.

## Setup Your GitHub API's Access Token

Token on this project is currently invalid anymore, You need to change the GitHub API token with your own token in Build.Gradle(app). [This link](https://docs.github.com/en/authentication/keeping-your-account-and-data-secure/creating-a-personal-access-token) may help you to get GitHub access token.

```
android {
    compileSdk 31
    
    defaultConfig {
        applicationId "com.dicoding.githubuser"
        minSdk 21
        targetSdk 31
        versionCode 1
        versionName "1.0"
        testInstrumentationRunner "androidx.test.runner.AndroidJUnitRunner"

        buildConfigField("String", "ACCESS_TOKEN", '"<Your Own Access Token Here>"')
    }
    ...
}
```
    
    
## Dependencies

- [Andoidx](https://mvnrepository.com/artifact/androidx)
- [Glide](https://github.com/bumptech/glide)
- [Retrofit](https://square.github.io/retrofit/)
- [OkHttp](https://square.github.io/okhttp/)
- [Lifecycle & LiveData](https://developer.android.com/jetpack/androidx/releases/lifecycle)
- [Coroutines](https://developer.android.com/kotlin/coroutines)
- [DataStore](https://developer.android.com/jetpack/androidx/releases/datastore)
- [Room](https://developer.android.com/jetpack/androidx/releases/room)
- [ViewPager2](https://developer.android.com/jetpack/androidx/releases/viewpager2)
- [Android KTX](https://developer.android.com/kotlin/ktx)
- [Mockito](https://site.mockito.org/)
- [Espresso](https://developer.android.com/training/testing/espresso/setup)

## Features

- MVVM (Model-View-ViewModel) Architecture
- Room Persistence for local database
- ViewBinding & DataBinding
- Retrofit to request api network
- Glide for load image from url
- Light/Dark mode theme
- Splashscreen
- Error handling when API's request failed
- Loading indicator

## Showcase Project (Final Submission)
<table>
  <tr>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167506-8cc27492-8f02-4563-b451-978618421b81.jpg"/></td>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167597-57b00846-16d1-4c11-9a3b-03b1f80633ca.jpg"/></td>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167660-c524a9ba-26e9-4721-9c58-cca6c329635c.jpg"/></td>
  </tr>
</table>

<table>
  <tr>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167718-52cef2d1-0baf-44ab-ae16-0c7a2e48eb61.jpg"/></td>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167766-f4885cdf-57e1-4de2-bcc0-c1343241ba85.jpg"/></td>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167802-4cbc8ef1-6f13-424a-9c13-6825849083b3.jpg"/></td>
  </tr>
</table>

<table>
  <tr>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167888-7db13155-e19e-4102-a77b-0a111beb276d.jpg"/></td>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161167913-bd331b01-9a9d-4006-afb7-f04fc384bae7.jpg"/></td>
    <td valign="top"><img src="https://user-images.githubusercontent.com/66185022/161168297-f86ae876-f25a-41aa-8a30-4c5a5d4f2048.jpg"/></td>
  </tr>
</table>


## Testing (only 1 case)

| No  | Scenario                                                                                                     | Pre-requisites             | Steps                                                                                                               | Expected Results                    | Actual Result | Status |
| --- | ------------------------------------------------------------------------------------------------------------ | -------------------------- | ------------------------------------------------------------------------------------------------------------------- | ----------------------------------- | ------------- | ------ |
| 1   | Add user to favorite                                                                     | User has opened the application and internet network is active | Search for GitHub users then selects one. In detail page, tap the favorite floating button.                       | Favorite users are on the favorites page               | As expected   | Pass ✔ |

![unit test](https://user-images.githubusercontent.com/66185022/161169684-e8129df8-cdb1-4b9d-a2f6-3b276d7c625f.png)

## Author
[**I Dewa Putu Semadi**](https://www.linkedin.com/in/dewasemadi/) - dewasemadi@apps.ipb.ac.id
## Credit
[StorySet](https://storyset.com/)

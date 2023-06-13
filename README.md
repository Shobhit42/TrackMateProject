# TrackMate

<p align="center">
  <img src="https://github.com/Shobhit4225/TrackMate-App/raw/master/studio.png" alt="TrackMate Logo">
</p>

TrackMate is a mobile app designed to help users track their daily physical activities like running, walking, and cycling and monitor their performance. With its intuitive interface and powerful features, TrackMate allows users to set goals, track progress, and analyze their data for better insights.

## Description

TrackMate is a comprehensive mobile app that combines advanced tracking capabilities with user-friendly features. It provides an all-in-one solution for tracking various activities, setting goals, and visualizing progress. The app is built using Kotlin, XML, Firebase, Room Database, MongoDB, and Node.js, ensuring a seamless and secure user experience. TrackMate incorporates a social media feature that allows users to share their achievements and fitness milestones with others.

## Features

### 1. Authentication

The authentication feature allows users to securely sign up, log in, and manage their accounts.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/splash.png" alt="Authentication Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/loginpage.png" alt="Authentication Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/signuppage.png" alt="Authentication Screenshot 2" width="250"/>
</div>

### 2. Setup page

TrackMate enables users to select their avatar and Asks for the NickName , Weight of the user, so that it can be usered for measuring calories burned

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/setuppage1.png" alt="Activity Tracking Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/setuppage2.png" alt="Activity Tracking Screenshot 2" width="250"/>
</div>

### 3. Dashboard and Settings

The TrackMate dashboard provides users with an at-a-glance view of their overall performance. It showcases weekly and lifetime performance summaries, helping users track their progress and identify areas for improvement. The settings tab allows customization of profile details, such as the profile image, name, and weight. With these features, TrackMate offers a comprehensive and personalized fitness tracking experience.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/dashboardpage1.png" alt="Activity Tracking Screenshot 1" width="250" height="500"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/dashboardpage2.png" alt="Activity Tracking Screenshot 2" width="250" height="500"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/settings1.png" alt="Activity Tracking Screenshot 2" width="250" height="500"/>
</div>

### 4. Location Access Permission Handling

TrackMate implements robust permission handling for accessing the user's location. Here's how it works:

  Permission Request: When the app requires access to the user's location, TrackMate requests the necessary permissions. This ensures that the app can retrieve the user's     location data accurately.

  Permission Dialog: A permission dialog is displayed to the user, explaining why the app requires location access and how it will be used. The user can choose to grant or   deny the permission based on their preference.

  Permission Handling: TrackMate handles the permission response accordingly. If the permission is granted, the app proceeds to retrieve the user's location data. If the     permission is denied, the app adjusts its functionality accordingly and informs the user about the limitations.

  Permission Settings: TrackMate provides a direct link to the device's settings, allowing users to modify the location permission at any time. This ensures transparency     and empowers users to control their privacy settings.

By implementing comprehensive permission handling, TrackMate prioritizes user privacy and ensures the secure and responsible use of location data.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/permission1.jpeg" alt="Activity Tracking Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/permission2.jpeg" alt="Activity Tracking Screenshot 2" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/permission3.jpeg" alt="Activity Tracking Screenshot 2" width="250"/>
</div>

### 5. Activity Statistics

The Activity Statistics feature in TrackMate presents users with a dedicated page that showcases interactive and animated graphs. Users can track their progress and view detailed statistics for various activities. The graph type can be easily toggled using a convenient toggle button, providing users with the flexibility to analyze their data in different ways. With this feature, users can gain valuable insights into their performance and monitor their progress over time.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/walkstatistics.png" alt="Activity Tracking Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/runstatistics.png" alt="Activity Tracking Screenshot 2" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/cyclestatistics.png" alt="Activity Tracking Screenshot 2" width="250"/>
</div>

### 6. Track History

View and manage your recorded tracks with the Track History feature. Easily sort the track list by date, calories burned, or distance covered to find specific tracks. Get a quick overview of essential details like average speed and duration. Click on any track to access a detailed view with a map visualization, elevation profile, and more. Gain valuable insights into your performance and track your progress effortlessly.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/list1.png" alt="Activity Tracking Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/list2.png" alt="Activity Tracking Screenshot 2" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/list3.png" alt="Activity Tracking Screenshot 2" width="250"/>
</div>

### 7. Realtime Tracking Activity of User

One of the key features of TrackMate is its real-time tracking functionality, which allows users to track their activities with precision. Here's how it works:

- **Start Tracking**: Users can initiate the tracking process by selecting the "Start Tracking" option. This action triggers the foreground services, ensuring that the tracking continues seamlessly. A notification is displayed, showcasing the elapsed time in milliseconds. The notification also provides two actions: "Pause" and "Resume," allowing users to control the tracking process conveniently.

- **Activity Mode Selection**: When starting the tracking, users are presented with a dialogue box that prompts them to select the desired activity mode. The dialogue box incorporates animations and toggle buttons, enabling users to choose from various activity modes, such as walking, running, cycling, etc.

- **Cancellation Option**: If a user decides to cancel the tracking session, they have the option to do so. This feature provides flexibility and allows users to make changes or abort the activity if needed.

- **Map View and Polyline**: To provide a visual representation of the tracked path, TrackMate utilizes a map view. The map displays the user's current location and draws a polyline that accurately depicts the route taken during the activity. This visual feedback allows users to visualize their progress and follow their path on the map.

With its real-time tracking capabilities, activity mode selection, and visual representation of the user's path, TrackMate offers a comprehensive and engaging experience for users to track and monitor their activities effectively.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/starttrackingpage1.png" alt="Realtime Tracking Screenshot" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/selectmodepage.png" alt="Realtime Tracking Screenshot" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/notification3.jpeg" alt="Realtime Tracking Screenshot" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/trackpage2.png" alt="Realtime Tracking Screenshot" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/trackpage3.png" alt="Realtime Tracking Screenshot" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/trackpage1.png" alt="Realtime Tracking Screenshot" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/canclepage.png" alt="Realtime Tracking Screenshot" width="250"/>
</div>


### 8. Overall Performance Analysis

TrackMate's Overall Performance Analysis feature provides a comprehensive overview of your overall performance. With responsive and animated bar and line charts, you can visualize your progress across different activities in a single graph. Analyze trends, compare performance metrics, and gain valuable insights into your fitness journey. Stay motivated and track your achievements with this powerful statistical tool.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/overallstatisticspage1.png" alt="Activity Tracking Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/overallstatisticspage2.png" alt="Activity Tracking Screenshot 2" width="250"/>
</div>

### 9. Social Media Integration and Achievements Sharing

TrackMate incorporates a social media feature that allows users to share their achievements and fitness milestones with others. Users can post their accomplishments and receive likes and comments from fellow users, fostering a sense of community and motivation. The implementation includes server-side pagination to efficiently manage large amounts of data and ensure optimal performance. Additionally, client-side pagination enhances the user experience by loading content in smaller, manageable chunks. This combination of server-side and client-side pagination ensures a smooth and seamless social media experience for users while maintaining performance and scalability.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/social1.jpeg" alt="Activity Tracking Screenshot 1" width="250"/>
</div>


### 10. Notification System

TrackMate incorporates a comprehensive notification system that enhances user engagement and provides timely updates. The system includes three types of notifications:

- **Sticky Notification**: This notification is displayed when the user is actively tracking their activity. It serves as a persistent indicator that the foreground service is running, ensuring that the user is aware of ongoing tracking activities.

- **Inactivity Reminder**: To encourage regular app usage, TrackMate sends a notification to users who have been inactive for more than 24 hours. This serves as a reminder to engage with the app and continue their fitness journey.

- **New Post Notification**: Users are notified when another user creates a new post in the social media feature. This keeps users informed about new content and encourages interaction within the TrackMate community.

By incorporating these notification types, TrackMate ensures that users stay informed, engaged, and motivated throughout their fitness journey.

<div align="left">
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/notification1.png" alt="Activity Tracking Screenshot 1" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/notification2.png" alt="Activity Tracking Screenshot 2" width="250"/>
  <img src="https://github.com/Shobhit4225/TrackMate-App/blob/main/Images/notification3.jpeg" alt="Activity Tracking Screenshot 2" width="250"/>
</div>

## Getting Started

To get started with TrackMate, follow these steps:

1. Step 1: Clone the repository
2. Step 2: Install the necessary dependencies
3. Step 3: Configure your API keys and credentials
4. Step 4: Build and run the app

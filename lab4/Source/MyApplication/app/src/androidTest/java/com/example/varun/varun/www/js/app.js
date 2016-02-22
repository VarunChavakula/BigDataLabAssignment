// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.controllers' is found in controllers.js
angular.module('starter', ['ionic', 'starter.controllers'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if (window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
      cordova.plugins.Keyboard.disableScroll(true);

    }
    if (window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleDefault();
    }
  });
})

.config(function($stateProvider, $urlRouterProvider) {
  $stateProvider

    .state('app', {
    url: '/app',
    abstract: true,
    templateUrl: 'templates/menu.html',
    controller: 'AppCtrl'
  })

  .state('app.MostPopularLocations', {
    url: '/MostPopularLocations',
    views: {
      'menuContent': {
        templateUrl: 'templates/MostPopularLoc.html'
      }
    }
  })
  .state('app.MostFollowedProfiles', {
    url: '/MostFollowedProfiles',
    views: {
      'menuContent': {
        templateUrl: 'templates/MostFollowedProfiles.html'
      }
    }
  })
  .state('app.TimeZoneTweets', {
    url: '/TimeZoneTweets',
    views: {
      'menuContent': {
        templateUrl: 'templates/Tweetstimezone.html'
      }
    }
  })
  .state('app.MostPopularTimezones', {
    url: '/MostPopularTimezones',
    views: {
      'menuContent': {
        templateUrl: 'templates/PopularLoc.html'
      }
    }
  })
  .state('app.PopularFoodVarieties', {
    url: '/PopularFoodVarieties',
    views: {
      'menuContent': {
        templateUrl: 'templates/Food.html'
      }
    }
  })
  .state('app.MostPopularLocationforsales', {
    url: '/MostPopularLocationforsales',
    views: {
      'menuContent': {
        templateUrl: 'templates/MostPopularLocationSales.html'
      }
    }
  })
  .state('app.SensitiveTweets', {
    url: '/SensitiveTweets',
    views: {
      'menuContent': {
        templateUrl: 'templates/Sensitive.html'
      }
    }
  })
  .state('app.PopularvarifiedTweeters', {
    url: '/PopularvarifiedTweeters',
    views: {
      'menuContent': {
        templateUrl: 'templates/VerifiedTweets.html'
      }
    }
  })
});

'use strict';

/* Filters */
// need load the moment.js to use this filter. 
angular.module('app.filters', [])
  .filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
  })
  .filter('titleFilters', function() {
    return function(title) {
    	var reg = new RegExp("\\?","g"); 
		title = title.replace(reg,""); 					
		var tit_disp = title;

        return tit_disp;
    }
  });
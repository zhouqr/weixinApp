
/* Filters */
angular.module('weixin.filters', [])
  .filter('fromNow', function() {
    return function(date) {
      return moment(date).fromNow();
    }
  })
  .filter('titleFilters', function() {
    return function(title) {
    	if(title == undefined){
    		return;
    	}
    	var reg = new RegExp("\\?","g"); 
		title = title.replace(reg,""); 					
		var tit_disp = title;

        return tit_disp;
    }
  });
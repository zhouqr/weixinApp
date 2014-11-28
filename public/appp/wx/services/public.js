
angular.module('services.public', [])
    .factory('Publics',function($http){
        return {
            publicList:function(page,pageSize){
            	return $http({method:'get',url:'/AttrWeixin/attrList',params:{ 
            	  page:page,
  				  pageSize:pageSize,}
            	})
            },
            publicCheck:function(open_id){
            	return $http({method:'get',url:'/Search/wxDetail',params:{ 
            		open_id:open_id,
    			 }
              	})
            },
            publicAdd:function(params){
            	return $http({method:'get',url:'/AttrWeixin/attr',params:params
              	})
            },
            
        }
    });

    
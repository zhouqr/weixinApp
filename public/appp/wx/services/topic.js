
angular.module('services.topics', [])
    .factory('Topics',function($http){
        return {
            topicCount:function(){
                return $http({method:'get',url:'/Topic/topicCount'})
            },
            topicList:function(page,pageSize){
            	return $http({method:'get',url:'/topic/topicList',params:{ 
            	  page:page,
  				  pageSize:pageSize,}
            	})
            },
            topicAdd:function(params){
            	return $http({method:'POST',url:'/Topic/addTopic',params:params
              	})
            },
            topicEdit:function(params){
            	return $http({method:'POST',url:'/Topic/updateTopic',params:params
            	})
            },
            topicDel:function(id){
            	return $http({method:'get',url:'/Topic/delTopic',params:{ 
              	  id:id}
              	})
            },
            topicDetail:function(id){
            	return $http({method:'get',url:'/Topic/topicDetail',params:{ 
                	  id:id}
                })
            },
            topicDetailList:function(id,page,pageSize){
            	return $http({method:'get',url:'/Topic/getTopicDocs',params:{ 
            	  id:id,
              	  page:page,
              	  pageSize:pageSize,}
              	})
            },
        }
    });

    
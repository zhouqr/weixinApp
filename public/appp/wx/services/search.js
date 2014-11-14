
angular.module('services.search', [])

    .constant("Global",{
        board:{
            pagesize:5
        },
        post:{
            pagesize:5
        }
    })
    .factory('Search',function($http){

        var Search = function(searchInfo){
            angular.extend(this, searchInfo);
        }

        Search.searchArtical=function(keywords){
            return $http({method:'get',url:'/search/searchArticals',params:{
            	keywords:keywords
            }})
        }
        Search.searchWxs=function(keywords){
        	return $http({method:'get',url:'/search/searchWxs',params:{
        		keywords:keywords
        	}})
        }
        
        Search.searchWxDetail=function(open_id){
        	return $http({method:'get',url:'/search/get_wx_openid',params:{
        		open_id:open_id
        	}})
        }

        /*Post.getPostDetail=function(postId,page,pagesize){
            return $http.get('/postDetail/' + postId+'?page='+page+'&pageSize='+pagesize);
        }*/

        return Search;
    })

    .factory('Post',function($http){

        var Post = function(postInfo){
            angular.extend(this, postInfo);
        }

        Post.reply=function(boardId,replyPostId,content){
            return $http({method:'post',url:'/post',data:{
                boardId:boardId,
                title:'Reply',
                content:content,
                replyPostId:replyPostId
            }})
        }

        Post.getPostDetail=function(postId,page,pagesize){
            return $http.get('/postDetail/' + postId+'?page='+page+'&pageSize='+pagesize);
        }

        return Post;
    })

    .factory('Board',function(Post,$http,$q,Global){

        var Board = function (boardInfo) {
            angular.extend(this, boardInfo);
        }

        Board.prototype.createPost = function (title, content) {
            var defer=$q.defer();

            $http({method: 'post', url: '/post', data: {
                boardId: this.id,
                title: title,
                content: content
            }})
                .success(function(postInfo){
                    defer.resolve(new Post(postInfo));
                })
                .error(function(){
                    defer.reject();
                });

            return defer.promise;
        }

        Board.prototype.getPost=function(postId){
            var defer=$q.defer();
            $http.get('/post/' + postId).success(function(postInfo){
                defer.resolve(new Post(postInfo));
            })
                .error(function(){
                    defer.reject();
                });
            return defer.promise;
        }

        Board.prototype.getPosts = function (page, pageSize) {
            page = page || 1;
            pageSize = pageSize || Global.board.pagesize;
            return $http({method: 'get', url: '/board/' + this.id + '/posts', params: {
                page: page,
                pageSize: pageSize
            }});
        }
        return Board;
    })

    .factory('BBS', function ($http, $q,Board) {

        var BBS = function () {

        }

        BBS.prototype.getBoard = function (boardId) {
            var defer = $q.defer();
            $http.get('/board/' + boardId).success(function (boardInfo) {
                defer.resolve(new Board(boardInfo));
            })
                .error(function () {
                    defer.reject();
                })

            return defer.promise;
        }

        BBS.prototype.getBoards = function () {
            return $http.get('/boards');
        }

        return new BBS();
    })
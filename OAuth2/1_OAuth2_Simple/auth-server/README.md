Authentication : Verifies you are who you say you are.
				 Through Login UI form, Http Headers in a POST etc.
Authorization:	 Decides if you have the permission to access a resource.
				 You don't need to do anything. All checks are internal after you are
				 authenticated.
				
/oauth/token :		The token endpoint is the endpoint on the authorization server where the 					client application exchanges the authorization code, client ID and client 					secret, for an access token.
/oauth/authorize :  The authorization endpoint is the endpoint on the authorization server 					where the resource owner logs in, and grants authorization to the client 					application.				 


http://tutorials.jenkov.com/oauth2/overview.html